package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userCache")
@Slf4j
public class TestCacheController {

    @Autowired
    private UserService userService;
    @Autowired
    private CacheManager cacheManager;

    @CachePut(value = "userCache", key = "#user.id")
    @PostMapping
    public User save(User user) {
        userService.save(user);
        return user;
    }

    @CacheEvict(value = "userCache", key = "#id")
    //或者@CacheEvict(value = "userCache", key = "#p0")
    @DeleteMapping("/{id}")
    public void deleteCache(@PathVariable Long id) {
        userService.removeById(id);
    }

    @CacheEvict(value = "userCache", key = "#user.id")
    @PutMapping
    public User update(User user){
        userService.updateById(user);
        return user;
    }

    @Cacheable(value = "userCache", key = "#id", unless = "#result == null")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return user;
    }
}
