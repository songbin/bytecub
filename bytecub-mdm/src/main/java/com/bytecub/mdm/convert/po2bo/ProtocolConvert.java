package com.bytecub.mdm.convert.po2bo;

import com.bytecub.common.domain.bo.ProtocolBo;
import com.bytecub.mdm.dao.po.ProtocolPo;
import com.bytecub.utils.ObjectCopyUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: ProtocolConvert.java, v 0.1 2020-12-05  Exp $$
  */
public class ProtocolConvert {
    public static ProtocolBo item(ProtocolPo protocolPo){
        ProtocolBo protocolBo = new ProtocolBo();
        ObjectCopyUtil.copyProperties(protocolPo, protocolBo);
        return protocolBo;
    }

    public static List<ProtocolBo> list(List<ProtocolPo> list){
        List<ProtocolBo> result = new ArrayList<>();
        if(CollectionUtils.isEmpty(list)){
            return result;
        }

        for(ProtocolPo item:list){
            ProtocolBo itemBo = item(item);
            result.add(itemBo);
        }
        return result;
    }
}
