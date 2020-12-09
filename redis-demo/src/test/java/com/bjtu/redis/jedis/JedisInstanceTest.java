package com.bjtu.redis.jedis;

import org.junit.Test;

import com.bjtu.redis.JedisInstance;

import redis.clients.jedis.Jedis;

public class JedisInstanceTest {

    /**
     * 基本使用
     */
    @Test
    public void test() {
        JedisDemo demo =new JedisDemo();
        demo.basicUse();
    }

}
