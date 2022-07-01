package com.lite.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.entity.Blog;
import com.lite.mapper.BlogMapper;
import com.lite.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Long countReads(Long blogId) {
        return blogMapper.countReads(blogId);
    }

    @Override
    public Long countLikes(Long blogId) {
        return blogMapper.countLikes(blogId);
    }
}
