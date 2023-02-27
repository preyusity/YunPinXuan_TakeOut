package com.itheima.yupinxuan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.yupinxuan.common.R;
import com.itheima.yupinxuan.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
