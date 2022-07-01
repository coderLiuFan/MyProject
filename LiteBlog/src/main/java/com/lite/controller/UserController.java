package com.lite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lite.common.R;
import com.lite.entity.User;
import com.lite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param request 用于获取session存放用户id
     * @param user    用户实体类对象，用于登录
     * @return 返回当前登录的用户实体类对象
     */
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        //1，将页面提交的密码进行md5加密处理
        String username = user.getUsername();
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2，根据页面提交的用户名username查询数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
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

    /**
     * 用户登出
     *
     * @param request 用于获取session移除存储的用户id
     * @return 操作结果信息
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理session中保存的用户id
        request.getSession().removeAttribute("userId");
        log.info("已清除用户id");
        return R.success("退出成功");
    }


    /**
     * 根据用户ID查找对应的用户信息
     * 用于个人主页信息展示（user.html）
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    public R<User> getById(@PathVariable Long userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, userId);
        User user = userService.getOne(queryWrapper);
        return R.success(user);
    }

    /**
     * 获取当前登录的用户信息
     *
     * @param request 容器Bean：HTTPServletRequest，用于获取session对象
     * @return 返回当前用户信息
     */
    @GetMapping("/getCurrentUser")
    public R<User> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserId, userId);
        User user = userService.getOne(queryWrapper);
        return R.success(user);
    }

    /**
     * 用户注册（register.html）验证用户名是否可用
     *
     * @param username 用户提交校验的用户名
     * @return 提示信息
     */
    @GetMapping("/checkExist/{username}")
    public R<String> checkExist(@PathVariable String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userService.getOne(queryWrapper);
        if (user == null) {
            return R.success("该用户名可注册");
        }
        return R.error("该用户名已被注册");
    }

    /**
     * 用户注册（register.html）
     *
     * @param user 注册数据
     * @return 成功则返回用户对象，失败则返回失败操作信息
     */
    @PostMapping
    public R<User> register(User user) {
        //1，将页面提交的密码进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);

        boolean flag = userService.save(user);
        if (flag) {
            return R.success(user);
        }
        return R.error("服务器开小差了，请稍后再试~");
    }

    /**
     * 用户修改个人信息（userInfo-edit.html）
     * 性别、生日等信息不可修改
     *
     * @param user 用户实体类对象
     * @return 操作结果信息
     */
    @PutMapping
    public R<String> update(@RequestBody User user) {
        // 新上传的头像
        Long userId = user.getUserId();
        String profile = user.getProfile();
        // 修改前的头像
        User originalUser = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserId, userId));
        String originalUserProfile = originalUser.getProfile();
        try {
            if (!originalUserProfile.equals(profile)) {// 如果重新上传了新头像，则将原来的头像删除
                File file = new File(basePath + userProfilePath + originalUser.getProfile());
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("修改用户信息，id={}", userId);
        boolean flag = userService.updateById(user);
        if (flag) {
            return R.success("用户信息修改成功");
        }
        return R.error("服务器开小差了，请稍后再试~");
    }


    // 静态路径，可在配置文件中修改value
    @Value("${liteblog.path}")
    private String basePath;
    @Value("${liteblog.userProfile.path}")
    private String userProfilePath;

    /**
     * 上传用户头像
     *
     * @param file 参数名字要求必须为file，和浏览器FileData.name对应一致
     * @return 返回上传文件的文件名
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));// 获取文件后缀
        String fileName = UUID.randomUUID().toString() + suffix;// 随机字符串（名称）+ 文件类型后缀
        String filePath = userProfilePath + fileName;// 项目路径+文件名称
        try {
            // 将临时文件转存到指定位置
            file.transferTo(new File(basePath + filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 下载用户头像
     *
     * @param name     前端页面提交请求中携带的参数（文件名）
     * @param response 回写字节流，显示图片
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            log.info("开始下载文件");
            // 输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(basePath + userProfilePath + name);
            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            // 释放资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/likes/{blogId}")
    public R<String> likes(@PathVariable Long blogId, HttpServletRequest request) {
        List<Long> blogIdList = userService.getAllLikesBlogIdByUserId((Long) request.getSession().getAttribute("userId"));
        if (blogIdList.contains(blogId)) {
            return R.error("您已经点赞过这篇文章了~");
        } else {
            userService.saveLikesBlog((Long) request.getSession().getAttribute("userId"), blogId, LocalDateTime.now());
            return R.success("点赞成功！");
        }
    }

    @PostMapping("/favorites/{blogId}")
    public R<String> favorites(@PathVariable Long blogId, HttpServletRequest request) {
        List<Long> blogIdList = userService.getAllFavoritesBlogIdByUserId((Long) request.getSession().getAttribute("userId"));
        if (blogIdList.contains(blogId)) {
            return R.error("您已经点赞过这篇文章了~");
        } else {
            userService.saveFavoritesBlog((Long) request.getSession().getAttribute("userId"), blogId, LocalDateTime.now());
            return R.success("感谢点赞！");
        }
    }
    @PostMapping("/reads/{blogId}")
    public R<String> reads(@PathVariable Long blogId, HttpServletRequest request) {
        List<Long> blogIdList = userService.getAllHistoryBlogIdByUserId((Long) request.getSession().getAttribute("userId"));
        if(blogIdList.contains(blogId)) {
            userService.updateHistoryBlog((Long) request.getSession().getAttribute("userId"), blogId, LocalDateTime.now());
            return R.success("更新阅读时间");
        } else {
            userService.saveHistoryBlog((Long) request.getSession().getAttribute("userId"), blogId, LocalDateTime.now());
            return R.success("添加阅读记录");
        }
    }

}
