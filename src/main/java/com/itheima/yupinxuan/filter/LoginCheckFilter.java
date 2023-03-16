package com.itheima.yupinxuan.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.yupinxuan.common.BaseContext;
import com.itheima.yupinxuan.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否已经完成登录
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns="/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);
        //定义不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //2、判断本次请求是否需要处理（check检查路径是否在上面配置通配符路径里面）
        boolean check = check(urls, requestURI);
        //3、如果不需要处理，则直接放行
        if (check){log.info(
                "本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //4-1判断登录状态，如果已登录，
        if (request.getSession().getAttribute("employee")!=null){
            Long x= (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrectId(x);
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
        //4-2、判断移动端用户登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户已登录，ID为：{}",request.getSession().getAttribute("user"));


            //调用封装在BaseContext中的线程设置ID的方法
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrectId(userId);

            filterChain.doFilter(request,response);
            return;
        }
        //5、如果未登录则返回未登录结果,通过输出流的方式向客户端页面响应数据
            log.info("用户未登录");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;


    }
    //（方法）路径匹配检查本次请求是否需要放行
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match=PATH_MATCHER.match(url,requestURI);
            if (match==true){
                return true;
            }
        }
        return false;
    }
}
