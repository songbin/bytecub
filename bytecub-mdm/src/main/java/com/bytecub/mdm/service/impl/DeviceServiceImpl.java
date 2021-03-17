package com.bytecub.mdm.service.impl;

import com.bytecub.common.biz.RedisKeyUtil;
import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.bo.BaseAttrItemBo;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.device.DevCreateReqDto;
import com.bytecub.common.domain.dto.request.DevQueryReqDto;
import com.bytecub.common.domain.dto.request.ProductFuncItemResDto;
import com.bytecub.common.domain.dto.request.device.DeviceUpdateReqDto;
import com.bytecub.common.domain.dto.request.device.GatewayMapReqDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.dto.response.device.DeviceRtResDto;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import com.bytecub.common.domain.storage.EsMessage;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.enums.BatchOpEnum;
import com.bytecub.common.enums.DevTypeEnum;
import com.bytecub.common.enums.DeviceStateEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.metadata.BcMetaType;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.mdm.cache.IDeviceCache;
import com.bytecub.mdm.cache.IDeviceOfflineCache;
import com.bytecub.mdm.dao.dal.DevicePoMapper;
import com.bytecub.mdm.dao.po.DevicePo;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.plugin.redis.CacheTemplate;
import com.bytecub.storage.IDataCenterService;
import com.bytecub.utils.JSONProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: DeviceServiceImpl.java, v 0.1 2020-12-22  Exp $$  
 */
@Service
@Slf4j
public class DeviceServiceImpl implements IDeviceService {
    @Autowired
    DevicePoMapper devicePoMapper;
    @Autowired
    IProductFuncService productFuncService;
    @Autowired
    IDataCenterService dataCenterService;
    @Autowired
    CacheTemplate cacheTemplate;
    @Autowired
    IDeviceCache deviceCache;
    @Autowired
    IDeviceOfflineCache deviceOfflineCache;

    @Override public int countByType(int type) {
        DevicePo devicePo = new DevicePo();
        devicePo.setDelFlag(0);
        if(2 == type){
            devicePo.setActiveStatus(1);
        }
        if(3 == type){
            devicePo.setEnableStatus(1);
        }
        return devicePoMapper.selectCount(devicePo);
    }

    @Override
    public void batchInsert(List<DevCreateReqDto> list) {
        try {
            devicePoMapper.batchInsert(list);
        } catch (DataIntegrityViolationException e) {
            throw new BCGException(BCErrorEnum.CODE_UNIQUE_ERROR, "", e);
        }

    }

    @Override
    public void activeDevice(DeviceActiveMqBo activeMqBo) {
        deviceOfflineCache.cacheWriter(activeMqBo);
    }

    @Override
    public void update(DeviceUpdateReqDto reqDto) {
        DevicePo query = devicePoMapper.selectByPrimaryKey(reqDto.getId());
        if (null == query) {
            return;
        }
        DevicePo po = new DevicePo();
        po.setProductCode(reqDto.getProductCode());
        po.setGwDevCode(reqDto.getGwDevCode());
        po.setDeviceName(reqDto.getDeviceName());
        po.setId(reqDto.getId());
        String key = RedisKeyUtil.buildDeviceInfoKey(query.getDeviceCode());
        cacheTemplate.remove(key);
        devicePoMapper.updateByPrimaryKeySelective(po);
    }

    @Override
    public void create(DevCreateReqDto reqDto) {
        try {
            DevicePo po = new DevicePo();
            po.setDeviceCode(reqDto.getDeviceCode());
            po.setDeviceName(reqDto.getDeviceName());
            po.setDeviceSecret(reqDto.getDeviceSecret());
            po.setGwDevCode(reqDto.getGwDevCode());
            po.setProductCode(reqDto.getProductCode());
            devicePoMapper.insertSelective(po);
        } catch (DataIntegrityViolationException e) {
            throw new BCGException(BCErrorEnum.CODE_UNIQUE_ERROR, reqDto.getDeviceCode(), e);
        }

    }

    @Override
    public DevicePageResDto queryByDevCode(String devCode) {
        DevicePageResDto resDto = deviceCache.deviceReader(devCode);
        if (null == resDto) {
            resDto = devicePoMapper.selectByCode(devCode);
            if(null != resDto){
                deviceCache.deviceWriter(devCode, resDto);
            }
        }
        return resDto;
    }

    /** 如果属性在数据库已经移除，则在缓存中也移除 */
    private void removeUnuse(String redisKey, List<ProductFuncItemResDto> propList, Map<String, String> cacheMap) {
        if (!CollectionUtils.isEmpty(cacheMap) || !CollectionUtils.isEmpty(propList)) {
            return;
        }
        cacheMap.forEach((key, value) -> {
            Boolean isExists = false;
            for (ProductFuncItemResDto item : propList) {
                if (item.getIdentifier().equals(key)) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                cacheTemplate.removeHashMap(redisKey, key);
            }
        });
    }

    @Override
    public void mapGateway(GatewayMapReqDto dto) {
        devicePoMapper.mapGateway(dto.getDevices(), dto.getGwDeviceCode());
        for(String deviceCode: dto.getDevices()){
            deviceCache.remove(deviceCode);
        }
    }

    @Override
    public long countByGwDevice(String gwDevCode, DeviceStateEnum stateEnum) {
        DevicePo query = new DevicePo();
        query.setGwDevCode(gwDevCode);
        if(DeviceStateEnum.ACTIVE.equals(stateEnum)){
            query.setActiveStatus(1);
        }
        return devicePoMapper.selectCount(query);
    }

