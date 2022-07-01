package com.lite;

import com.lite.entity.Blog;
import com.lite.entity.User;
import com.lite.mapper.BlogMapper;
import com.lite.mapper.CategoryMapper;
import com.lite.mapper.CommentMapper;
import com.lite.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMapper {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Test
    public void testSelectList(){
        List<User> users = userMapper.selectList(null);
        List<Blog> blogs = blogMapper.selectList(null);

        for (Blog blog : blogs) {
            System.out.println(blog);
        }
    }

    @Test
    public void testGetFavorites(){
        List<Long> allFavoritesBlogIdByUserId = userMapper.findAllFavoritesBlogIdByUserId(1L);
        for (Long aLong : allFavoritesBlogIdByUserId) {
            System.out.println(aLong);
        }
    }
}
