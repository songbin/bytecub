package com.bytecub.common.annotations;

import java.lang.annotation.*;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BcProtocolAnnotation.java, v 0.1 2020-12-12  Exp $$
  */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BcProtocolAnnotation {
    String protocolCode() default "";
    String desc() default "";
    String name() default "";
}

