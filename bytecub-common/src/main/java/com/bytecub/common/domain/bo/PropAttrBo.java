package com.bytecub.common.domain.bo;

import com.bytecub.common.metadata.BcMetaUnit;
import lombok.Data;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: PropAttrBo.java, v 0.1 2020-12-17  Exp $$
  */
@Data
public class PropAttrBo {
    String dataType;
    String identifier;
    Long max;
    Long min;
    String unit;
    String unitName;
    String bool0;
    String bool1;
    Long length;
    Object data;
}
