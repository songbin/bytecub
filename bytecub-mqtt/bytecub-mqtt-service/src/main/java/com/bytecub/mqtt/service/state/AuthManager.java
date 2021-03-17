package com.bytecub.mqtt.service.state;

import com.bytecub.common.constants.BCConstants;
import com.bytecub.common.domain.dto.response.device.DevicePageResDto;
import com.bytecub.mdm.service.IDeviceService;
import com.bytecub.mqtt.domain.config.BrokerProperties;
import com.bytecub.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 授权管理
 * Created by songbin on 2020-11-25.
 * @Author
 */
@Service("authManager")
@Slf4j
public class AuthManager {
    @Autowired IDeviceService deviceService;
    @Autowired BrokerProperties brokerProperties;

    public Boolean checkValid( String clientId, String userName, String password){
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)){
            return false;
        }
        try{
            return this.verify(clientId, userName, password);
        }catch (Exception e){
            log.warn("[{}:{}]授权检验异常", userName, password, e);
            return false;
        }
    }
    private Boolean verify(String clientId, String userName, String password){
        String[] ret = this.parseUserName(userName);
        String devCode = ret[0];
        if(!this.verifyExpired(ret[1])){
            log.warn("MQTT签名过期:username =>{}  password=>{}", userName, password);
            return false;
        }
        DevicePageResDto resDto = deviceService.queryByDevCode(clientId);
        if(resDto.getEnableStatus() != 1){
            log.warn("{}设备未激活，不能连接", devCode);
            return false;
        }
        String srand = userName + BCConstants.AUTH.SIGN_SPLIT + resDto.getDeviceSecret();
        String sign = Md5Utils.md5(srand);
        if(sign.equals(password)){
            return true;
        }
        log.warn("MQTT密码错误:username =>{}  password=>{}", userName, password);
        return false;
    }
    private final String[] parseUserName(String userName){
        return userName.split("\\" + BCConstants.AUTH.SIGN_SPLIT);
    }
    private final Boolean verifyExpired(String timestamp){
        if(-1 == brokerProperties.getPasswordExpired()){
            return true;
        }
        Long signTime = Long.valueOf(timestamp);
        Long curr = System.currentTimeMillis();
        if((curr - signTime) > brokerProperties.getPasswordExpired()){
            log.warn("MQTT签名时间[原始字符串=>{}  转化后:{}] 系统当前时间:{}", timestamp, signTime, curr);
            return false;
        }
        return true;
    }

    public static void main(String[] args){
        String userName = "yr1bw0ytbt4uqmfp|123456789";
        String srand = userName + BCConstants.AUTH.SIGN_SPLIT + "bbn7mxhw6sjodn9hvaap";
        String sign = Md5Utils.md5(srand);
        System.out.println(sign);
    }
}
