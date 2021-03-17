package com.bytecub.mqtt.service.network.impl;

import com.bytecub.mqtt.domain.config.BrokerProperties;
import com.bytecub.mqtt.service.core.HandlerFactory;
import com.bytecub.mqtt.service.core.MqttHandler;
import com.bytecub.mqtt.service.network.MQTTServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

/**
 * @author songbin
 * @version Id: NettyServerImpl.java, v 0.1 2019/1/15 Exp $$
 */
@Service
@Slf4j
public class MQTTServerImpl implements MQTTServer {

    @Autowired
    private BrokerProperties brokerProperties;
    @Autowired
    HandlerFactory handlerFactory;
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private Channel channel = null;
    private EventLoopGroup sslBossGroup = null;
    private EventLoopGroup sslWorkerGroup = null;
    private Channel sslChannel = null;
    private SslContext sslContext;

    @Override
    public void start() throws Exception {

        this.startMQTT();
        this.startMQTTSSL();

    }

    private void startMQTT() {
        try {
            if (!brokerProperties.isNormalEnable()) {
                log.info("MQTT普通模式禁用");
                return;
            }
            ServerBootstrap b = new ServerBootstrap();
            bossGroup = brokerProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            workerGroup = brokerProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            b.group(bossGroup, workerGroup)
                .channel(brokerProperties.isUseEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                // handler在初始化时就会执行
                .handler(new LoggingHandler(LogLevel.INFO))
                // childHandler会在客户端成功connect后才执行
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        log.info("initChannel ch:{}", ch);
                        try {
                            ChannelPipeline p = ch.pipeline();
                            // Netty提供的心跳检测
                            p.addFirst("idle",
                                new IdleStateHandler(0, 0, brokerProperties.getKeepAlive(), TimeUnit.SECONDS));

                            p.addLast(new MqttDecoder());
                            p.addLast(MqttEncoder.INSTANCE);
                            p.addLast(new MqttHandler(handlerFactory));
                        } catch (Exception e) {
                            log.warn("new connect exception", e);
                            throw new RuntimeException(e);
                        }

                    }

                }).option(ChannelOption.SO_BACKLOG, brokerProperties.getSoBacklog())
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

            channel = b.bind(brokerProperties.getPort()).sync().channel();
            log.info("MQTT 启动，监听端口:{}", brokerProperties.getPort());
        } catch (Exception e) {
            log.error("启动mqtt server失败", e);
            System.exit(1);
        }

    }

    private void startMQTTSSL() {
        try {
            if (!brokerProperties.isSslEnable()) {
                log.info("MQTT ssl禁用模式");
                return;
            }
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("keystore/mqtt-broker.pfx");
            keyStore.load(inputStream, brokerProperties.getSslPassword().toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, brokerProperties.getSslPassword().toCharArray());
            sslContext = SslContextBuilder.forServer(kmf).build();
            ServerBootstrap sb = new ServerBootstrap();
            sslBossGroup = brokerProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            sslWorkerGroup = brokerProperties.isUseEpoll() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
            sb.group(sslBossGroup, sslWorkerGroup)
                .channel(brokerProperties.isUseEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        log.info("initChannel ch:{}", ch);
                        ChannelPipeline p = ch.pipeline();
                        // Netty提供的心跳检测
                        p.addFirst("idle", new IdleStateHandler(0, 0, brokerProperties.getKeepAlive()));

                        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
                        sslEngine.setUseClientMode(false); // 服务端模式
                        sslEngine.setNeedClientAuth(false); // 不需要验证客户端
                        p.addLast("ssl", new SslHandler(sslEngine));
                        p.addLast(new MqttDecoder());
                        p.addLast(MqttEncoder.INSTANCE);
                        p.addLast(new MqttHandler(handlerFactory));
                    }
                }).option(ChannelOption.SO_BACKLOG, brokerProperties.getSoBacklog())
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

            sslChannel = sb.bind(brokerProperties.getSslPort()).sync().channel();
            log.info("MQTT SSL 启动，监听端口:{}", brokerProperties.getSslPort());
        } catch (Exception e) {
            log.error("启动SSL mqtt server失败", e);
            System.exit(1);
        }

    }

    @Override
    public void stop() {
        try {
            log.info("Shutdown {} MQTT Broker ...", "[" + brokerProperties.getId() + "]");
            if (brokerProperties.isNormalEnable()) {
                bossGroup.shutdownGracefully();
                bossGroup = null;
                workerGroup.shutdownGracefully();
                workerGroup = null;
                channel.closeFuture().syncUninterruptibly();
                channel = null;
            }
            if (brokerProperties.isSslEnable()) {
                sslBossGroup.shutdownGracefully();
                sslBossGroup = null;
                sslWorkerGroup.shutdownGracefully();
                sslWorkerGroup = null;
                sslChannel.closeFuture().syncUninterruptibly();
                sslChannel = null;
            }

            // websocketChannel.closeFuture().syncUninterruptibly();
            // websocketChannel = null;
            log.info("MQTT Broker {} shutdown finish.", "[" + brokerProperties.getId() + "]");
        } catch (Exception e) {
            log.warn("销毁网络异常", e);
        }

    }

}
