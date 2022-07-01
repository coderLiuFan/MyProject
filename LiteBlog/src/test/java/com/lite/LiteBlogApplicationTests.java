package com.lite;

import com.lite.service.BlogService;
import com.lite.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class LiteBlogApplicationTests {
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;


    @Test
    void testGetFavoritesOrderByTime(){
        List<Long> list = userService.getAllFavoritesBlogIdByUserId(1L);
        for (Long aLong : list) {
            System.out.println(aLong);
        }
    }
}
