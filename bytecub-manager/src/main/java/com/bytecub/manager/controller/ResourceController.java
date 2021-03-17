package com.bytecub.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.DataResult;
import com.bytecub.storage.es.MessageReplyRepository;
import com.bytecub.storage.entity.MessageReplyEntity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2020 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: ResourceController.java, v 0.1 2020-12-10  Exp $$  
 */
@Slf4j
@RestController
@RequestMapping(BCConstants.URL_PREFIX.MGR + "resource")
@Api(tags = "资源管理")
public class ResourceController {
    @Autowired MessageReplyRepository messageReplyService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ApiOperation(value = "创建数据", httpMethod = "POST", response = DataResult.class, notes = "创建数据")
    public DataResult create(@RequestBody MessageReplyEntity messageReplyEntity) {
        messageReplyService.save(messageReplyEntity);
        return DataResult.ok();
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ApiOperation(value = "查询数据", httpMethod = "POST", response = DataResult.class, notes = "查询数据")
    public DataResult<Page> list( ) {
        Sort sort =  Sort.by(Sort.Direction.DESC, "timestamp");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<MessageReplyEntity> list = messageReplyService.findAll(pageable);
        return DataResult.ok(list);
    }
}
