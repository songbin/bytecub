package com.bytecub.mqtt.service.state;

import com.bytecub.mqtt.domain.bo.ContextBo;
import com.bytecub.mqtt.service.biz.ProducerBiz;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**网络连接管理
 * clientId和网络链接的映射关系
 * @author songbin
 * @version Id: SessionManger.java, v 0.1 2019/1/17   Exp $$
 */
public class SessionManger {
    private static final Logger                          logger     = LoggerFactory.getLogger(SessionManger.class);
    //存储clientId和网络上下文映射关系
    private static       Map<String, ContextBo>          contextMap = new ConcurrentHashMap<>();
    /**key 网络链接， value 上下文信息*/
    private static Map<ChannelHandlerContext, ContextBo> ctxMap     = new ConcurrentHashMap<>();

    public static Map<String, ContextBo> getContextMap(){
        return contextMap;
    }
    public static boolean checkClientOnline(String clientId){
        if(contextMap.containsKey(clientId)){
            return true;
        }
        return false;
    }
    /**
     *  当有新合法网络链接请求，使用该方法进行网络上线文关系存储
     *  1.存储上下文和clientId的映射
     *  2.存储上下文和CTX的映射
     * */
    public static void newConnect(String clientId, ContextBo contextBo){
        if(StringUtils.isEmpty(clientId) || !verifyContext(contextBo)){
            logger.warn("newconnect[{}]的上下文有误{}", contextBo, contextBo);
            return;
        }
        // 处理连接心跳包
        if (contextBo.getKeepAlive() > 0) {
            if (contextBo.getHandlerContext().pipeline().names().contains("idle")) {
                contextBo.getHandlerContext().pipeline().remove("idle");
            }
            contextBo.getHandlerContext().pipeline().addFirst("idle", new IdleStateHandler(0, 0, Math.round(contextBo.getKeepAlive() * 1.5f)));
        }
        contextMap.put(clientId, contextBo);
        ctxMap.put(contextBo.getHandlerContext(), contextBo);
        ClientManager.updateClientOnLine(contextBo.getClientId());
        ProducerBiz.sendActiveMQ(contextBo, true);

    }
    /**
     *  当client发起disconnect请求，使用该方法更新存储关系 同时关闭相关链接
     * */
    public static void disConnect(String clientId){
        try{
            removeContextByClientId(clientId);
        }catch (Exception e){
            logger.warn("MQTT连接断开异常", e);
        }

    }

    /**
     * ping 连续超时移除链接
     * */
    public static void pingTimeout(String clientId, ContextBo contextBo){
        try{
            removeContextByClientId(clientId);
        }catch (Exception e){

        }
    }

    /**
     * 根据clientId获取上下文网络信息
     * */
    public static ContextBo getContextByClientId(String clientId){
        return contextMap.get(clientId);
    }
    /**
     * 关闭上下文网络
     * */
    public static void removeContextByClientId(String clientId){
        try{
            if(StringUtils.isEmpty(clientId) || !contextMap.containsKey(clientId) ){
                return ;
            }

            ContextBo contextBo = contextMap.get(clientId);
            if(!verifyContext(contextBo)){
                logger.warn("disconnect[{}]的上下文有误{}", contextBo, contextBo);
                return;
            }
            contextBo.getHandlerContext().close();
            contextMap.remove(clientId);
            ctxMap.remove(contextBo.getHandlerContext());
            ProducerBiz.sendActiveMQ(contextBo, false);
        }catch (Exception e){

        }
    }

    /**
     * 关闭上下文网络
     * */
    public static void removeContextByCtx(ChannelHandlerContext ctx){
        try{
            if(null == ctx || !ctxMap.containsKey(ctx) ){
                return ;
            }
            ctx.close();
            ContextBo contextBo = ctxMap.get(ctx);
            if(!verifyContext(contextBo)){
                logger.warn("disconnect[{}]的上下文有误", contextBo);
                return;
            }
            contextMap.remove(contextBo.getClientId());
            ctxMap.remove(contextBo.getHandlerContext());
            ClientManager.removeClient(contextBo.getClientId());
            ProducerBiz.sendActiveMQ(contextBo, false);
        }catch (Exception e){

        }
    }
    private static boolean verifyContext(ContextBo contextBo){
        if(null == contextBo || null == contextBo.getHandlerContext()){
            return false;
        }
        return true;
    }


}
