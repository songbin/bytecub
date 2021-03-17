package com.bytecub.mqtt.service.protocol;


import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.state.ClientManager;
import com.bytecub.mqtt.service.state.SessionManger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author songbin
 * @version Id: DisconnectHandler.java, v 0.1 2019/1/16   Exp $$
 */
@Component
public class DisconnectHandler {
    private static final Logger logger = LoggerFactory.getLogger(DisconnectHandler.class);

    public  void onDisconnect(ContextBo contextBo) {
        logger.debug("接收到disconnect请求:{}", contextBo);
        try{
            if(!contextBo.getConnected()){
                contextBo.getHandlerContext().close();
                return;
            }

            SessionManger.disConnect(contextBo.getClientId());
            ClientManager.removeClient(contextBo.getClientId());
        }catch (Exception e){

        }
//        if (!this.connected) {
//            ctx.close();
//            return;
//        }
//
//        BrokerSessionHelper.removeSession(this.clientId, ctx);
//
//        this.willMessage = null;
//
//        this.connected = false;
//
//        ctx.close();

    }
}
