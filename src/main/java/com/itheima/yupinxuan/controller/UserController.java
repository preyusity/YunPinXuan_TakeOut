package com.itheima.yupinxuan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.yupinxuan.common.R;
import com.itheima.yupinxuan.entity.User;
import com.itheima.yupinxuan.service.UserService;
import com.itheima.yupinxuan.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;
    /**
     *  发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号并且判空
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            //生成四位验证码
            String s = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",s);
            //需要将生成的验证码保存到Session
            session.setAttribute(phone,s);
            return R.success("手机验证码发送成功");
        }
        return R.error("手机验证码发送失败");

    }
    /**
     *  移动端用户登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取验证码
        String code1 = session.getAttribute(phone).toString();
        //进行验证码的比对
        if (code1!=null&&code1.equals(code)){
            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                session.setAttribute("user",user.getId());
                return R.success(user);

            }
        }
        return R.error("登录失败");

    }

}
