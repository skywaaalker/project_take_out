package com.example;

import com.example.entity.User;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import redis.clients.jedis.Jedis;

@SpringBootTest
class ProjectTakeOutApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

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

    @Test
    @CachePut(value = "userCache", key = "#user.id")
    public User save() {
        User user = new User();
        user.setName("haha");
        userService.save(user);
        return user;
    }
}
