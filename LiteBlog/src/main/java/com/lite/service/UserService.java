package com.lite.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lite.entity.User;

public interface UserService extends IService<User> {
    User getByUsernameAndPassword(String username,String password);

}
