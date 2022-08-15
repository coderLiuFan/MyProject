package com.lite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lite.common.R;
import com.lite.dto.BlogDto;
import com.lite.entity.Blog;
import com.lite.entity.Category;
import com.lite.entity.Comment;
import com.lite.entity.User;
import com.lite.service.BlogService;
import com.lite.service.CategoryService;
import com.lite.service.CommentService;
import com.lite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

    /**
     * 获取所有带有作者名字、板块名称的博客
     * 用于首页展示所有博客（active-blog.html）
     *
     * @return blogDto的封装类集合
     */
    @GetMapping("/getAllDto")
    public R<List<BlogDto>> getAllDto() {
        log.info("获取所有带作者信息的博客");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogAccess, 1).eq(Blog::getBlogStatus, 1);
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);

        return R.success(blogDtoList);
    }

    /**
     * 通过博客id查询具体博客信息
     * 用于单篇博客的展示（blog-detail.html）
     * 用于编辑博客时回显数据（blog-write.html）
     *
     * @param blogId 路径参数
     * @return blogDto的封装类
     */
    @RequestMapping(value = "/{blogId}", method = RequestMethod.GET)
    public R<BlogDto> getById(@PathVariable Long blogId) {
        log.info("查询博客，id={}", blogId);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogId, blogId);
        BlogDto blogDto = getBlogDtoByCondition(queryWrapper);
        return R.success(blogDto);
    }

    /**
     * 通过session域中存储的用户id查询用户所写的所有博客，包含了作者和板块信息
     * 用于创作者中心展示作者的博客列表（author.html）
     *
     * @param request session域中存储的userId
     * @return blogDto的封装类集合
     */
    @GetMapping("/getByAuthorId")
    public R<List<BlogDto>> getByAuthorId(HttpServletRequest request) {
        log.info("查询当前作者博客");
        Long userId = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogAuthorId, userId)
                .orderByDesc(Blog::getCreateTime);

        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        return R.success(blogDtoList);
    }

    /**
     * 通过用户id查找与其对应的博客
     *
     * @param userId 路径参数，用户id
     * @return blogDto的封装类集合
     */
    @GetMapping("/getByUserId/{userId}")
    public R<List<BlogDto>> getByUserId(@PathVariable Long userId) {
        log.info("查询用户id={}的博客", userId);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogAuthorId, userId).eq(Blog::getBlogAccess, 1).eq(Blog::getBlogStatus, 1);
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        if (blogDtoList != null) {
            return R.success(blogDtoList);
        }
        return R.error("没有查询到该用户的博客");
    }


    /**
     * 通过板块id查询当前板块下所有的博客，包含了作者和板块信息
     * 用于板块分类展示（active-blog.html）
     *
     * @param categoryId 路径参数
     * @return blogDto的封装类集合
     */
    @GetMapping("/getByCategoryId/{categoryId}")
    public R<List<BlogDto>> getByCategoryId(@PathVariable Long categoryId) {
        log.info("开始查询当前板块对应的博客");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogCategoryId, categoryId).eq(Blog::getBlogAccess, 1).eq(Blog::getBlogStatus, 1)
                .orderByDesc(Blog::getCreateTime);
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        return R.success(blogDtoList);
    }

    /**
     * 通过关键词查找相应的博客，包含了作者和板块信息
     * 用于首页博客搜索功能（header.html）
     *
     * @param keyWord 用户输入
     * @return blogDto的封装类集合
     */
    @GetMapping("/getByKeyWord/{keyWord}")
    public R<List<BlogDto>> getByKeyWord(@PathVariable String keyWord) {
        log.info("开始根据关键字查找博客");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(keyWord != null, Blog::getBlogTitle, keyWord).or().like(keyWord != null, Blog::getBlogContent, keyWord).eq(Blog::getBlogAccess, 1).eq(Blog::getBlogStatus, 1);
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        return R.success(blogDtoList);
    }

    /**
     * 查询用户点赞的博客
     * @param userId
     * @return
     */
    @GetMapping("/getLikesByUserId/{userId}")
    public R<List<BlogDto>> getLikesByUserId(@PathVariable Long userId) {
        List<BlogDto> blogDtoList = getBlogDtoListByBlogIdList(userService.getAllLikesBlogIdByUserId(userId));
        if (blogDtoList != null) {
            return R.success(blogDtoList);
        }
        return R.error("当前用户没有点赞过任何博客");
    }

    /**
     * 查询用户收藏的博客
     * @param userId
     * @return
     */
    @GetMapping("/getFavoritesByUserId/{userId}")
    public R<List<BlogDto>> getFavoritesByUserId(@PathVariable Long userId) {
        List<BlogDto> blogDtoList = getBlogDtoListByBlogIdList(userService.getAllFavoritesBlogIdByUserId(userId));
        if (blogDtoList != null) {
            return R.success(blogDtoList);
        }
        return R.error("当前用户没有收藏任何博客");
    }

    /**
     * 查询用户看过的博客
     * @param userId
     * @return
     */
    @GetMapping("/getHistoryByUserId/{userId}")
    public R<List<BlogDto>> getHistoryByUserId(@PathVariable Long userId) {
        List<BlogDto> blogDtoList = getBlogDtoListByBlogIdList(userService.getAllHistoryBlogIdByUserId(userId));
        if (blogDtoList != null) {
            return R.success(blogDtoList);
        }
        return R.error("当前用户没有阅读过任何博客");
    }


    // 注入全局变量，项目根目录
    @Value("${liteblog.path}")
    private String basePath;

    // 注入全局变量，博客封面根目录
    @Value("${liteblog.blogCover.path}")
    private String blogCoverPath;

    /**
     * 上传博客封面
     * 用于新增/修改博客时上传封面功能（blog-write.html）
     *
     * @param file 参数名字要求必须为file，和浏览器FileData.name对应一致
     * @return 返回上传文件的文件名
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();// 获取原始文件名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));// 获取文件后缀
        String fileName = UUID.randomUUID().toString() + suffix;// 随机字符串（名称）+ 文件类型后缀
        String filePath = blogCoverPath + fileName;// 项目路径+文件名称
        try {
            // 将临时文件转存到指定位置
            file.transferTo(new File(basePath + filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 下载博客封面
     * 用于新增/修改博客时回显封面功能（blog-write.html）
     *
     * @param name     前端页面提交请求中携带的参数（文件名）
     * @param response 回写字节流，显示图片
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            log.info("开始下载文件");
            // 输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(basePath + blogCoverPath + name);
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

    /**
     * 删除博客上传封面但未保存时，阻塞的临时图片
     *
     * @param name 图片名称
     * @return 操作结果信息
     */
    @GetMapping("/deleteTempImage/{name}")
    public R<String> deleteTempImage(@PathVariable String name) {
        boolean delete = false;
        try {
            File file = new File(basePath + blogCoverPath + name);
            delete = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (delete){
            return R.success("已删除临时文件");
        }
        return R.error("操作出错了");
    }

    /**
     * 保存博客
     * 用于创作博客页面（blog-write.html）
     *
     * @param blog    博客实体类
     * @param request 用于获取session中存储的用户id
     * @return 操作结果信息
     */
    @PostMapping
    public R<String> save(Blog blog, HttpServletRequest request) {
        log.info("保存博客");
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId != null) {
            blog.setBlogAuthorId(userId);
            boolean flag = blogService.save(blog);
            if (flag) {
                return R.success("保存成功");
            }
        }
        return R.error("服务器开小差了，请稍后再试~");
    }

    /**
     * 根据博客id删除对应的博客
     * 用于创作者中心删除按钮（author.html）
     *
     * @param blogId 路径参数
     * @return 操作结果信息
     */
    @DeleteMapping("/{blogId}")
    public R<String> delete(@PathVariable Long blogId) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogId, blogId);
        Blog blog = blogService.getOne(queryWrapper);
        String blogCoverImage = blog.getBlogCoverImage();
        log.info("开始删除博客封面");
        if (blogCoverImage != null) {
            try {
                File file = new File(basePath + blogCoverPath + blogCoverImage);
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("开始删除博客对应的评论");
        LambdaQueryWrapper<Comment> queryComment = new LambdaQueryWrapper<>();
        queryComment.eq(Comment::getCommentBlogId, blog.getBlogId());
        commentService.remove(queryComment);
        log.info("开始删除博客");
        boolean flag = blogService.remove(queryWrapper);
        if (flag) {
            return R.success("删除成功");
        }
        return R.error("错误");
    }


    /**
     * 修改博客
     * 用于修改博客页面（blog-write.html）
     *
     * @param blog 博客实体类
     * @return 操作结果信息
     */
    @PutMapping
    public R<String> update(@RequestBody Blog blog) {
        // 新上传的封面
        Long blogId = blog.getBlogId();
        String blogCoverImage = blog.getBlogCoverImage();
        // 修改前的封面
        Blog originalBlog = blogService.getOne(new LambdaQueryWrapper<Blog>().eq(Blog::getBlogId, blogId));
        String originalBlogBlogCoverImage = originalBlog.getBlogCoverImage();
        log.info("开始上传封面");
        try {
            if (!originalBlogBlogCoverImage.equals(blogCoverImage)) {// 如果重新上传了新的封面，则将原来的封面图片删除
                File file = new File(basePath + blogCoverPath + originalBlog.getBlogCoverImage());
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("修改博客，id={}", blogId);
        boolean flag = blogService.updateById(blog);
        if (flag) {
            return R.success("博客修改成功");
        }
        return R.error("服务器开小差了，请稍后再试~");
    }

    /**
     * 获取博客列表的DTO封装类，在controller中调用
     *
     * @param queryBlog 查询博客列表的条件
     * @return blogDto的封装类集合
     */
    private List<BlogDto> getBlogDtoListByCondition(LambdaQueryWrapper<Blog> queryBlog) {
        // 获取到所有博客实体对象
        List<Blog> blogList = blogService.list(queryBlog);
        return blogList.stream().map((item) -> {
            BlogDto blogDto = new BlogDto();
            BeanUtils.copyProperties(item, blogDto);
            // 数据未过滤板块为空的博客，可能会导致空指针异常，影响正常功能，因此在这里需要捕获异常
            try {
                // 查询该篇博客作者的条件
                LambdaQueryWrapper<User> queryUser = new LambdaQueryWrapper<>();
                queryUser.eq(User::getUserId, item.getBlogAuthorId());
                // 将用户昵称封装进blogDto
                User user = userService.getOne(queryUser);
                blogDto.setAuthorName(user.getNickname());
                // 查询该篇博客所在板块的条件
                LambdaQueryWrapper<Category> queryCategory = new LambdaQueryWrapper<>();
                queryCategory.eq(Category::getCategoryId, item.getBlogCategoryId());
                // 将板块名称封装进blogDto
                Category category = categoryService.getOne(queryCategory);
                blogDto.setCategoryName(category.getCategoryName());
                // 将阅读人数和点赞人数封装进blogDto
                blogDto.setCountReads(blogService.countReads(item.getBlogId()));
                blogDto.setCountLikes(blogService.countLikes(item.getBlogId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return blogDto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取博客的DTO封装类，在controller中调用
     *
     * @param queryWrapper 查询单个博客的条件
     * @return blogDto的封装类
     */
    private BlogDto getBlogDtoByCondition(LambdaQueryWrapper<Blog> queryWrapper) {
        Blog blog = blogService.getOne(queryWrapper);
        BlogDto blogDto = new BlogDto();
        BeanUtils.copyProperties(blog, blogDto);
        // 数据未过滤板块为空的博客，可能会导致空指针异常，影响正常功能，因此在这里需要捕获异常
        try {
            LambdaQueryWrapper<User> queryUser = new LambdaQueryWrapper<>();
            queryUser.eq(User::getUserId, blog.getBlogAuthorId());
            User user = userService.getOne(queryUser);

            LambdaQueryWrapper<Category> queryCategory = new LambdaQueryWrapper<>();
            queryCategory.eq(Category::getCategoryId, blog.getBlogCategoryId());
            Category category = categoryService.getOne(queryCategory);

            blogDto.setAuthorName(user.getNickname());
            blogDto.setCategoryName(category.getCategoryName());
            // 将阅读人数和点赞人数封装进blogDto
            blogDto.setCountReads(blogService.countReads(blog.getBlogId()));
            blogDto.setCountLikes(blogService.countLikes(blog.getBlogId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blogDto;
    }

    /**
     * 采用for循环逐一将查询到用户收藏的博客按照点赞/收藏/阅读时间倒序排列，实现最近点赞/收藏/阅读的博客在前面
     * 在查询Controller中调用
     * @param blogIdList blogId的集合
     * @return 按照blog集合顺序排序的blogDto集合
     */
    private List<BlogDto> getBlogDtoListByBlogIdList(List<Long> blogIdList) {
        List<BlogDto> blogDtoList = new ArrayList<>();
        for (Long blogId : blogIdList) {
            LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Blog::getBlogId, blogId);
            blogDtoList.add(getBlogDtoByCondition(queryWrapper));
        }
        return blogDtoList;
    }
}
