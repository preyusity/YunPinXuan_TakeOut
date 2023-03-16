package com.itheima.yupinxuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.yupinxuan.entity.User;
import com.itheima.yupinxuan.mapper.UserMapper;
import com.itheima.yupinxuan.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
