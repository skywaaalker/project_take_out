package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.DishDto;
import com.example.entity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，同时操作两张表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查菜品信息，以及对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //修改菜品，同时插入菜品对应的口味数据，同时操作两张表
    public void updateWithFlavor(DishDto dishDto);

    //删除
    public void deleteWithFlavorBatch(String[] ids);

    //更新起售状态
    public void updateStatus(Integer status, String[] ids);
}
