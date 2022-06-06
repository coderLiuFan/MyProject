package com.lite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lite.entity.Blog;

import java.util.List;

public interface BlogService extends IService<Blog> {
    List<Blog> getBlogsByAuthorId(Long authorId);
}
