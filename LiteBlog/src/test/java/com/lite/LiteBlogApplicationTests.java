package com.lite;

import com.lite.entity.Blog;
import com.lite.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LiteBlogApplicationTests {
    @Autowired
    private BlogService blogService;
    @Test
    void testGetBlogsByAuthorId() {
        List<Blog> blogs = blogService.getBlogsByAuthorId(1L);
        for (Blog blog : blogs) {
            System.out.println(blog);
        }
    }

    void testGetAllCategory(){

    }


}
