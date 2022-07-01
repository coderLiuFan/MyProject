package com.lite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lite.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
    @Select("select count(*) from reader where bid = #{blogId}")
    Long countReads(Long blogId);
    @Select("select count(*) from likes where bid = #{blogId}")
    Long countLikes(Long blogId);
}
