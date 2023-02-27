package com.itheima.yupinxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.yupinxuan.common.CustomException;
import com.itheima.yupinxuan.common.R;
import com.itheima.yupinxuan.entity.Category;
import com.itheima.yupinxuan.entity.Dish;
import com.itheima.yupinxuan.entity.Setmeal;
import com.itheima.yupinxuan.mapper.CategoryMapper;
import com.itheima.yupinxuan.service.CategoryService;
import com.itheima.yupinxuan.service.DishService;
import com.itheima.yupinxuan.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        int x= dishService.count(queryWrapper);
        if (x>0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,id);
        int y= setmealService.count(queryWrapper1);
        if (y>0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
