package com.lite.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录验证的过滤器
 * 登录验证的时候login.jsp的css样式也没有加载出来，因为login.css也是内部资源，被拦截掉所以拒绝访问
 * 所以所有跟登录相关的资源要放行，跟注册相关的资源也要放行！
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("filter init...");
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        log.info("do filter...");
        // 获取uri
        String uri = request.getRequestURI();
        log.info("拦截到请求:{}",uri);

        // 定义放行的请求路径
        String[] urls = new String[]{
                "/api/login.js",
                "/users/login",
                "/plugins/",
                "/styles/",
                "/js/",
                "/login/",
                "/register/",
                "/users/logout"
        };

        // 循环判断
        for (String u : urls) {
            if (uri.contains(u)) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        // 1. 判断session中是否有user

        HttpSession session = request.getSession();
        Object userId = session.getAttribute("userId");
        log.info("查询到当前用户id为{}", userId);

        // 2. 判断user是否为null
        if (userId != null) {
            // 用户登录过了，允许放行
            // 放行
            chain.doFilter(servletRequest, servletResponse);
        } else {
            // 用户没有登陆
            // 拦截下来，跳转到登录页面，存放提示信息
            request.setAttribute("login_msg", "您尚未登录！");
            response.sendRedirect("/templates/page/login/login.html");
            // request.getRequestDispatcher("/templates/page/login/login.html").forward(servletRequest, servletResponse);
        }


    }

    @Override
    public void destroy() {
        log.info("filter destroy...");
        Filter.super.destroy();
    }
}
