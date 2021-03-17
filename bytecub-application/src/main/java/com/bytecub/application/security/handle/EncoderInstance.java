package com.bytecub.application.security.handle;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *  * ByteCub.cn.  * Copyright (c) 2020-2021 All Rights Reserved.  *   * @author bytecub@163.com songbin
 *  * @version Id: EncoderInstance.java, v 0.1 2021-02-23  Exp $$  
 */
public class EncoderInstance implements PasswordEncoder {
    private static EncoderInstance instance = new EncoderInstance();

    public static EncoderInstance getInstance() {
        return instance;
    }

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        if(null == s){
            return false;
        }
        return s.equals(charSequence);
    }
}
