package com.bytecub.protocol.service;

import com.bytecub.common.annotations.BcProtocolAnnotation;
import com.bytecub.common.domain.dto.response.ProtocolItemResDto;
import com.bytecub.protocol.base.IBaseProtocol;
import com.bytecub.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProtocolUtilService.java, v 0.1 2020-12-29  Exp $$
  */
public interface IProtocolUtilService {
    /**
     * 列出所有协议
     * @return
     * */
    List<ProtocolItemResDto> listProtocol();
    /**
     * 根据协议编码查询协议实例
     * @param protocolCode
     * @return
     * */
    IBaseProtocol queryProtocolInstanceByCode(String protocolCode);
}
