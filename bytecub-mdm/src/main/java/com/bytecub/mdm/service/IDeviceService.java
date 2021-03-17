package com.bytecub.mdm.service;

import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.device.DevCreateReqDto;
import com.bytecub.common.domain.dto.request.DevQueryReqDto;
import com.bytecub.common.domain.dto.request.device.DeviceUpdateReqDto;
import com.bytecub.common.domain.dto.request.device.GatewayMapReqDto;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.common.domain.dto.response.device.DeviceRtResDto;
import com.bytecub.common.domain.gateway.mq.DeviceActiveMqBo;
import com.bytecub.common.enums.BatchOpEnum;
import com.bytecub.common.enums.DeviceStateEnum;
import com.bytecub.common.metadata.ProductFuncTypeEnum;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IDeviceService.java, v 0.1 2020-12-22  Exp $$
  */
public interface IDeviceService {
    /**
     * 根据类型计算总数
     * @param type int 1:总数 2:在线设备 3:激活设备
     * @return
     * */
    int countByType(int type);
    /**
     * batchInsert
     * @param  list
     * */
    void batchInsert(List<DevCreateReqDto> list);
    /**
     * create
     * @param reqDto
     * */
    void create(DevCreateReqDto reqDto);
    /**
     * update
     * @param reqDto
     * */
    void update(DeviceUpdateReqDto reqDto);
    /**
     * 设备在线状态改变
     * @param deviceCode
     * @param active
     * @param host
     * @param port
     * @return
     * */
    void activeDevice(DeviceActiveMqBo activeMqBo);
    /**
     * 根据条件分页查询设备列表
     * @param  pageReqDto
     * @return
     * */
    PageResDto<DevicePageResDto> queryByPage(PageReqDto<DevQueryReqDto> pageReqDto);
    /**
     * 批量改变设备状态
     * @param ids
     * @param batchOpEnum
     * */
    void batchChangeStatus(List<Long> ids, BatchOpEnum batchOpEnum);
    /**
     * 根据设备编码批量改变设备状态
     * @param devices
     * @param batchOpEnum
     * */
    void batchChangeStatusByCode(List<String> devices, BatchOpEnum batchOpEnum);
    /**根据设备编码查询设备详情
     * @param devCode String
     * @return
     * */
    DevicePageResDto queryByDevCode(String devCode);
    /**
     * 根据设备编码查询设备实时运行状态
     * @param deviceCode
     * @param productCode
     * @param funcTypeEnum
     * @return
     * */
    List<DeviceRtResDto> queryRtByDevCode(String deviceCode, String productCode, ProductFuncTypeEnum funcTypeEnum);
    /**
     * 将网关子设备关联到网关设备
     * @param dto
     * */
    void mapGateway(GatewayMapReqDto dto);
    /**
     * 计算网关设备的子设备数量
     * @param gwDevCode
     * @param stateEnum
     * @return
     * */
    long countByGwDevice(String gwDevCode, DeviceStateEnum stateEnum);
}
