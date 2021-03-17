package com.bytecub.mdm.service.impl;

import java.lang.reflect.Field;
import java.util.*;

import com.bytecub.mdm.cache.IProductFuncCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.bytecub.common.biz.EsUtil;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.request.prop.TemplateReqDto;
import com.bytecub.common.domain.dto.response.prop.TemplateResDto;
import com.bytecub.common.domain.storage.EsMessage;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.MetaDesc;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.mdm.convert.po2dto.ProductFucPoConvert;
import com.bytecub.mdm.dao.dal.ProductFuncPoMapper;
import com.bytecub.mdm.dao.po.ProductFuncPo;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.plugin.es.config.enums.BcEsMetaType;
import com.bytecub.plugin.es.domain.PropertiesItem;
import com.bytecub.storage.IDataCenterService;
import com.bytecub.utils.JSONProvider;

import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: ProductFuncServiceImpl.java, v 0.1 2020-12-11  Exp $$  
 */
@Slf4j
@Service
public class ProductFuncServiceImpl implements IProductFuncService {
    @Autowired
    ProductFuncPoMapper funcPoMapper;
    @Autowired
    IDataCenterService dataCenterService;
    @Autowired
    IProductFuncCache productFuncCache;

    @Override
    public List<ProductFuncItemResDto> ListFuncByProductCode(String productCode, Integer funcStatus,
        ProductFuncTypeEnum typeEnum) {
        List<ProductFuncItemResDto> resultList = new ArrayList<>();
        if (StringUtils.isEmpty(productCode)) {
            return resultList;
        }
        List<ProductFuncPo> cacheList = productFuncCache.getListProperties(productCode, typeEnum.getType(), funcStatus);
        if (!CollectionUtils.isEmpty(cacheList)) {
            return ProductFucPoConvert.list(cacheList);
        }
        /** 重新装载缓存 */
        ProductFuncPo queryCacheDo = new ProductFuncPo();
        queryCacheDo.setFuncType(typeEnum.getType());
        queryCacheDo.setProductCode(productCode);
        queryCacheDo.setDelFlag(0);
        List<ProductFuncPo> innerList = funcPoMapper.select(queryCacheDo);
        productFuncCache.setProperties(productCode, typeEnum.getType(), innerList);

        ProductFuncPo queryDo = new ProductFuncPo();
        queryDo.setProductCode(productCode);
        if (null != typeEnum) {
            queryDo.setFuncType(typeEnum.getType());
        }
        queryDo.setFuncStatus(funcStatus);
        queryDo.setDelFlag(0);
        List<ProductFuncPo> list = funcPoMapper.queryByUnion(queryDo);
        resultList = ProductFucPoConvert.list(list);
        return resultList;
    }

    @Override
    public ProductFuncItemResDto queryFunc(String productCode, ProductFuncTypeEnum typeEnum, String identifier) {
        if (StringUtils.isEmpty(productCode)) {
            return null;
        }
        ProductFuncPo cache = productFuncCache.getProperty(productCode, typeEnum.getType(), identifier, 1);
        if (null != cache) {
            return ProductFucPoConvert.item(cache);
        }
        ProductFuncPo queryDo = new ProductFuncPo();
        queryDo.setProductCode(productCode);
        if (null != typeEnum) {
            queryDo.setFuncType(typeEnum.getType());
        }
        queryDo.setFuncStatus(1);
        queryDo.setDelFlag(0);
        queryDo.setIdentifier(identifier);
        List<ProductFuncPo> list = funcPoMapper.queryByUnion(queryDo);
        if (!CollectionUtils.isEmpty(list)) {
            productFuncCache.setProperty(productCode, typeEnum.getType(), list.get(0));
        }
        return CollectionUtils.isEmpty(list) ? null : ProductFucPoConvert.item(list.get(0));
    }

    @Override
    public void insert(ProductFuncPo record) {
        try {
            funcPoMapper.insertSelective(record);
            productFuncCache.setProperty(record.getProductCode(), record.getFuncType(), record);
        } catch (DataIntegrityViolationException e) {
            throw new BCGException(BCErrorEnum.IDENTIFIER_UNIQUE_ERROR, record.getIdentifier(), e);
        }

    }

    @Override
    public void update(ProductFuncPo record) {
        ProductFuncPo query = funcPoMapper.selectByPrimaryKey(record.getId());
        if (null == query) {
            return;
        }
        funcPoMapper.updateByPrimaryKeySelective(record);
        productFuncCache.removeKey(query.getProductCode(), query.getFuncType());
    }

