package com.bytecub.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: EchoController.java, v 0.1 2020-12-02  Exp $$
  */

@Slf4j
@RestController
public class EchoController {

    @RequestMapping(value = "/echo", method = RequestMethod.POST)
    public String echo( @RequestBody String data) {
        return data;
    }
}
