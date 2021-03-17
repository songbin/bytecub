package com.bytecub.manager.controller.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.bytecub.common.domain.dto.response.device.GwDevicePageResDto;
import com.bytecub.common.enums.DeviceStateEnum;
import com.bytecub.common.exception.BCGException;
import com.bytecub.common.domain.gateway.direct.response.PropertyGetResponse;
import com.bytecub.gateway.direct.service.IPropGetService;
import com.bytecub.gateway.mq.mqttclient.BcPubMqttClient;
import com.bytecub.gateway.mq.redis.publish.PropertySetPublisher;
import com.bytecub.gateway.mq.redis.publish.ServiceInvokePublisher;
import com.bytecub.mqtt.service.biz.MqttRemoteService;
import com.bytecub.storage.IMessageReplyService;
import com.bytecub.storage.entity.MessageReplyEntity;
import com.bytecub.utils.IdGenerate;
import com.bytecub.utils.ObjectCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.DevBatchAddReqDto;
import com.bytecub.common.domain.dto.request.DevQueryReqDto;
import com.bytecub.common.domain.dto.request.device.*;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.dto.response.device.DeviceRtHistoryResDto;
import com.bytecub.common.domain.dto.response.device.DeviceRtResDto;
import com.bytecub.common.enums.BCErrorEnum;
import com.bytecub.common.enums.BatchOpEnum;
import com.bytecub.common.metadata.ProductFuncTypeEnum;
import com.bytecub.common.domain.gateway.mq.PropertySetMessageBo;
import com.bytecub.common.domain.gateway.mq.ServiceInvokeMessageBo;
import com.bytecub.manager.service.IAdminDeviceService;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mdm.service.IProductFuncService;
import com.bytecub.mdm.service.IProductService;
import com.bytecub.storage.IDataCenterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: DeviceController.java, v 0.1 2020-12-22  Exp $$  
 */
@Slf4j
@RestController
@RequestMapping(BCConstants.URL_PREFIX.MGR + "device")
@Api(tags = "设备管理")
public class DeviceController {
    @Autowired
    IDeviceService deviceService;
    @Autowired
    IProductFuncService productFuncService;
    @Autowired
    IDataCenterService dataCenterService;
    @Autowired
    IAdminDeviceService adminDeviceService;
    @Autowired
    IProductService productService;
    @Autowired
    BcPubMqttClient bcPubMqttClient;
    @Autowired
    IPropGetService propGetService;
    @Autowired
    IMessageReplyService         messageReplyService;

