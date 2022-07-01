package com.lite.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
public class User implements Serializable {
    @TableId
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String nickname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private Integer gender;//性别，女1男2
    private String address;
    private String resume;
    private String profession;
    private String profile;
}
