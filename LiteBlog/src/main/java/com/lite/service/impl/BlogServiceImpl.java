package com.lite.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.entity.Blog;
import com.lite.mapper.BlogMapper;
import com.lite.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Override
    public List<Blog> getBlogsByAuthorId(Long authorId) {
        List<Blog> blogs = blogMapper.selectAllByBlogAuthorId(authorId);
        return blogs;
    }
}
