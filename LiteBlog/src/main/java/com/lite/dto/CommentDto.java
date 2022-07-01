package com.lite.dto;

import com.lite.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论的封装类，出评论实体类所具有的属性外，还包括了该条评论作者的部分信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto extends Comment {
    private String authorName;
    private String authorProfile;
}
