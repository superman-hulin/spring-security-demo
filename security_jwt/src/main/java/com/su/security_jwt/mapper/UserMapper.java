package com.su.security_jwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.su.security_jwt.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @program: security_jwt
 * @description: 用户的mapper接口
 * @author: Mr.Wang
 * @create: 2020-04-30 21:23
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
