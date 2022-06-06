package com.lite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lite.common.R;
import com.lite.dto.BlogDto;
import com.lite.entity.Blog;
import com.lite.entity.Category;
import com.lite.entity.User;
import com.lite.service.BlogService;
import com.lite.service.CategoryService;
import com.lite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * 获取所有带有作者名字、板块名称的博客
     * 用于首页展示所有博客（active-blog.html）
     *
     * @return
     */
    @GetMapping("/getAllDto")
    public R<List<BlogDto>> getDto() {
        log.info("获取所有带作者信息的博客");
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(null);

        return R.success(blogDtoList);
    }

    /**
     * 通过博客id查询具体博客信息
     * 用于单篇博客的展示（blog-detail.html）
     * 用于编辑博客时回显数据（blog-write.html）
     *
     * @param blogId 路径参数
     * @return
     */
    @RequestMapping(value = "/{blogId}", method = RequestMethod.GET)
    public R<BlogDto> getById(@PathVariable Long blogId) {
        log.info("查询博客，id={}", blogId);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogId, blogId);
        Blog blog = blogService.getOne(queryWrapper);
        LambdaQueryWrapper<Category> queryCategory = new LambdaQueryWrapper<>();
        queryCategory.eq(Category::getCategoryId, blog.getBlogCategoryId());
        Category category = categoryService.getOne(queryCategory);
        LambdaQueryWrapper<User> queryUser = new LambdaQueryWrapper<>();
        queryUser.eq(User::getUserId, blog.getBlogAuthorId());
        User user = userService.getOne(queryUser);

        BlogDto blogDto = new BlogDto();
        BeanUtils.copyProperties(blog, blogDto);
        blogDto.setAuthorName(user.getNickname());
        blogDto.setCategoryName(category.getCategoryName());
        return R.success(blogDto);
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        // 分页构造器
        Page<Blog> pageInfo = new Page<>();
        // 条件构造器
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件，进行sort排序
        queryWrapper.orderByDesc(Blog::getBlogCreateTime);
        // 进行分页查询
        blogService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 通过session域中存储的用户id查询用户所写的所有博客，包含了作者和板块信息
     * 用于创作者中心展示作者的博客列表（author.html）
     * @param request session域中存储的userId
     * @return
     */
    @GetMapping("/getByAuthorId")
    public R<List<BlogDto>> getByAuthorId(HttpServletRequest request) {
        log.info("查询当前作者博客");
        Long userId = (Long) request.getSession().getAttribute("userId");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogAuthorId, userId);
        queryWrapper.orderByDesc(Blog::getBlogCreateTime);

        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        return R.success(blogDtoList);
    }

    /**
     *
     * @param userId
     * @return
     */
    @GetMapping("/getByUserId/{userId}")
    public R<List<BlogDto>> getByUserId(@PathVariable Long userId){
        log.info("查询用户id={}的博客",userId);
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogAuthorId,userId);
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        if (blogDtoList != null){
            return R.success(blogDtoList);
        }
        return R.error("没有查询到该用户的博客");
    }



    /**
     * 通过板块id查询当前板块下所有的博客，包含了作者和板块信息
     * 用于板块分类展示（active-blog.html）
     *
     * @param categoryId 路径参数
     * @return
     */
    @GetMapping("/getByCategoryId/{categoryId}")
    public R<List<BlogDto>> getByCategoryId(@PathVariable Long categoryId) {
        log.info("开始查询当前板块对应的博客");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogCategoryId, categoryId);
        queryWrapper.orderByDesc(Blog::getBlogCreateTime);
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        return R.success(blogDtoList);
    }

    /**
     * 通过关键词查找相应的博客，包含了作者和板块信息
     * 用于首页博客搜索功能（header.html）
     *
     * @param keyWord 用户输入
     * @return
     */
    @GetMapping("/getByKeyWord/{keyWord}")
    public R<List<BlogDto>> getByKeyWord(@PathVariable String keyWord) {
        log.info("开始根据关键字查找博客");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(keyWord != null, Blog::getBlogTitle, keyWord).or().like(keyWord != null, Blog::getBlogContent, keyWord);
        List<BlogDto> blogDtoList = getBlogDtoListByCondition(queryWrapper);
        return R.success(blogDtoList);
    }

    @DeleteMapping("/{blogId}")
    public R<String> delete(@PathVariable Long blogId) {
        log.info("开始删除博客");
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Blog::getBlogId, blogId);
        boolean flag = blogService.remove(queryWrapper);
        if (flag) {
            return R.success("删除成功");
        }
        return R.error("错误");
    }

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
        return R.error("操作失败");
    }

    @PutMapping
    public R<String> update(@RequestBody Blog blog) {
        Long blogId = blog.getBlogId();
        log.info("修改博客，id={}", blogId);
        boolean flag = blogService.updateById(blog);
        if (flag) {
            return R.success("博客修改成功");
        }
        return R.error("操作失败");
    }

    public List<BlogDto> getBlogDtoListByCondition(LambdaQueryWrapper<Blog> queryBlog) {
        // 获取到所有博客实体对象
        List<Blog> blogList = blogService.list(queryBlog);
        return blogList.stream().map((item) -> {
            BlogDto blogDto = new BlogDto();
            System.out.println(item);
            BeanUtils.copyProperties(item, blogDto);
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            LambdaQueryWrapper<Category> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getUserId, item.getBlogAuthorId());
            queryWrapper2.eq(Category::getCategoryId, item.getBlogCategoryId());
            User user = userService.getOne(queryWrapper1);
            Category category = categoryService.getOne(queryWrapper2);
            blogDto.setAuthorName(user.getNickname());
            blogDto.setCategoryName(category.getCategoryName());
            return blogDto;
        }).collect(Collectors.toList());
    }

//    public BlogDto getBlogDtoByCondition(LambdaQueryWrapper<Blog> queryWrapper) {
//        Blog blog = blogService.getOne(queryWrapper);
//        Long authorId = blog.getBlogAuthorId();
//        String nickname = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUserId, authorId)).getNickname();
//        Long categoryId = blog.getBlogCategoryId();
//        String categoryName = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryId, categoryId)).getCategoryName();
//        BlogDto blogDto = new BlogDto(blog, nickname, categoryName);
//        return blogDto;
//    }
}
