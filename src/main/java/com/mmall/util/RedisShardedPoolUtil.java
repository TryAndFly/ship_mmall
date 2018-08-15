package com.mmall.util;

import com.mmall.common.RedisSharderPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisShardedPoolUtil {

    /**
     * 设置key的有效期
     *
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisSharderPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("setex key:{} , error", key, e);

            RedisSharderPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisSharderPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是s
    public static String setEx(String key, String vaule, int exTime) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisSharderPool.getJedis();
            result = jedis.setex(key, exTime, vaule);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("setex key:{} ,value:{} error", key, vaule, e);

            RedisSharderPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisSharderPool.returnResource(jedis);
        return result;
    }


    public static String set(String key, String vaule) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisSharderPool.getJedis();
            result = jedis.set(key, vaule);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("set key:{} ,value:{} error", key, vaule, e);
            RedisSharderPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisSharderPool.returnResource(jedis);
        return result;
    }

    public static Long setnx(String key, String vaule) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisSharderPool.getJedis();
            result = jedis.setnx(key, vaule);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("set key:{} ,value:{} error", key, vaule, e);
            RedisSharderPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisSharderPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisSharderPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("set key:{} error", key, e);
            RedisSharderPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisSharderPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisSharderPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("del key:{} error", key, e);
            RedisSharderPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisSharderPool.returnResource(jedis);
        return result;
    }
}