    @Override
    public List<DeviceRtResDto> queryRtByDevCode(String deviceCode, String productCode,
        ProductFuncTypeEnum funcTypeEnum) {
        String redisKey = RedisKeyUtil.buildRtCacheKey(deviceCode, funcTypeEnum);
        List<DeviceRtResDto> resultFinal = new ArrayList<>();
        Map<String, String> map = cacheTemplate.getHashMapAll(redisKey);
        List<ProductFuncItemResDto> propList = productFuncService.ListFuncByProductCode(productCode, 1, funcTypeEnum);
        this.removeUnuse(redisKey, propList, map);
        map = cacheTemplate.getHashMapAll(redisKey);
        // 只会把identifier存在的数据返回给前端
        for (ProductFuncItemResDto prop : propList) {
            DeviceRtResDto item = new DeviceRtResDto();
            item.setIdentifier(prop.getIdentifier());
            item.setPropName(prop.getPropName());
            String jsonStr = map.get(prop.getIdentifier());
            item.setDataType(prop.getDataType());
            if (StringUtils.isEmpty(jsonStr)) {
                item.setValue("/");
                item.setArrivedTime("/");
            } else {
                EsMessage bo = JSONProvider.parseObject(jsonStr, EsMessage.class);
                item.setValue(bo.getRequest());
                item.setArrivedTime(bo.getTimestamp());
                BaseAttrItemBo attrItemBo = JSONProvider.parseObjectDefValue(prop.getAttr(), BaseAttrItemBo.class);
                if (!StringUtils.isEmpty(attrItemBo.getUnit())) {
                    item.setUnit(attrItemBo.getUnit());
                }
                if (BcMetaType.BOOLEAN.getType().equals(attrItemBo.getDataType())) {
                    if ((Boolean)bo.getRequest()) {
                        item.setValue(attrItemBo.getBool1());
                    } else {
                        item.setValue(attrItemBo.getBool0());
                    }
                }

            }
            resultFinal.add(item);
        }
        return resultFinal;
    }

    @Override
    public void batchChangeStatus(List<Long> ids, BatchOpEnum batchOpEnum) {

        switch (batchOpEnum) {
            case DELETE:
                devicePoMapper.batchDeleteById(ids);
                break;
            case ENABLE:
                devicePoMapper.batchEnableById(ids);
                break;
            case DISABLE:
                devicePoMapper.batchDisableById(ids);
                break;
            default:
                break;
        }
        List<DevicePageResDto> list = devicePoMapper.selectByIds(ids);
        for(DevicePageResDto item:list){
            deviceCache.remove(item.getDeviceCode());
        }
    }

    @Override
    public void batchChangeStatusByCode(List<String> devices, BatchOpEnum batchOpEnum) {
        if(CollectionUtils.isEmpty(devices)){
            return;
        }
        if(BatchOpEnum.OFFLINE.equals(batchOpEnum)){
            devicePoMapper.batchOfflineByCode(devices);
        }else if(BatchOpEnum.ONLINE.equals(batchOpEnum)){
            devicePoMapper.batchOnlineByCode(devices);
        }
        for(String deviceCode: devices){
            deviceCache.remove(deviceCode);
        }
    }

    @Override
    public PageResDto<DevicePageResDto> queryByPage(PageReqDto<DevQueryReqDto> pageReqDto) {
        int startId = (pageReqDto.getPageNo() - 1) * pageReqDto.getLimit();
        DevQueryReqDto item = pageReqDto.getParamData();
        String deviceName = StringUtils.isEmpty(item.getDeviceName()) ? null : item.getDeviceName();
        String gwDevCode = item.getGwDevCode();
        if(!item.getSubDevQuery()){
            /**是查询网关子设备的话，那就传什么是什么*/
           gwDevCode = StringUtils.isEmpty(item.getGwDevCode()) ? null : item.getGwDevCode();
        }

        String deviceCode = StringUtils.isEmpty(item.getDeviceCode()) ? null : item.getDeviceCode();
        String productCode = StringUtils.isEmpty(item.getProductCode()) ? null : item.getProductCode();
        String nodeType = StringUtils.isEmpty(item.getNodeType()) ? null : item.getNodeType();
        DevQueryReqDto query = new DevQueryReqDto();
        query.setDeviceCode(deviceCode);
        query.setDeviceName(deviceName);
        query.setGwDevCode(gwDevCode);
        query.setProductCode(productCode);
        query.setNodeType(nodeType);
        query.setEnableStatus(item.getEnableStatus());
        //query.setActiveStatus(item.getActiveStatus());
        List<DevicePageResDto> list = devicePoMapper.queryByPage(query, startId, pageReqDto.getLimit());
        Long total = devicePoMapper.countByPage(query);
        this.processActiveStatus(list);
        PageResDto<DevicePageResDto> resultPage =
            PageResDto.genResult(pageReqDto.getPageNo(), pageReqDto.getLimit(), total, list, null);
        return resultPage;
    }
    /**
     * 分页查询在线状态
     * */
    private void processActiveStatus(List<DevicePageResDto> list){
        if(CollectionUtils.isEmpty(list)){
            return;
        }

        List<String> listDevices = new ArrayList<>();
        for(DevicePageResDto item:list){
            listDevices.add(item.getDeviceCode());
        }
        Map<String, DeviceActiveMqBo> map = deviceOfflineCache.cacheMapReader(listDevices);
        for(DevicePageResDto item:list){
            DeviceActiveMqBo deviceActiveMqBo = map.get(item.getDeviceCode());
            if(null == deviceActiveMqBo){
                continue;
            }
            item.setLastOnlineTime(deviceActiveMqBo.getTimestamp());
            item.setActiveStatus(deviceActiveMqBo.getActive() ? 1 : 0);
        }
    }
}
