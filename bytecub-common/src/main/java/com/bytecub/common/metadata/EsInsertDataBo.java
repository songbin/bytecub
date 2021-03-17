package com.bytecub.common.metadata;

import com.bytecub.common.domain.storage.EsMessage;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: EsInSertDataBo.java, v 0.1 2020-12-12  Exp $$
  */
@Data
public class EsInsertDataBo {
    String          indexName;
    String identifier;
    /**协议es的数据*/
    EsMessage esMessage;
}
