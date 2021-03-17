package com.bytecub.manager.service;

import com.bytecub.common.domain.dto.PageReqDto;
import com.bytecub.common.domain.dto.PageResDto;
import com.bytecub.common.domain.dto.request.DevBatchAddReqDto;
import com.bytecub.common.domain.dto.request.device.DevCreateReqDto;
import com.bytecub.common.domain.dto.request.device.DeviceImportReqDto;
import com.bytecub.common.domain.dto.request.device.DeviceRtItemReqDto;
import com.bytecub.common.domain.dto.response.device.DeviceRtHistoryResDto;
import com.bytecub.common.domain.storage.EsMessage;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: IAdminDeviceService.java, v 0.1 2020-12-28  Exp $$
  */
public interface IAdminDeviceService {
    /**
     * 搜索设备具体某个属性设置历史数据
     * @param searchPage PageReqDto
     * @return
     * */
    PageResDto<DeviceRtHistoryResDto> searchSetItem(PageReqDto<DeviceRtItemReqDto> searchPage);
    /**
     * 搜索设备具体某个属性上报历史数据
     * @param searchPage PageReqDto
     * @return
     * */
    PageResDto<DeviceRtHistoryResDto> searchRtItem(PageReqDto<DeviceRtItemReqDto> searchPage);
    /**
     * 创建设备
     * @param deviceCreateReqDto DevCreateReqDto
     * @return
     * */
    void create(DevCreateReqDto deviceCreateReqDto);
    /**
     * 批量创建设备
     * @param devBatchAddReqDto DevBatchAddReqDto
     * @return
     * */
    void batchCreate(DevBatchAddReqDto devBatchAddReqDto);
    /**
     * 导入设备
     * @param reqImportDto List
     * @return
     * */
    void importExcel(List<DeviceImportReqDto> reqImportDto);
}
