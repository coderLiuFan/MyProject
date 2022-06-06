package com.lite.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
@Data
public class Comment {
    @TableId
    private Long commentId;
    private Date commentCreateTime;
    private Long commentAuthorId;
    private Long commentBlogId;
    private String commentContent;
}
