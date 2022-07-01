package com.lite.dto;

import com.lite.entity.Blog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客的封装类，除博客实体类所有的属性外，还包括了该篇博客作者的昵称、所属板块的名称
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogDto extends Blog{
    private String authorName;
    private String categoryName;
    private Long countReads;
    private Long countLikes;
}
