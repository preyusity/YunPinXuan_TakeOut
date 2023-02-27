package com.itheima.yupinxuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.yupinxuan.entity.Setmeal;
import com.itheima.yupinxuan.mapper.SetmealMapper;
import com.itheima.yupinxuan.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
