package com.example.dto;

import com.example.entity.Dish;
import com.example.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    //对应菜品名称
    private String categoryName;

    //
    private Integer copies;

}
