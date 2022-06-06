package com.lite.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
    @TableId
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String nickname;
}
