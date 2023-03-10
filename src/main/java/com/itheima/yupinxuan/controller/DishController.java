package com.itheima.yupinxuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.yupinxuan.common.R;
import com.itheima.yupinxuan.dto.DishDto;
import com.itheima.yupinxuan.entity.Category;
import com.itheima.yupinxuan.entity.Dish;
import com.itheima.yupinxuan.service.CategoryService;
import com.itheima.yupinxuan.service.DishFlavorService;
import com.itheima.yupinxuan.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//菜品管理
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**新增菜品
     @param dishDto
     @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器对象
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件使用like进行模糊查询
        queryWrapper.like(name!=null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //进行对象拷贝,忽略Recrods元素
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<DishDto> list=pageInfo.getRecords().stream().map((item->{
            DishDto dishDto=new DishDto();
            //拿到Records元素进行从Dish中拷贝到DishDto中
            BeanUtils.copyProperties(item,dishDto);
            //每个菜品对应的分类id
            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            //如果查到不为空传入dishDto
            if (category!=null){
                String name1 = category.getName();
                dishDto.setCategoryName(name1);
            }
            return dishDto;
        })).collect(Collectors.toList());



        //把处理过的recodes传入dishDtoPage
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**根据id查询菜品信息和对应的口味信息
     @param id
     @return
     */
    @GetMapping("{id}")
    private R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**修改菜品
     @param dishDto
     @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    /**根据条件查询对应的菜品数据
     @param dish
     @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);

    }

}