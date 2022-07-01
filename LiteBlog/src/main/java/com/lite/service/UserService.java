package com.lite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lite.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService extends IService<User> {
    List<Long> getAllLikesBlogIdByUserId(Long userId);
    List<Long> getAllFavoritesBlogIdByUserId(Long userId);
    List<Long> getAllHistoryBlogIdByUserId(Long userId);
    void saveLikesBlog(Long userId,Long blogId, LocalDateTime createTime);
    void saveFavoritesBlog(Long userId,Long blogId, LocalDateTime createTime);
    void saveHistoryBlog(Long userId,Long blogId, LocalDateTime createTime);
    void updateHistoryBlog(Long userId,Long blogId, LocalDateTime createTime);
}
