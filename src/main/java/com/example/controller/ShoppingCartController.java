package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.BaseContext;
import com.example.common.R;
import com.example.entity.ShoppingCart;
import com.example.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        // 插入userID
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 对于相同套餐或者菜品，插入不需要重新加入一条新的记录 需要用户id以及名称，口味都相同
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        queryWrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
        queryWrapper.eq(shoppingCart.getDishId()!=null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingCartRes = shoppingCartService.getOne(queryWrapper);

        if(shoppingCartRes != null) {
            shoppingCartRes.setNumber(shoppingCartRes.getNumber() + 1);
            shoppingCartService.updateById(shoppingCartRes);
            log.info("现在的购物车数据：{}", shoppingCartRes);
            return R.success(shoppingCartRes);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            log.info("现在的购物车数据：{}", shoppingCart);
            return R.success(shoppingCart);
        }
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        return R.success(shoppingCartService.list(queryWrapper));
    }

    @DeleteMapping("/clean")
    public R<String> deleteAll() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(queryWrapper);
        return R.success(("清空购物车成功"));
    }

    @PostMapping("/sub")
    public R<String> subNumber(@RequestBody ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        log.info("需要减少数量的菜品或套餐id{}, {}", dishId, setmealId);
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(dishId!=null, ShoppingCart::getDishId, dishId);
        queryWrapper.eq(setmealId!=null, ShoppingCart::getSetmealId, setmealId);
        ShoppingCart shoppingCartRes = shoppingCartService.getOne(queryWrapper);
        if(shoppingCartRes.getNumber() == 1) {
            shoppingCartService.removeById(shoppingCartRes);
        } else {
            shoppingCartRes.setNumber(shoppingCartRes.getNumber() - 1);
            shoppingCartService.updateById(shoppingCartRes);
        }
        return R.success("减少数量成功");
    }

}
