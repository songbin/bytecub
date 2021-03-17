package com.bytecub.mqtt.domain.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 服务配置
 * @author dell
 */
@Component
@ConfigurationProperties(prefix = "bytecub.mqtt.broker")
public class BrokerProperties {

	/**
	 * Broker唯一标识
	 */
	private String id;
	private boolean sslEnable = true;
	private boolean normalEnable = true;
	/**
	 * SSL端口号, 默认8883端口
	 */
	private int sslPort = 8885;

	private int port = 1883;

	/**
	 * WebSocket SSL端口号, 默认9993端口
	 */
	private int websocketSslPort = 9995;

	/**
	 * WebSocket Path值, 默认值 /mqtt
	 */
	private String websocketPath = "/mqtt";

	/**
	 * SSL密钥文件密码
	 */
	private String sslPassword;

	/**
	 * 心跳时间(秒), 默认60秒, 该值可被客户端连接时相应配置覆盖
	 */
	private int keepAlive = 60;

	/**
	 * 是否开启Epoll模式, 默认关闭
	 */
	private boolean useEpoll = false;

	/**
	 * Sokcet参数, 存放已完成三次握手请求的队列最大长度, 默认511长度
	 */
	private int soBacklog = 511;

	/**
	 * Socket参数, 是否开启心跳保活机制, 默认开启
	 */
	private boolean soKeepAlive = true;
	/**
	 * MQTT密钥验签有效期，-1时标识永不过期
	 * */
	private long passwordExpired = 1000*60*10;

	public long getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(long passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public String getId() {
		return id;
	}

	public BrokerProperties setId(String id) {
		this.id = id;
		return this;
	}

	public int getSslPort() {
		return sslPort;
	}

	public BrokerProperties setSslPort(int sslPort) {
		this.sslPort = sslPort;
		return this;
	}

	public int getPort() {
		return port;
	}

	public BrokerProperties setPort(int port) {
		this.port = port;
		return this;
	}

	public int getWebsocketSslPort() {
		return websocketSslPort;
	}

	public BrokerProperties setWebsocketSslPort(int websocketSslPort) {
		this.websocketSslPort = websocketSslPort;
		return this;
	}

	public String getWebsocketPath() {
		return websocketPath;
	}

	public BrokerProperties setWebsocketPath(String websocketPath) {
		this.websocketPath = websocketPath;
		return this;
	}

	public String getSslPassword() {
		return sslPassword;
	}

	public BrokerProperties setSslPassword(String sslPassword) {
		this.sslPassword = sslPassword;
		return this;
	}

	public int getKeepAlive() {
		return keepAlive;
	}

	public BrokerProperties setKeepAlive(int keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public boolean isUseEpoll() {
		return useEpoll;
	}

	public BrokerProperties setUseEpoll(boolean useEpoll) {
		this.useEpoll = useEpoll;
		return this;
	}

	public boolean isSslEnable() {
		return sslEnable;
	}

	public BrokerProperties setSslEnable(boolean sslEnable) {
		this.sslEnable = sslEnable;
		return this;
	}

	public boolean isNormalEnable() {
		return normalEnable;
	}

	public BrokerProperties setNormalEnable(boolean normalEnable) {
		this.normalEnable = normalEnable;
		return this;
	}

	public int getSoBacklog() {
		return soBacklog;
	}

	public BrokerProperties setSoBacklog(int soBacklog) {
		this.soBacklog = soBacklog;
		return this;
	}

	public boolean isSoKeepAlive() {
		return soKeepAlive;
	}

	public BrokerProperties setSoKeepAlive(boolean soKeepAlive) {
		this.soKeepAlive = soKeepAlive;
		return this;
	}




}
