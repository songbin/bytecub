package com.bytecub.mdm.service;

import com.bytecub.common.domain.bo.ProtocolBo;
import com.bytecub.mdm.dao.po.ProductPo;
import com.bytecub.mdm.dao.po.ProtocolPo;

import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 协议管理相关
  * @author bytecub@163.com  songbin
  * @version Id: IProtocolService.java, v 0.1 2020-12-05  Exp $$
  */
public interface IProtocolService {
    List<ProtocolBo> listEnableAll();
    List<ProtocolBo> listByCondition(ProtocolPo protocolPo);
}
