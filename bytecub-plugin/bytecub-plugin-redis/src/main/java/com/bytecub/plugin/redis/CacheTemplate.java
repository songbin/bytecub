package com.bytecub.plugin.redis;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存操作接口.
 *
 * @author
 * @date 2018年7月30日17:01:38
 */
public interface CacheTemplate {
    /**
     * 获取缓存值
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return T
     */
    <T> T get(String key, Class<T> clazz);
    /**
     * @return
     * */
    String get(String key);

    <T> Boolean setnx(String key, String data, Integer seconds);

    <T> Boolean  publish(String channel, T data);

    /**
     * 获取缓存值列表
     *
     * @param keys
     * @return Map<String, String>
     */
    Map<String, String> getBatched(Set<String> keys);

    /**
     * 获取缓存值列表
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return List<T>
     */
    <T> List<T> getList(String key, Class<T> clazz);
    /**
     * 获取缓存值列表
     *
     * @param keys
     * @param clazz
     * @param <T>
     * @return List<T>
     */
    <T> List<T> mget(List<String> keys, Class<T> clazz);
    /**
     * 获取缓存值列表
     *
     * @param keys
     * @param clazz
     * @param <T>
     * @return List<T>
     */
    List<String> mgetString(List<String> keys);
    /**
     * 获取缓存值列表
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Map<String, List<T>> getMapArray(String key, Class<T> clazz);

    /**
     * 获取缓存值列表
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Map<String, T> getMap(String key, Class<T> clazz);

    /**
     * 存储缓存值
     *
     * @param key
     * @param value
     * @param timeout
     * @param <T>
     */
    <T> void set(String key, T value, Integer timeout);

    /**
     * 储存缓存值
     *
     * @param key
     * @param value
     * @param <T>
     */
    <T> void set(String key, T value);

    /**
     * 精准删除
     *
     * @param key
     */
    void remove(String key);

    /**
     * 模糊删除(有性能损耗，不推荐使用)
     *
     * @param key
     */
    void removeLike(String key);

    /**
     * 判断缓存中是否存在
     *
     * @param key
     * @return Boolean
     */
    Boolean exists(String key);

    /**
     * 全局序列
     *
     * @param key
     * @return Long
     */
    Long getSequence(String key);

    /**
     * 从value中随机删除一个元素.
     *
     * @param key 键值
     * @return String 删除的元素
     */
    String sPop(String key);

    /**
     * 将一个或多个 item 元素加入到集合 key 当中，已经存在于集合的 item 元素将被忽略.
     *
     * @param key  键值
     * @param item 待加入集合的元素
     */
    void sAdd(String key, String... item);

    /**
     * 获取集合的成员数.
     *
     * @param key 键值
     * @return Long
     */
    Long sCard(String key);

    /**
     * 返回集合 key 中的所有成员, 不存在的 key 被视为空集合
     *
     * @param key 键值
     * @return
     */
    Set<String> sMembers(String key);

    /**
     * 返回集合 key 中是否有此成员
     *
     * @param key 键值
     * @param member
     * @return
     */
    Boolean sismember(String key, String member);

    /**
     * 获取byte[]数据.
     *
     * @param key 缓存key
     */
    byte[] getByte(byte[] key);

    /**
     * 往列表头部插入数据.
     *
     * @param key   缓存key
     * @param value 值
     */
    void lPush(String key, String value);
    /**
     * 右边删除.
     *
     * @param key   缓存key
     * @return value
     */
    String rPop(String key);

    /**
     * 往列表尾部插入数据.
     *
     * @param key   缓存key
     * @param value 值
     */
    void rPush(String key, String value);

    /**
     * 从列表头部获取数据.
     *
     * @param key 缓存key
     * @return
     */
    String lPop(String key);

    /**
     * 获取列表长度
     *
     * @param key 缓存key
     * @return 列表长度，若返回-1则表示取值发生异常
     */
    Long lLen(String key);

    /**
     * 计数器递增
     *
     * @param key 缓存key
     * @return 执行 incr 命令之后 key 的值
     */
    Long incr(String key);

    /**
     * 计数器递减
     *
     * @param key 缓存key
     * @return 执行 decr 命令之后 key 的值
     */
    Long decr(String key);

    /**
     * 设置key有效期
     *
     * @param key     缓存key
     * @param seconds 失效时间
     */
    void expire(String key, int seconds);

    /**
     * 设置key有效期
     *
     * @param key    缓存key
     * @param expire 失效时间
     */
    void expireBytesKey(byte[] key, int expire);

    /**
     * 获取键的剩余有效秒数
     * 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以毫秒为单位，返回 key 的剩余生存时间
     * 注意：在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1
     *
     * @param key
     */
    Long pTtl(String key);

    /**
     * 获取List第n个值
     *
     * @param key   缓存key
     * @param index 索引
     */
    String lIndex(String key, Long index);

    /**
     * redis加法
     *
     * @param key 缓存key
     * @return 执行 incr 命令之后 key 的值
     */
    Long incr(String key, Integer num);


    /**
     * 模糊查询key
     *
     * @param key
     * @return
     */
    Set<String> keyLike(String key);

    /**
     * songbin
     * 地理位置存储
     *
     * @param key
     * @param lng    double 经度
     * @param lat    double 纬度
     * @param member String 存储内容
     * @return ret <0 错误
     */
    Long geoAdd(String key, Double lng, Double lat, String member);

