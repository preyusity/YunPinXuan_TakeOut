package com.itheima.yupinxuan.dto;

import com.itheima.yupinxuan.entity.Setmeal;
import com.itheima.yupinxuan.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
