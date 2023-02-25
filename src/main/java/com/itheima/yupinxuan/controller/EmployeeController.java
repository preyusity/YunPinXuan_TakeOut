package com.itheima.yupinxuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.yupinxuan.Employee.Employee;
import com.itheima.yupinxuan.common.R;
import com.itheima.yupinxuan.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**员工登录
    @param request
    @param employee
    @return
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
    private R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
