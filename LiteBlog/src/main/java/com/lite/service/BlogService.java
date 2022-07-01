package com.lite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lite.entity.Blog;


public interface BlogService extends IService<Blog> {
    Long countReads(Long blogId);
    Long countLikes(Long blogId);
}