    @RequestMapping(value = "batchadd", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:device:batchadd')")
    @ApiOperation(value = "批量添加设备", httpMethod = "POST", response = DataResult.class, notes = "批量添加设备")
    public DataResult batchAdd(@RequestBody DevBatchAddReqDto reqDto) {
        adminDeviceService.batchCreate(reqDto);
        return DataResult.ok();
    }

    @RequestMapping(value = "import/file", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:device:import')")
    @ApiOperation(value = "导入设备", httpMethod = "POST", response = DataResult.class, notes = "导入设备")
    public DataResult importFile(@RequestBody List<DeviceImportReqDto> reqDtoList) {
        adminDeviceService.importExcel(reqDtoList);
        return DataResult.ok();
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:device:add')")
    @ApiOperation(value = "添加设备", httpMethod = "POST", response = DataResult.class, notes = "添加设备")
    public DataResult create(@RequestBody DevCreateReqDto reqDto) {
        deviceService.create(reqDto);
        return DataResult.ok();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:device:update')")
    @ApiOperation(value = "更新设备", httpMethod = "POST", response = DataResult.class, notes = "更新设备")
    public DataResult update(@RequestBody DeviceUpdateReqDto reqDto) {
        deviceService.update(reqDto);
        return DataResult.ok();
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询设备列表", httpMethod = "POST", response = DataResult.class, notes = "分页查询设备列表")
    public DataResult<PageResDto<DevicePageResDto>> search(@RequestBody PageReqDto<DevQueryReqDto> searchPage) {
        PageResDto<DevicePageResDto> resDto = deviceService.queryByPage(searchPage);
        return DataResult.ok(resDto);
    }

    @RequestMapping(value = "gw/sub/dev", method = RequestMethod.POST)
    @ApiOperation(value = "网关设备列表", httpMethod = "POST", response = DataResult.class, notes = "网关设备列表")
    public DataResult<PageResDto<GwDevicePageResDto>> gwSubDevice(@RequestBody PageReqDto<DevQueryReqDto> searchPage) {
        PageResDto<DevicePageResDto> resDto = deviceService.queryByPage(searchPage);
        PageResDto<GwDevicePageResDto> result = new PageResDto<>();
        List<GwDevicePageResDto> list = new ArrayList<>();
        for(DevicePageResDto device: resDto.getResultData()){
            GwDevicePageResDto item = new GwDevicePageResDto();
            long total = deviceService.countByGwDevice(device.getDeviceCode(), DeviceStateEnum.TOTAL);
            long active = deviceService.countByGwDevice(device.getDeviceCode(), DeviceStateEnum.ACTIVE);
            ObjectCopyUtil.copyProperties(device, item);
            item.setDeviceTotal(total);
            item.setDeviceActive(active);
            list.add(item);
        }
        result.setResultData(list);
        return DataResult.ok(result);
    }

    @RequestMapping(value = "map", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:device:gw:map')")
    @ApiOperation(value = "子设备关联到网关设备", httpMethod = "POST", response = DataResult.class, notes = "子设备关联到网关设备")
    public DataResult map(@RequestBody GatewayMapReqDto dto) {
        if(!dto.getRemoveAction()){
            DevicePageResDto gwDevice =deviceService.queryByDevCode(dto.getGwDeviceCode());
            if(null == gwDevice){
                throw BCGException.genException(BCErrorEnum.RESOURCE_NOT_EXISTS, "网关设备不存在");
            }
        }else{
            dto.setGwDeviceCode("");
        }

        deviceService.mapGateway(dto);
        return DataResult.ok();
    }
    @RequestMapping(value = "status/change", method = RequestMethod.POST)
    @PreAuthorize("@ps.hasPermission('system:device:status')")
    @ApiOperation(value = "设备状态批量改变", httpMethod = "POST", response = DataResult.class, notes = "设备状态批量改变")
    public DataResult statusChange(@RequestBody BatchStatusReqDto reqDto) {
        if (CollectionUtils.isEmpty(reqDto.getList())) {
            return DataResult.fail("没有选中要改变的项");
        }
        deviceService.batchChangeStatus(reqDto.getList(), BatchOpEnum.explain(reqDto.getType()));
        return DataResult.ok();
    }

    @RequestMapping(value = "info", method = RequestMethod.GET)
    @ApiOperation(value = "设备详情查看", httpMethod = "GET", response = DataResult.class, notes = "设备详情查看")
    public DataResult info(String deviceCode) {
        if (StringUtils.isEmpty(deviceCode)) {
            return DataResult.fail(BCErrorEnum.INVALID_PARAM);
        }
        DevicePageResDto dto = deviceService.queryByDevCode(deviceCode);
        return DataResult.ok(dto);
    }

    @RequestMapping(value = "runtime", method = RequestMethod.GET)
    @ApiOperation(value = "设备运行状态", httpMethod = "GET", response = DataResult.class, notes = "设备运行状态")
    public DataResult<List<DeviceRtResDto>> runtime(String deviceCode, String productCode, String type) {
        if (StringUtils.isEmpty(deviceCode) || StringUtils.isEmpty(productCode)) {
            return DataResult.fail(BCErrorEnum.INVALID_PARAM);
        }
        ProductFuncTypeEnum productFuncTypeEnum = ProductFuncTypeEnum.explain(type);
        if (null == productFuncTypeEnum) {
            productFuncTypeEnum = ProductFuncTypeEnum.PROP;
        }
        List<DeviceRtResDto> result = deviceService.queryRtByDevCode(deviceCode, productCode, productFuncTypeEnum);
        return DataResult.ok(result);
    }

    @RequestMapping(value = "runtime/item", method = RequestMethod.POST)
    @ApiOperation(value = "查看设备具体属性运行详情列表", httpMethod = "POST", response = DataResult.class, notes = "查看设备具体属性运行详情列表")
    public DataResult<PageResDto<DeviceRtHistoryResDto>>
        rtItem(@RequestBody PageReqDto<DeviceRtItemReqDto> searchPage) {
        PageResDto<DeviceRtHistoryResDto> pageResult = adminDeviceService.searchRtItem(searchPage);
        return DataResult.ok(pageResult);
    }

    @RequestMapping(value = "set/item", method = RequestMethod.POST)
    @ApiOperation(value = "查看设备属性设置历史", httpMethod = "POST", response = DataResult.class, notes = "查看设备属性设置历史")
    public DataResult<PageResDto<DeviceRtHistoryResDto>>
        setItem(@RequestBody PageReqDto<DeviceRtItemReqDto> searchPage) {
        PageResDto<DeviceRtHistoryResDto> pageResult = adminDeviceService.searchSetItem(searchPage);
        return DataResult.ok(pageResult);
    }

    @RequestMapping(value = "service/invoke", method = RequestMethod.POST)
    @ApiOperation(value = "下发指令", httpMethod = "POST", response = DataResult.class, notes = "下发指令")
    public DataResult invoke(@Valid @RequestBody InvokeReqDto reqDto) {
        log.info("下发指令请求...");
        ServiceInvokeMessageBo bo = new ServiceInvokeMessageBo();
        String messageId = IdGenerate.genId();
        bo.setIdentifier(reqDto.getIdentifier());
        bo.setMessageId(messageId);
        bo.setDeviceCode(reqDto.getDeviceCode());
        bo.setProductCode(reqDto.getProductCode());
        bo.setCommand(reqDto.getCommand());
        ServiceInvokePublisher.send(bo);
        return DataResult.ok(messageId);
    }

    @RequestMapping(value = "message/get", method = RequestMethod.GET)
    @ApiOperation(value = "根据messageId查询回执", httpMethod = "GET", response = DataResult.class, notes = "根据messageId查询回执")
    public DataResult messageId(String deviceCode, String messageId) {
        if (StringUtils.isEmpty(messageId) || StringUtils.isEmpty(deviceCode)) {
            BCGException.genException(BCErrorEnum.INVALID_PARAM);
        }
        MessageReplyEntity messageReplyEntity = messageReplyService.queryByMessageId(deviceCode, messageId);
        return DataResult.ok(messageReplyEntity);
    }

    @RequestMapping(value = "property/set", method = RequestMethod.POST)
    @ApiOperation(value = "属性设置", httpMethod = "POST", response = DataResult.class, notes = "属性设置")
    public DataResult propertySet(@Valid @RequestBody PropertySetReqDto reqDto) {
        log.info("属性设置请求...");
        PropertySetMessageBo bo = new PropertySetMessageBo();
        bo.setIdentifier(reqDto.getIdentifier());
        bo.setDeviceCode(reqDto.getDeviceCode());
        DevicePageResDto devicePageResDto = deviceService.queryByDevCode(reqDto.getDeviceCode());
        bo.setProductCode(devicePageResDto.getProductCode());
        bo.setCommand(reqDto.getCommand());
        PropertySetPublisher.send(bo);
        return DataResult.ok();
    }

    @RequestMapping(value = "property/get", method = RequestMethod.POST)
    @ApiOperation(value = "属性读取", httpMethod = "POST", response = DataResult.class, notes = "属性读取")
    public DataResult propertyGet(@Valid @RequestBody PropertyGetReqDto reqDto) {
        log.info("属性读取请求...");
        Long start = System.currentTimeMillis();
        String deviceCode = reqDto.getDeviceCode();
        String identifier = reqDto.getIdentifier();
        Long timeout = reqDto.getTimeout();
        PropertyGetResponse propertyGetResponse = propGetService.fetchProperty(deviceCode, identifier, timeout);
        Object value = propertyGetResponse.getValue();
        Long dura = (System.currentTimeMillis() - start) / 1000;
        log.info("属性读取请求耗时:{}s", dura);
        return null == value ? DataResult.fail(BCErrorEnum.TIMEOUT) : DataResult.ok(propertyGetResponse.getValue());
    }

    @RequestMapping(value = "online", method = RequestMethod.POST)
    @ApiOperation(value = "在线设备列表", httpMethod = "POST", response = DataResult.class, notes = "在线设备列表")
    public DataResult deviceOnline() {
        List<String> list =  MqttRemoteService.onlineDevices();
        Map<String, Object> result = new HashMap<>();
        result.put("total", list.size());
        result.put("data", list);
        return DataResult.ok(result);
    }
}
