package com.lite.dto;

import com.lite.entity.Blog;
import com.lite.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto extends Blog{

    private String authorName;
    private String categoryName;


}
