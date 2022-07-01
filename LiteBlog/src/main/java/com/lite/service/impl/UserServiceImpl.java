package com.lite.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.entity.User;
import com.lite.mapper.UserMapper;
import com.lite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Long> getAllLikesBlogIdByUserId(Long userId) {
        return userMapper.findAllLikesBlogIdByUserId(userId);
    }

    @Override
    public List<Long> getAllFavoritesBlogIdByUserId(Long userId) {
        return userMapper.findAllFavoritesBlogIdByUserId(userId);
    }

    @Override
    public List<Long> getAllHistoryBlogIdByUserId(Long userId) {
        return userMapper.findAllHistoryBlogIdByUserId(userId);
    }

    @Override
    public void saveLikesBlog(Long userId, Long blogId, LocalDateTime createTime) {
        userMapper.addLikesBlog(userId,blogId,createTime);
    }

    @Override
    public void saveFavoritesBlog(Long userId, Long blogId, LocalDateTime createTime) {
        userMapper.addFavoritesBlog(userId,blogId,createTime);
    }

    @Override
    public void saveHistoryBlog(Long userId, Long blogId, LocalDateTime createTime) {
        userMapper.addHistoryBlog(userId,blogId,createTime);
    }

    @Override
    public void updateHistoryBlog(Long userId, Long blogId, LocalDateTime createTime) {
        userMapper.updateHistoryBlog(userId,blogId,createTime);
    }
}
