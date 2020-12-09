package com.bjtu.redis.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JedisDemo {

    /**
     * 基本使用
     */
    @Test
    public void basicUse() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("服务正在运行: " + jedis.ping());
        //jedis.set("name", "lizai");
//        jedis.setex("namewithttl", 20,"lizai");
//        String val = jedis.get("name");
//        System.out.println(val);
        jedis.close();
    }

    /**
     * 使用连接池
     */
    @Test
    public void poolUse() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);

        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("date", "5/18");
        String val = jedis.get("name");
        System.out.println(val);
        jedis.close();
        jedisPool.close();
    }

    /**
     * 使用set
     */
    @Test
    public void redis_SET(){

    }


}
