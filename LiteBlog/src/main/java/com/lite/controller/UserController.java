package com.lite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lite.common.R;
import com.lite.entity.User;
import com.lite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        //1，将页面提交的密码进行md5加密处理
        String username = user.getUsername();
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2，根据页面提交的用户名username查询数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username", username);
        User u = userService.getOne(queryWrapper);
        //3，如果没有查询到则返回登录失败结果
        if (u == null) {
            return R.error("用户名不存在");
        }
        //4，密码比对，如果不一致则返回登录失败结果
        if (!u.getPassword().equals(password)) {
            return R.error("密码错误");
        }
        //6，登录成功，将id存入session并返回登录结果
        request.getSession().setAttribute("userId", u.getUserId());
        Object userId = request.getSession().getAttribute("userId");
        log.info("用户id" + userId.toString() + "已存储到session中");
        return R.success(u);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理session中保存的用户id
        request.getSession().removeAttribute("userId");
        log.info("已清除用户id");
        return R.success("退出成功");
    }

//    @GetMapping("/getByAuthorId/{authorId}")
//    public R<User> getByAuthorId(@PathVariable Long authorId){
//        LambdaQueryWrapper<User> queryWrapper =
//
//    }
    @GetMapping("/{userId}")
    public R<User> getById(@PathVariable Long userId){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,userId);
        User user = userService.getOne(queryWrapper);
        return R.success(user);
    }

    @GetMapping("/getMyPage")
    public R<User> getById(HttpServletRequest request){
        log.info("获取");
        Long userId = (Long)request.getSession().getAttribute("userId");
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId,userId);
        User user = userService.getOne(queryWrapper);
        return R.success(user);
    }
}
