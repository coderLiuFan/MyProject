package com.lite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lite.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select b.blog_id, l.create_time from likes l, blog b where l.bid = b.blog_id and l.likes_id = #{userId} ORDER BY create_time desc")
    List<Long> findAllLikesBlogIdByUserId(Long userId);

    @Select("select b.blog_id, f.create_time from favorites f, blog b where f.bid = b.blog_id and f.favorites_id = #{userId} ORDER BY create_time desc")
    List<Long> findAllFavoritesBlogIdByUserId(Long userId);

    @Select("select b.blog_id, r.create_time  from reader r, blog b where r.bid = b.blog_id and r.reader_id = #{userId} ORDER BY create_time desc" )
    List<Long> findAllHistoryBlogIdByUserId(Long userId);

    @Insert("insert into likes values(#{blogId},#{userId},#{createTime})")
    void addLikesBlog(Long userId,Long blogId, LocalDateTime createTime);

    @Insert("insert into favorites values(#{blogId},#{userId},#{createTime})")
    void addFavoritesBlog(Long userId,Long blogId, LocalDateTime createTime);

    @Insert("insert into reader values(#{blogId},#{userId},#{createTime})")
    void addHistoryBlog(Long userId,Long blogId, LocalDateTime createTime);

    @Update("update reader set create_time = #{createTime} where reader_id = #{userId} and bid = #{blogId}")
    void updateHistoryBlog(Long userId,Long blogId, LocalDateTime createTime);


}
