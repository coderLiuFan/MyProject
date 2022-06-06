package com.lite.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.entity.Comment;
import com.lite.mapper.CommentMapper;
import com.lite.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
