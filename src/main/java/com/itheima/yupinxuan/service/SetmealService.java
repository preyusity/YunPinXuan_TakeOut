package com.itheima.yupinxuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.yupinxuan.dto.SetmealDto;
import com.itheima.yupinxuan.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
    * 新增套餐，同时保存套餐和柴品的关联关系saveWithDish
     * @param setmealDto
    */
    public void saveWithDish(SetmealDto setmealDto);

    /** removeWithDish
     *  删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

}
