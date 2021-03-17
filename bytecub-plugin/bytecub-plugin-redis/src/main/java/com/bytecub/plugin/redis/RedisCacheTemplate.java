package com.bytecub.plugin.redis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bytecub.utils.DateUtil;
import com.bytecub.utils.JSONProvider;
import com.bytecub.utils.JsonUtil;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;
import redis.clients.jedis.params.GeoRadiusParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * Redis缓存实现
 *
 * @author hanxiaoqiang on 2017/1/4.
 * @date 2018年7月30日17:03:46
 */
@Component
@Setter
public class RedisCacheTemplate implements CacheTemplate {
    /**
     * redis key过期时间，24小时
     */
    public static final Integer KEY_EXPIRED_DURA = 1000 * 60 * 60 * 24;

    private static final Logger logger = LoggerFactory.getLogger( RedisCacheTemplate.class );

    @Resource
    private JedisPool jedisPool;

    @Override
    public <T> Boolean publish(String channel, T data) {
        Jedis jedis = jedisPool.getResource();
        try {
            String dataString = JSONProvider.toJSONString(data);
            long ret = jedis.publish(channel, dataString);
            return 1 == ret ? true : false;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public  Boolean setnx(String key, String data, Integer seconds) {
        Jedis jedis = jedisPool.getResource();
        try {
            long ret = jedis.setnx(key, data);
            if(ret > 0){
                jedis.expire(key, seconds);
            }
            return 1 == ret ? true : false;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            String value = jedis.get( key );
            return value;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Jedis jedis = jedisPool.getResource();
        try {
            String value = jedis.get( key );
            if(null == value){
                return null;
            }
            if (clazz.newInstance() instanceof String) {
                return (T) value;
            }
            return (T)JSONProvider.parseObject( value, clazz );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Map<String, String> getBatched(Set<String> keys) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> map = new HashMap<>();
        try {
            for (String key : keys) {
                String value = jedis.get( key );
                if(null == value){
                    continue;
                }
                map.put(key, value);
            }
            return map;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return map;
    }

    @Override
    public List<String> mgetString(List<String> keys) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<String> results = new ArrayList<>();
            if(CollectionUtils.isEmpty(keys)){
                return results;
            }

            results = jedis.mget(keys.toArray(new String[keys.size()]));
            return results;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;

    }

    @Override
    public <T> List<T> mget(List<String> keys, Class<T> clazz) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<T> results = new ArrayList<>();
            if(CollectionUtils.isEmpty(keys)){
                return results;
            }

            List<String> list = jedis.mget(keys.toArray(new String[keys.size()]));
            for (String item:list){
                T result = JSONProvider.parseObject(item, clazz);
                if(null == result){
                    continue;
                }
                results.add(result);
            }
            return  results;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;

    }


    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        Jedis jedis = jedisPool.getResource();
        try {
            return JsonUtil.parseArray( jedis.get( key ), clazz );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 获取缓存值列表
     *
     * @param key
     * @param clazz
     * @return
     */
    @Override
    public <T> Map<String, List<T>> getMapArray(String key, Class<T> clazz) {
        Jedis jedis = jedisPool.getResource();
        try {
            Map<String, JSONArray> maps = this.get( key, Map.class );
            Map<String, List<T>> conventResult = null;
            if (maps != null && maps.size() > 0) {
                conventResult = new HashMap<>( maps.size() );
                Iterator<String> iterator = maps.keySet().iterator();
                String mapKey;
                while (iterator.hasNext()) {
                    mapKey = iterator.next();
                    List<T> arrays = JsonUtil.parseArray( maps.get( mapKey ).toJSONString(), clazz );
                    conventResult.put( mapKey, arrays );
                }
            }
            return conventResult;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 获取缓存值列表
     *
     * @param key
     * @param clazz
     * @return
     */
    @Override
    public <T> Map<String, T> getMap(String key, Class<T> clazz) {
        Jedis jedis = jedisPool.getResource();
        try {
            Map<String, JSONObject> maps = this.get( key, Map.class );
            Map<String, T> conventResult = null;
            if (maps != null && maps.size() > 0) {
                conventResult = new HashMap<>( maps.size() );
                Iterator<String> iterator = maps.keySet().iterator();
                String mapKey;
                while (iterator.hasNext()) {
                    mapKey = iterator.next();
                    if (clazz == String.class) {
                        conventResult.put( mapKey, (T) maps.get( mapKey ) );
                    } else {
                        conventResult.put( mapKey, JsonUtil
                                .parseObject( maps.get( mapKey ).toJSONString(), clazz ) );
                    }
                }
            }
            return conventResult;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public <T> void set(String key, T value, Integer timeout) {
        Jedis jedis = jedisPool.getResource();
        try {
            String data = "";
            if (value instanceof String) {
                data = (String) value;
            } else {
                data = JSONProvider.toJSONString( value );
            }
            jedis.setex( key, timeout, data );

        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 储存缓存值
     *
     * @param key
     * @param value
     */
    @Override
    public <T> void set(String key, T value) {
        Jedis jedis = jedisPool.getResource();
        try {
            String data = "";
            if (value instanceof String) {
                data = (String) value;
            } else {
                data = JSONProvider.toJSONString( value );
            }
            jedis.set( key, data );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void remove(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.del( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void removeLike(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            Set<String> keys = jedis.keys( key + "*" );
            if (!CollectionUtils.isEmpty( keys )) {
                jedis.del( keys.toArray( new String[keys.size()] ) );
            }
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public Long getSequence(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            //超时时间为1小时
            int timeout = 3600;
            Long seq = jedis.incr( key );
            jedis.expire( key, timeout );
            //14位日期+4位数字
            StringBuilder builder = new StringBuilder();
            builder.append( DateUtil.getNow( DateUtil.longFormat ) );
            builder.append( StringUtils.leftPad( String.valueOf( seq ), 4, '0' ) );

            return Long.valueOf( builder.toString() );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public String sPop(String key) {
        Jedis jedis = jedisPool.getResource();
        try {

            return jedis.spop( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public String rPop(String key) {
        Jedis jedis = jedisPool.getResource();
        try {

            return jedis.rpop( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public void sAdd(String key, String... item) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.sadd( key, item );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Long sCard(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.scard( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Set<String> sMembers(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.smembers( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return new HashSet<>();
    }

    @Override
    public Boolean sismember(String key, String member) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.sismember( key, member );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public byte[] getByte(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public void lPush(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.lpush( key, value );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void rPush(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.rpush( key, value );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String lPop(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lpop( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Long lLen(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.llen( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incr( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Long decr(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.decr( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public void expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire( key, seconds );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void expireBytesKey(byte[] key, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.expire( key, expire );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public Long pTtl(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.pttl( key );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public String lIndex(String key, Long index) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.lindex( key, index );
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Long incr(String key, Integer num) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.incrBy( key, num );
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Set<String> keyLike(String keyPrefix) {
        Jedis jedis = jedisPool.getResource();
        try {
            Set<String> keys = jedis.keys(keyPrefix + "*" );
            return keys;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return null;
    }

    @Override
    public Long geoAdd(String key, Double lng, Double lat, String member) {
        Jedis jedis = jedisPool.getResource();
        try {

            return jedis.geoadd( key, lng, lat, member );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }

    }

    @Override
    public List<GeoCoordinate> geoPos(String key, String... member) {
        Jedis jedis = jedisPool.getResource();
        try {
            List<GeoCoordinate> list = jedis.geopos( key, member );
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }


    @Override
    public Double geoDist(String key, String startMember, String endMember) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.geodist( key, startMember, endMember );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public List<GeoRadiusResponse> geoRadius(String key, double longitude, double latitude, double radius) {
        Jedis jedis = jedisPool.getResource();
        try {
            GeoRadiusParam param = GeoRadiusParam.geoRadiusParam();
            param.withCoord();
            param.withDist();
            return jedis.georadius( key, longitude, latitude, radius, GeoUnit.M, param );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public List<GeoRadiusResponse> geoRadiusMember(String key, String member, double radius) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.georadiusByMember( key, member, radius, GeoUnit.M );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public List<String> geoHash(String key, String... members) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.geohash( key, members );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long zRem(String key, String... members) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrem( key, members );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long zcard(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zcard(key);
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long sRem(String key, String... members) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.srem( key, members );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long zAdd(String key, Double score, String member) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zadd( key, score, member );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Set<String> zRange(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrange( key, start, end );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Set<Tuple> zRangeWithScores(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrangeWithScores( key, start, end );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }


    @Override
    public Set<Tuple> zRangeByScoreWithScores(String key, double min, double max) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrangeByScoreWithScores( key, min, max );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Set<String> zRangeByScore(String key, double min, double max) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zrangeByScore( key, min, max );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long zRemRangeByScore(String key, double min, double max) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.zremrangeByScore( key, min, max );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long addNewString(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.setnx( key, value );
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long addHashMap(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hset( key, field, value );

        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Long addHashMapTime(String key, String field, String value,int timeOut) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long hashSize =  jedis.hset( key, field, value );
            jedis.expire(key,timeOut);
            return hashSize;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }
    @Override
    public String getHashMap(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hget( key, field );

        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public Map<String,String> getHashMapAll(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hgetAll( key );

        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    /**
     *  移除 map 中的元素
     * @param key
     * @param field
     * @return
     */
    public Long removeHashMap(String key,String field){
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hdel(key,field);
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    /**
     *  获取MAP的长度
     * @param key
     * @return
     */
    public Long getHashMapLen(String key){
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.hlen(key);
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    /**
     *
     * @param key
     * @param seconds 有效期
     * @return
     */
    @Override
    public Long incrTime(String key,int seconds ) {
        Jedis jedis = jedisPool.getResource();
        try {
             long incrNum = jedis.incr( key );
             jedis.expire(key,seconds);
            return incrNum;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    /**
     *  添加有效期
     * @param key
     * @param score
     * @param member
     * @param seconds
     * @return
     */
    @Override
    public Long zAddTime(String key, Double score, String member,int seconds ) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long  zAddNum =  jedis.zadd( key, score, member );
            jedis.expire(key,seconds);
            return zAddNum;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    /**
     *  添加有效期
     * @param key
     * @param member
     * @param seconds
     * @return
     */
    @Override
    public Double zIncrbyTime(String key, String member,int seconds ) {
        Jedis jedis = jedisPool.getResource();
        try {
            Double  zAddNum = jedis.zincrby(key,1,member);
            jedis.expire(key,seconds);
            return zAddNum;
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    /**
     *  添加有效期
     * @param key  键值
     * @param item 待加入集合的元素
     */
    @Override
    public void sAddTime(String key,int seconds, String... item) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.sadd( key, item );
            jedis.expire(key,seconds);
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    /**
     *   获取SORT SET 排序TOPN
     * @param key  键值
     * @param start 从0开始
     * @param end 结束 -1  全部
     */
    @Override
    public  Set<Tuple> zrevRangeWithScores(String key,int start,int end) {
        Jedis jedis = jedisPool.getResource();
        try {
            Set<Tuple>  sortedSet = jedis.zrevrangeWithScores(key,start,end);
            return sortedSet;
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
