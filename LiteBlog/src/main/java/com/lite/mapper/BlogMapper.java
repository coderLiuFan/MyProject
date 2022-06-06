package com.lite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lite.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    @Select("SELECT *, count(*) countViews, c.category_name blogCategoryName FROM blog b, reader r, category c WHERE b.blog_id = r.bid and b.blog_category_id = c.category_id AND b.blog_author_id = #{authorId} GROUP BY blog_id")
    List<Blog> selectAllByBlogAuthorId(Long authorId);
}
