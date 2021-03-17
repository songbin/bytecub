package com.bytecub.manager.service.impl;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.bo.PropAttrBo;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.DevBatchAddReqDto;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.request.device.DevCreateReqDto;
import com.bytecub.common.domain.dto.request.device.DeviceImportReqDto;
import com.bytecub.common.domain.dto.request.device.DeviceRtItemReqDto;
import com.bytecub.common.domain.dto.response.device.DeviceRtHistoryResDto;
import com.bytecub.common.domain.storage.EsMessage;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.manager.service.IAdminDeviceService;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.plugin.es.config.enums.KeyMatchTypeEnum;
import com.bytecub.plugin.es.domain.EsRange;
import com.bytecub.plugin.es.domain.SearchItem;
import com.bytecub.storage.IDataCenterService;
import com.bytecub.utils.JSONProvider;
import com.bytecub.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: AdminDeviceServiceImpl.java, v 0.1 2020-12-28  Exp $$  
 */
@Slf4j
@Service
public class AdminDeviceServiceImpl implements IAdminDeviceService {
    @Autowired
    IDataCenterService dataCenterService;
    @Autowired
    IProductFuncService productFuncService;
    @Autowired
    IDeviceService deviceService;

    @Override
    public void create(DevCreateReqDto reqDto) {
        String devCode = StringUtils.isEmpty(reqDto.getDeviceCode()) ? this.buildDevCode() : reqDto.getDeviceCode();
        reqDto.setDeviceCode(devCode);
        deviceService.create(reqDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(List<DeviceImportReqDto> reqImportDto) {
        if (CollectionUtils.isEmpty(reqImportDto)) {
            return;
        }
        List<DevCreateReqDto> list = new ArrayList<>();
        for (DeviceImportReqDto value : reqImportDto) {
            DevCreateReqDto item = new DevCreateReqDto();
            item.setDeviceSecret(value.getDeviceSecret());
            item.setDeviceCode(value.getDeviceCode());
            item.setGwDevCode("");
            item.setProductCode(value.getProductCode());
            item.setDeviceName(value.getDeviceName());
            list.add(item);
        }
        deviceService.batchInsert(list);
    }

    @Override
    public void batchCreate(DevBatchAddReqDto reqDto) {
        List<DevCreateReqDto> list = new ArrayList<>();
        for (int i = 0; i < reqDto.getNumber(); i++) {
            DevCreateReqDto item = new DevCreateReqDto();
            item.setDeviceSecret(this.buildDevSecret());
            item.setDeviceCode(this.buildDevCode());
            item.setGwDevCode(reqDto.getGwDevCode());
            item.setProductCode(reqDto.getProductCode());
            item.setDeviceName(reqDto.getDeviceName());
            list.add(item);
        }
        deviceService.batchInsert(list);
    }

    @Override
    public PageResDto<DeviceRtHistoryResDto> searchSetItem(PageReqDto<DeviceRtItemReqDto> searchPage) {
        DeviceRtItemReqDto params = searchPage.getParamData();
        List<EsRange> listRange = new ArrayList<>();
        List<SearchItem> searchItemList = new ArrayList<>();
        this.buildParam(params.getDeviceCode(), listRange, searchItemList, params);
        ProductFuncTypeEnum typeEnum = ProductFuncTypeEnum.explain(params.getFuncType());
        int startId = (searchPage.getPageNo() - 1) * searchPage.getLimit();
        List<DeviceRtHistoryResDto> resultPage = dataCenterService.searchDeviceSetList(params.getProductCode(), params.getDeviceCode(),
                params.getIdentifier(), typeEnum, searchItemList, startId, searchPage.getLimit(), listRange);

        long total = 0;
        if (!CollectionUtils.isEmpty(resultPage)) {
            total = dataCenterService.countDeviceSet(params.getProductCode(), params.getDeviceCode(), typeEnum,
                    params.getIdentifier(), searchItemList, listRange);
            this.processEsRtData(resultPage, params);
        }
        PageResDto<DeviceRtHistoryResDto> pageResult =
                PageResDto.genResult(searchPage.getPageNo(), searchPage.getLimit(), total, resultPage, null);
        return pageResult;

    }

    @Override
    public PageResDto<DeviceRtHistoryResDto> searchRtItem(PageReqDto<DeviceRtItemReqDto> searchPage) {
        DeviceRtItemReqDto params = searchPage.getParamData();
        List<EsRange> listRange = new ArrayList<>();
        List<SearchItem> searchItemList = new ArrayList<>();
        this.buildParam(params.getDeviceCode(), listRange, searchItemList, params);
        ProductFuncTypeEnum typeEnum = ProductFuncTypeEnum.explain(params.getFuncType());
        int startId = (searchPage.getPageNo() - 1) * searchPage.getLimit();
        List<DeviceRtHistoryResDto> resultPage = dataCenterService.searchDeviceRuntimeList(params.getProductCode(), params.getDeviceCode(),
            params.getIdentifier(), typeEnum, searchItemList, startId, searchPage.getLimit(), listRange);

        long total = 0;
        if (!CollectionUtils.isEmpty(resultPage)) {
            total = dataCenterService.countDeviceRuntime(params.getProductCode(), params.getDeviceCode(), typeEnum,
                params.getIdentifier(), searchItemList, listRange);
            this.processEsRtData(resultPage, params);
        }
        PageResDto<DeviceRtHistoryResDto> pageResult =
            PageResDto.genResult(searchPage.getPageNo(), searchPage.getLimit(), total, resultPage, null);
        return pageResult;
    }

    private void buildParam(String deviceCode, List<EsRange> rangeList, List<SearchItem> searchItemList, DeviceRtItemReqDto params) {
        EsRange range = new EsRange();
        range.setField(BCConstants.ES.HEADER_TIMESTAMP);
        if (params.getEndDate() instanceof Date || params.getStartDate() instanceof Date) {
            Long max = null == params.getEndDate() ? null : params.getEndDate().getTime();
            range.setMin(max);
            Long min = null == params.getStartDate() ? null : params.getStartDate().getTime();
            range.setMin(min);
        } else {
            range.setMax(params.getEndDate());
            range.setMin(params.getStartDate());
        }
        rangeList.add(range);

         SearchItem searchItem = new SearchItem();
         searchItem.setKey(BCConstants.ES.HEADER_DEVICE);
         searchItem.setValue(deviceCode);
         searchItem.setMatchType(KeyMatchTypeEnum.EXIST);
         searchItemList.add(searchItem);
    }

    /**
     * 对参数是boolean类型的返回值做处理
     * 因为boolean类型对true和false都有定义
     * */
    private Object processBoolean(Object value, DeviceRtItemReqDto params) {
        ProductFuncTypeEnum productFuncTypeEnum = ProductFuncTypeEnum.explain(params.getFuncType());
        ProductFuncItemResDto resDto =
            productFuncService.queryFunc(params.getProductCode(), productFuncTypeEnum, params.getIdentifier());
        PropAttrBo attrBo = JSONProvider.map2Object(resDto.getAttrMap(), PropAttrBo.class);
        if ((Boolean)value) {
            if (!StringUtils.isEmpty(attrBo.getBool1())) {
                value = attrBo.getBool1();
            }

        } else {
            if (!StringUtils.isEmpty(attrBo.getBool0())) {
                value = attrBo.getBool0();
            }
        }

        return value;
    }
    /**
     * 把es查询出来的原始数据根据attr等一些属性转化成最终结果
     */
    private void processEsData(List<EsMessage> resultPage, DeviceRtItemReqDto params) {
        for (EsMessage esMessage : resultPage) {
            Object value = esMessage.getRequest();
            if (null != value) {
                if (BcMetaType.BOOLEAN.getCode().equals(params.getDataType())) {
                    value = this.processBoolean(value, params);
                }
                esMessage.setRequest(value);
            }
        }
    }

    /**
     * 下行指令返回值处理
     */
    private void processEsRtData(List<DeviceRtHistoryResDto> resultPage, DeviceRtItemReqDto params) {
        for (DeviceRtHistoryResDto esMessage : resultPage) {
            Object value = esMessage.getRequest();
            if (null != value) {
                if (BcMetaType.BOOLEAN.getCode().equals(params.getDataType())) {
                    value = this.processBoolean(value, params);
                }
                esMessage.setRequest(value);
            }
        }
    }

    final private String buildDevCode() {
        return RandomUtil.randomString(16);
    }

    final private String buildDevSecret() {
        return RandomUtil.randomString(20);
    }
}
