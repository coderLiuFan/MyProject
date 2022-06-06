package com.lite.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


@Data
public class Blog {
    @TableId
    private Long blogId;
    private String blogTitle;
    private String blogSubUrl;
    private String blogCoverImage;
    // @TableField(typeHandler = FastjsonTypeHandler.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime blogCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill= FieldFill.INSERT)
    private LocalDateTime blogUpdateTime;
    private Integer blogStatus;
    private Integer blogAccess;
    private Long blogAuthorId;
    private Long blogCategoryId;
//    private String blogCategoryName;
    private String blogContent;
//
//    // 逻辑视图
//    private String blogStatusStr;
//    private String blogAccessStr;
//
//    public String getBlogStatusStr() {
//        if(this.blogStatus == 1){
//            return "已发布";
//        }
//        return "草稿";
//    }
//
//    public String getBlogAccessStr() {
//        if (this.blogAccess == 1) {
//            return "开放";
//        }
//        return "私密";
//    }
//
//    // 重写日期
//    private String blogCreateTimeStr;
//    private String blogUpdateTimeStr;
//
//    public void setBlogCreateTimeStr(String blogCreateTimeStr) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//        this.blogCreateTimeStr = sdf.format(this.blogCreateTime);
//    }
//
//    public void setBlogUpdateTimeStr(String blogUpdateTimeStr) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//        this.blogUpdateTimeStr = sdf.format(this.blogUpdateTime);
//    }
//
//    public String getBlogCreateTimeStr() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//        if (this.blogCreateTime != null) {
//            return sdf.format(this.blogCreateTime);
//        }
//        return null;
//    }
//
//    public String getBlogUpdateTimeStr() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//        if(this.blogUpdateTime != null){
//            return sdf.format(this.blogUpdateTime);
//        }
//        return null;
//    }
//
//    // 用来记录浏览量
//    private Integer countViews;
//

}
