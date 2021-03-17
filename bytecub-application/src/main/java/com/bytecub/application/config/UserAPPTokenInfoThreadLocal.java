package com.bytecub.application.config;

import com.bytecub.mdm.dao.po.UserPo;

public class UserAPPTokenInfoThreadLocal {
    private static ThreadLocal<UserPo> currentTokenHandler = new ThreadLocal<UserPo>();

    public static UserPo getCurrentToken() {
         return currentTokenHandler.get();
    }
    public static void setCurrentToken(UserPo token) {
         currentTokenHandler.set(token);
    }

    public static void cleanCurrentToken(){
        currentTokenHandler.remove();
    }


}