    @Override
    public ProductFuncPo queryById(Long id) {
        /** 这里不缓存了，因为这里只有管理后台手动操作，频率很低 */
        return funcPoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateByTypeIdentifier(ProductFuncPo record) {
        ProductFuncPo query = new ProductFuncPo();
        query.setIdentifier(record.getIdentifier());
        query.setFuncType(record.getFuncType());
        List<ProductFuncPo> search = funcPoMapper.queryByUnion(query);
        if (!CollectionUtils.isEmpty(search)) {
            return 0;
        }
        ProductFuncPo searchItem = search.get(0);
        productFuncCache.remove(searchItem.getProductCode(), searchItem.getFuncType());
        return funcPoMapper.updateByIdentifier(record);
    }

    @Override
    public void releaseProp(Long propId) {
        if (null == propId) {
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM);
        }
        ProductFuncPo query = new ProductFuncPo();
        query.setDelFlag(0);
        query.setId(propId);
        ProductFuncPo po = funcPoMapper.selectOne(query);
        if (null == po) {
            throw BCGException.genException(BCErrorEnum.INVALID_PARAM);
        }
        ProductFuncTypeEnum funcTypeEnum = ProductFuncTypeEnum.explain(po.getFuncType());
        /** 创建上报模版 */
        this.createIndexTemplate(po.getProductCode(), funcTypeEnum, po);
        /** 创建属性设置模版 */
        /** 更新状态从草稿到已发布 */
        this.updateFuncStatus(po, funcTypeEnum);
    }

    @Override
    public TemplateResDto template(TemplateReqDto reqDto) {
        ProductFuncTypeEnum funcTypeEnum = ProductFuncTypeEnum.explain(reqDto.getFuncType());
        List<ProductFuncItemResDto> list = new ArrayList<>();
        if (!StringUtils.isEmpty(reqDto.getIdentifier())) {
            ProductFuncItemResDto item = this.queryFunc(reqDto.getProductCode(), funcTypeEnum, reqDto.getIdentifier());
            list.add(item);
        } else {
            list = this.ListFuncByProductCode(reqDto.getProductCode(), 1, funcTypeEnum);
        }
        return this.processTemplate(list, funcTypeEnum);
    }

    @Override
    public ProductFuncItemResDto listByProdIdType(String productCode, String identifier, String funcType) {
        ProductFuncPo query = new ProductFuncPo();
        query.setProductCode(productCode);
        query.setIdentifier(identifier);
        query.setFuncType(funcType);
        ProductFuncPo record = funcPoMapper.selectOne(query);
        return ProductFucPoConvert.item(record);
    }


    /**
     * 处理单独的物模型
     */
    private TemplateResDto processTemplate(List<ProductFuncItemResDto> items, ProductFuncTypeEnum funcTypeEnum) {
        if (CollectionUtils.isEmpty(items)) {
            return null;
        }
        TemplateResDto resultDto = new TemplateResDto();
        Map<String, Object> demoData = this.calcDemoValue(items, funcTypeEnum);
        Map<String, Object> template = new HashMap<>();

        for (ProductFuncItemResDto item : items) {
            if (ProductFuncTypeEnum.SERVICE.equals(funcTypeEnum)) {
                template.put(item.getIdentifier(), JSONProvider.fromString(item.getInputParam()));
            } else {
                template.put(item.getIdentifier(), JSONProvider.fromString(item.getAttr()));
            }
        }
        resultDto.setDemoData(demoData);
        resultDto.setTemplate(template);
        return resultDto;
    }

