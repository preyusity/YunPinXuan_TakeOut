package com.itheima.yupinxuan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.yupinxuan.Employee.Employee;
import com.itheima.yupinxuan.mapper.EmployeeMapper;
import com.itheima.yupinxuan.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
