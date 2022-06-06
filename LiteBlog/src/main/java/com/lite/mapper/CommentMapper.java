package com.lite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lite.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