    /**
     * songbin
     * 获取member经纬度坐标
     *
     * @param key
     * @param member String 成员名，可变长度参数
     * @return ret List<GeoCoordinate>
     */
    List<GeoCoordinate> geoPos(String key, String... member);

    /**
     * songbin
     * 获取两个membre直接的距离
     *
     * @param key
     * @param startMember String 成员名1
     * @param endMember   String 成员名2
     * @return ret 距离,单位m
     */
    Double geoDist(String key, String startMember, String endMember);

    /**
     * songbin
     * 搜索距离指定坐标指定范围内的所有成员，单位 米
     *
     * @param key
     * @param longitude double 经度
     * @param latitude  double 纬度
     * @param radius    搜索范围 单位 米
     * @return ret List<GeoRadiusResponse>
     */
    List<GeoRadiusResponse> geoRadius(String key, double longitude, double latitude, double radius);

    /**
     * songbin
     * 搜索距离指定成员指定范围内的所有成员，单位 米
     *
     * @param key
     * @param member 指定成员
     * @param radius 搜索范围 单位 米
     * @return ret List<GeoRadiusResponse>
     */
    List<GeoRadiusResponse> geoRadiusMember(String key, String member, double radius);

    /**
     * songbin
     * 返回一个或多个位置元素的 Geohash 表示
     *
     * @param key
     * @param members 指定成员
     * @return ret List<GeoRadiusResponse>
     */
    List<String> geoHash(String key, String... members);

    /**
     * songbin
     * 删除sortedSet指定成员
     *
     * @param key
     * @param members 指定成员
     * @return ret Long
     */
    Long zRem(String key, String... members);
    Long zcard(String key);

    /**
     * songbin
     * 向添加sortedSet成员
     *
     * @param key    String
     * @param score  Double 排序打分，按照score从小到大排序
     * @param member String 指定成员
     * @return ret Long
     */
    Long zAdd(String key, Double score, String member);

    /**
     * songbin
     * 返回有序集 key 中，指定区间内的成员。
     *
     * @param key   String
     * @param start long 开始下标
     * @param end   long 结束下标
     */
    Set<String> zRange(String key, long start, long end);

    /**
     * songbin
     * 返回有序集 key 中，指定区间内的成员。
     *
     * @param key   String
     * @param start long 开始下标
     * @param end   long 结束下标
     */
    Set<Tuple> zRangeWithScores(String key, long start, long end);

    /**
     * songbin
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * 有序集成员按 score 值递增(从小到大)次序排列。
     *
     * @param key String
     * @param min double 最小score
     * @param max double 最大score
     */
    Set<Tuple> zRangeByScoreWithScores(String key, double min, double max);

    /**
     * songbin
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     * 有序集成员按 score 值递增(从小到大)次序排列。
     *
     * @param key String
     * @param min double 最小score
     * @param max double 最大score
     */
    Set<String> zRangeByScore(String key, double min, double max);

    /**
     * songbin
     * 移除有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。
     *
     * @param key String
     * @param min double 最小score
     * @param max double 最大score
     */
    Long zRemRangeByScore(String key, double min, double max);

    /**
     * 新增一个key-value  成功返回1 已存在无操纵返回0
     *
     * @param key
     * @param value
     * @return long
     * @drging
     */
    Long addNewString(String key, String value);

    /**
     * 新增一个哈希table  成功返回1 已存在无操纵返回0
     *
     * @param key
     * @param field
     * @param value
     * @return long
     * @drging
     */
    Long addHashMap(String key, String field, String value);

    /**
     * 新增一个哈希table  成功返回1 已存在无操纵返回0
     *
     * @param key
     * @param field
     * @param value
     * @return long
     * @drging
     */
    Long addHashMapTime(String key, String field, String value, int timeOut);

    /**
     * 根据一个map的key，field获取value
     *
     * @param key
     * @param field
     * @return String
     * @drging
     */
    String getHashMap(String key, String field);

    /**
     * 根据一个map的key获取全部数据
     *
     * @param key
     * @return Map<String, String>
     * @drging
     */
    Map<String, String> getHashMapAll(String key);

    /**
     * 移除 map 中的元素
     *
     * @param key
     * @param field
     * @return
     */
    Long removeHashMap(String key, String field);

    /**
     * 获取MAP的长度
     *
     * @param key
     * @return
     */
    Long getHashMapLen(String key);

    /**
     * 删除Set指定成员
     *
     * @param key
     * @param members 指定成员
     * @return ret Long
     */
    Long sRem(String key, String... members);


    /**
     * incr 并设定有效期
     * @param key
     * @param seconds
     * @return
     */
    Long incrTime(String key, int seconds);

    /**
     *  添加过期时间
     * @param key
     * @param score
     * @param member
     * @param seconds
     * @return
     */
    Long zAddTime(String key, Double score, String member, int seconds);

    /**
     *  SortSet 自增长排序
     * @param key
     * @param member
     * @param seconds
     * @return
     */
    Double zIncrbyTime(String key, String member, int seconds);

    /**
     *  添加有效期操作
     * @param key
     * @param seconds
     * @param item
     */
    void sAddTime(String key, int seconds, String... item);

    /**
     *  获取SORT SET TOPn
     * @param key
     * @param start 0 开始
     * @param end  -1  全部
     * @return
     */
    Set<Tuple> zrevRangeWithScores(String key, int start, int end);
}

