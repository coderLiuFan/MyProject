package com.lite.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 板块实体类
 */
@Data
public class Category {
    @TableId
    private Long categoryId;
    private String categoryName;
    private String categoryCover;
    private String categorySubUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill= FieldFill.INSERT)
    private Date createTime;
    private Long categoryAuthorId;
}
