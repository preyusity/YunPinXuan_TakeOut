package com.itheima.yupinxuan.dto;


import com.itheima.yupinxuan.entity.Dish;
import com.itheima.yupinxuan.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//导入DTO用于封装页面提交的数据,拓展了原来实体类没有的属性
@Data
public class DishDto extends Dish {
    //以数组的形式存入key和value，接受页面传回来的json格式的口味参数。
    //因为有多条口味相关的数据所以用LIST集合进行接收

    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
