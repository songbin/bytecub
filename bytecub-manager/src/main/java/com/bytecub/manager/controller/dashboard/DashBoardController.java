package com.bytecub.manager.controller.dashboard;

import com.bytecub.common.domain.dto.response.dashboard.DashBoardResDto;
import com.bytecub.gateway.mq.producer.DeviceUpMessageProducer;
import com.bytecub.gateway.mq.storage.ActiveMQStorage;
import com.bytecub.gateway.mq.storage.DeviceUpMessageStorage;
import com.bytecub.gateway.mq.storage.PropertySetStorage;
import com.bytecub.gateway.mq.storage.ServiceInvokeStorage;
import com.bytecub.mdm.service.IDashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2021 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: DashBoardController.java, v 0.1 2021-01-27  Exp $$
  */
@Slf4j
@RestController
@RequestMapping(BCConstants.URL_PREFIX.MGR + "dashboard")
public class DashBoardController {
    @Autowired
    IDashBoardService dashBoardService;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    @ApiOperation(value = "面板首页", httpMethod = "GET", response = DataResult.class, notes = "面板首页")
    public DataResult<DashBoardResDto> index() {
        DashBoardResDto resDto = dashBoardService.index();
        return DataResult.ok(resDto);
    }

    @RequestMapping(value = "queue", method = RequestMethod.GET)
    @ApiOperation(value = "队列情况", httpMethod = "GET", response = DataResult.class, notes = "队列情况")
    public DataResult queue() {
        int size1 = ServiceInvokeStorage.size();
        int size2 = ActiveMQStorage.size();
        int size3 = DeviceUpMessageStorage.size();
        int size4 = PropertySetStorage.size();
        Map<String,Integer> map = new HashMap<>();
        map.put("服务下发", size1);
        map.put("设备激活", size2);
        map.put("上报消息", size3);
        map.put("属性设置", size4);
        return DataResult.ok(map);
    }
}
