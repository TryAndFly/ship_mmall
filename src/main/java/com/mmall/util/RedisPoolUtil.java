package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {

    /**
     * 设置key的有效期
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key ,int exTime){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("setex key:{} , error",key,e);

            RedisPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    //exTime的单位是s
    public static String setEx(String key,String vaule,int exTime){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,vaule);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("setex key:{} ,value:{} error",key,vaule,e);

            RedisPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    public static String set(String key,String vaule){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key,vaule);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("set key:{} ,value:{} error",key,vaule,e);
            RedisPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("set key:{} error",key,e);
            RedisPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("del key:{} error",key,e);
            RedisPool.returnBrokenResouce(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();

        RedisPoolUtil.set("keyTest","value");
        String vale = RedisPoolUtil.get("keyTest");

        RedisPoolUtil.setEx("keyTestEx","valueEx",60*10);

        RedisPoolUtil.expire("keyTest",60*20);
        RedisPoolUtil.del("keyTest");
        System.out.println("end");
    }

}
