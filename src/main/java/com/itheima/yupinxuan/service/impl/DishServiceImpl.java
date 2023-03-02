package com.itheima.yupinxuan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.yupinxuan.dto.DishDto;
import com.itheima.yupinxuan.entity.Dish;
import com.itheima.yupinxuan.entity.DishFlavor;
import com.itheima.yupinxuan.mapper.DishMapper;
import com.itheima.yupinxuan.service.DishFlavorService;
import com.itheima.yupinxuan.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    //这里需要传入DishFlavorService
    @Autowired
    private DishFlavorService dishFlavorService;

    /**新增菜品，同时保存对应的口味数据
     @param dishDto
     @return
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        //获取菜品ID
        Long dishId = dishDto.getId();
        //封装菜品口味到List
        List<DishFlavor> flavors = dishDto.getFlavors();
        //把flavors重新赋值
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

    /**根据id查询菜品信息和对应的口味信息
     @param id
     @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        //查询菜品基本信息，从dish表查询
        Dish byId = this.getById(id);
        //进行对象的拷贝
        DishDto dishDto=new DishDto();

        BeanUtils.copyProperties(byId,dishDto);
        //查询菜品口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,byId.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        //dishDto set口味
        dishDto.setFlavors(list);
        //return
        return dishDto;
    }

    /**修改菜品
     @param dishDto
     @return
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应的口味数据dish_flaver表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //处理添加当前提交过来的口味数据。加上dishDto.Id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);

        //批量保存
    }
}
