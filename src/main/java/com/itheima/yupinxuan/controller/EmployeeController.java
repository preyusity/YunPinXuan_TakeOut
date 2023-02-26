package com.itheima.yupinxuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.yupinxuan.Employee.Employee;
import com.itheima.yupinxuan.common.R;
import com.itheima.yupinxuan.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login (HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码password进行md5加密处理
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp=employeeService.getOne(queryWrapper);
        //3.如果没有查询到则返回登录失败的结果
        if (emp==null){
            return R.error("登录失败");
        }
        //4.密码对比，如果不一样返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果,数据库中0表示的是禁用1表示的是可用
        if (emp.getStatus()==0){
            return R.error("用户被禁用");
        }
        //6.登录成功，将员工id存入session并返回成功结果
        else {
            request.getSession().setAttribute("employee",emp.getId());
            return R.success(emp);
        }
    }

    /**员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**新增员工
     *@param employee
    * @param request
     *@return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        //设置初始密码123456，对密码进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //createTime updateTime
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 获取当前登录用户的id//createUser updateUser
        Long emp = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(emp);
        employee.setUpdateUser(emp);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //添加过滤条件
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //执行查询
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
        }
}
