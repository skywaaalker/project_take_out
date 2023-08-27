package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.SetmealDto;
import com.example.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {


    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

    public void updateStatus(Integer status, List<Long> ids);
}
