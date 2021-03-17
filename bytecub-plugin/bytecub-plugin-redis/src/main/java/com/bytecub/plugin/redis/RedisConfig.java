package com.bytecub.plugin.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * jedis配置类
 * @author caol
 * 所有单位都是毫秒s
 * @date 2018-11-30
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private Logger          logger = LoggerFactory.getLogger(RedisConfig.class);
    @Autowired
    private RedisProperties redisProperties;
    @Value("${spring.redis.pool.max-active:1500}")
    int maxActive;
    @Value("${spring.redis.pool.max-idle:30}")
    int maxIdle;
    @Value("${spring.redis.pool.max-total:30}")
    int maxTotal;
    @Value("${spring.redis.pool.max-wait:3000}")
    int maxWait;
    @Value("${spring.redis.pool.min-idle:0}")
    int minIdle;
    @Value("${spring.redis.pool.timeout:3000}")
    int timeout;

    @Value("${spring.redis.database:0}")
    int database;



    /**
     * 创建redis连接池.
     *
     * @return JedisPool
     */
    @Bean(name = "jedisPool")
    public redis.clients.jedis.util.Pool redisPoolFactory() {
        try{
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setMaxIdle(this.maxIdle);
            config.setMaxWaitMillis(this.maxWait);
            config.setMaxTotal(this.maxActive);
            logger.info("redis configuration is : {}", config);
            //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时
            config.setBlockWhenExhausted(false);
            config.setTestOnReturn(true);
            if(null != redisProperties.getSentinel()){
                logger.info("redis sentinel地址：" + redisProperties.getSentinel().getNodes());

                Set<String> set = new HashSet<String>(redisProperties.getSentinel().getNodes());
                JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(redisProperties.getSentinel().getMaster(), set, config, timeout, redisProperties.getPassword(),database);
                logger.info("JedisSentinelPool注入成功！！");
                return jedisSentinelPool;
            }else{
                logger.info("redis地址：" + redisProperties.getHost() + ":" + redisProperties.getPort());
                JedisPool jedisPool = new JedisPool(config, redisProperties.getHost(), redisProperties.getPort(), timeout, redisProperties.getPassword(),database);
                logger.info("JedisPool注入成功！！");
                return jedisPool;
            }
        }catch (Exception e){
            logger.warn("创建redis对象异常");
            throw e;
        }

    }





}
