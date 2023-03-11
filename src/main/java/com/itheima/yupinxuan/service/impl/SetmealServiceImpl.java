package com.itheima.yupinxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.yupinxuan.common.CustomException;
import com.itheima.yupinxuan.dto.SetmealDto;
import com.itheima.yupinxuan.entity.Setmeal;
import com.itheima.yupinxuan.entity.SetmealDish;
import com.itheima.yupinxuan.mapper.SetmealMapper;
import com.itheima.yupinxuan.service.SetmealDishService;
import com.itheima.yupinxuan.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时保存套餐和柴品的关联关系
     * @param setmealDto
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        //保存套餐的基本信息
        // 用stream流处理给SetmealId
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes().stream().map(item-> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        //sql含义：select count(*) from setmeal where id in(1,2,3) and status=1
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        //查询套餐状态，确定是否可以删除
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        //如果不能删除，抛出一个业务异常
        if (count>0){
            throw  new CustomException("套餐正在售卖中,不能删除");
        }
        //如果可以删除，先删除套餐表中数据---setmeal
        this.remove(queryWrapper);
        //再删除SetmealDish
        LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper<>();
        //delete from setmeal_dish where setmeal_id in(i,2,3)
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);
    }
}
