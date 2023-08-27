package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import redis.clients.jedis.Jedis;

@SpringBootTest
class ProjectTakeOutApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testString() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("city", "beijing");
    }


    @Test
    void contextLoads() {
        //获取链接
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("username", "xiaoming");
        jedis.close();
    }

}
