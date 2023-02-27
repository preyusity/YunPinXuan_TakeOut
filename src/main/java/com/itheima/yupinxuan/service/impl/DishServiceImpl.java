package com.itheima.yupinxuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.yupinxuan.entity.Dish;
import com.itheima.yupinxuan.mapper.DishMapper;
import com.itheima.yupinxuan.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