    /** 根据数据类型分类计算数据模版默认值 */
    private final Map<String, Object> calcDemoValue(List<ProductFuncItemResDto> items,
        ProductFuncTypeEnum funcTypeEnum) {
        Map<String, Object> result = new HashMap<>();
        for (ProductFuncItemResDto item : items) {
            BcMetaType bcMetaType = BcMetaType.explain(item.getDataType());
            if (null == bcMetaType) {
                /** 说明是服务调用的无参数调用 */
                result.put(item.getIdentifier(), null);
                continue;
            }
            switch (bcMetaType.getType()) {
                case "num":
                    result.put(item.getIdentifier(), 0);
                    break;
                case "text":
                    result.put(item.getIdentifier(), "示例数据");
                    break;
                case "date":
                    result.put(item.getIdentifier(), System.currentTimeMillis());
                    break;
                case "boolean":
                    result.put(item.getIdentifier(), false);
                    break;
                case "struct":
                    if (ProductFuncTypeEnum.SERVICE.equals(funcTypeEnum)) {
                        ProductFuncItemResDto itemResDto =
                            JSONProvider.parseObject(item.getInputParam(), ProductFuncItemResDto.class);
                        itemResDto.setIdentifier(item.getIdentifier());
                        if ("struct".equals(itemResDto.getDataType())) {
                            /** 一旦进入input数据里面，数据结构就和prop一致了 */
                            result.put(item.getIdentifier(),
                                this.calcDemoValue(itemResDto.getData(), ProductFuncTypeEnum.PROP));
                        } else if ("".equals(itemResDto.getDataType())) {
                            result.put(item.getIdentifier(), null);
                        } else {
                            List<ProductFuncItemResDto> list = new ArrayList<>();
                            list.add(itemResDto);
                            Map<String, Object> objectMapper = this.calcDemoValue(list, ProductFuncTypeEnum.PROP);
                            result.putAll(objectMapper);
                        }

                    } else {
                        ProductFuncItemResDto itemResDto =
                            JSONProvider.parseObject(item.getAttr(), ProductFuncItemResDto.class);
                        result.put(item.getIdentifier(), this.calcDemoValue(itemResDto.getData(), funcTypeEnum));
                    }

                    break;
                default:
                    return result;
            }
        }

        return result;
    }

    private void updateFuncStatus(ProductFuncPo source, ProductFuncTypeEnum funcTypeEnum) {
        ProductFuncPo update = new ProductFuncPo();
        update.setFuncStatus(1);
        update.setUpdateTime(new Date());
        update.setId(source.getId());
        funcPoMapper.updateByPrimaryKeySelective(update);
        productFuncCache.remove(source.getProductCode(), source.getFuncType());
    }

    /**
     *
     * 模版的名字和索引的前缀一致，索引前缀: bcup/k0amzovaultxu2q4/prop
     */
    private void createIndexTemplate(String productCode, ProductFuncTypeEnum funcTypeEnum, ProductFuncPo po) {

        Map<String, PropertiesItem> propertiesMap = new HashMap<>();
        /** 属性/事件上报 服务调用 */
        String tempName = EsUtil.buildDeviceTemplateName(productCode, funcTypeEnum, po.getIdentifier());
        String uploadPattern = EsUtil.buildIndexPatterns(productCode, funcTypeEnum, po.getIdentifier());
        List<String> uploadPatterns = new ArrayList<>();
        uploadPatterns.add(uploadPattern);
        String aliases = EsUtil.buildIndexAliases(productCode, funcTypeEnum, po.getIdentifier());
        /** 属性设置 */
        String setTempName = EsUtil.buildSetPropertyTemplateName(productCode, funcTypeEnum, po.getIdentifier());
        String setPattern = EsUtil.buildPropSetIndexPatterns(productCode, funcTypeEnum, po.getIdentifier());
        List<String> setPatterns = new ArrayList<>();
        setPatterns.add(setPattern);
        String setAliases = EsUtil.buildPropSetIndexAliases(productCode, funcTypeEnum, po.getIdentifier());

        BcMetaType bcMetaType = BcMetaType.explain(po.getDataType());
        /** dataType是空 只能是服务调用且没有参数的情况，这种情况默认是字符串 */
        BcEsMetaType esMetaType = null == bcMetaType ? BcEsMetaType.STRING : bcMetaType.getEsMetaType();
        for (Field field : EsMessage.class.getDeclaredFields()) {
            PropertiesItem item = new PropertiesItem();
            MetaDesc metaDesc = field.getAnnotation(MetaDesc.class);
            if (!metaDesc.type().equals(BcEsMetaType.UNKNOWN)) {
                /** 是消息头 */
                item.setType(metaDesc.type().getType());
                propertiesMap.put(field.getName(), item);
            } else if ("request".equals(field.getName())) {
                item.setType(esMetaType.getType());
                propertiesMap.put(field.getName(), item);
            } else if ("response".equals(field.getName())) {
                item.setType(BcEsMetaType.OBJECT.getType());
                propertiesMap.put(field.getName(), item);
            }
        }

        dataCenterService.createIndexTemplate(tempName, aliases, BCConstants.ES.HEADER_DEVICE, uploadPatterns,
            propertiesMap);
        if (ProductFuncTypeEnum.PROP.equals(funcTypeEnum)) {
            /** 是属性的话，因为属性会面临设置，所以单独再给他设置个模版 */
            dataCenterService.createIndexTemplate(setTempName, setAliases, BCConstants.ES.HEADER_DEVICE, setPatterns,
                propertiesMap);
        }

    }
}
