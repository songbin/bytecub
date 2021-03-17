package com.bytecub.mdm.service.impl;

import com.bytecub.common.domain.bo.ProtocolBo;
import com.bytecub.mdm.convert.po2bo.ProtocolConvert;
import com.bytecub.mdm.dao.dal.ProtocolPoMapper;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.dao.po.ProtocolPo;
import com.bytecub.mdm.service.IProtocolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProtocolServiceImpl.java, v 0.1 2020-12-05  Exp $$
  */
@Service
@Slf4j
public class ProtocolServiceImpl implements IProtocolService {
    @Autowired
    ProtocolPoMapper protocolPoMapper;
    /**
     * 列出所有可用的协议
     * */
    @Override
    public List<ProtocolBo> listEnableAll() {
        List<ProtocolPo> list = protocolPoMapper.listAll(1, 0);
        List<ProtocolBo> result = ProtocolConvert.list(list);
        return result;
    }

    @Override
    public List<ProtocolBo> listByCondition(ProtocolPo protocolPo) {
        if(null == protocolPo){
            protocolPo = new ProtocolPo();
        }

        List<ProtocolPo> list = protocolPoMapper.queryByUnion(protocolPo);
        List<ProtocolBo> result = ProtocolConvert.list(list);
        return result;
    }
}
