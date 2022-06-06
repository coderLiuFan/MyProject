package com.lite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lite.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username = #{username} and password = #{password}")
    User selectByUsernameAndPassword(String username,String password);

}
