package com.su.security_jwt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.su.security_jwt.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface PermissionMapper extends BaseMapper<Role> {
     //根据用户id查询用户的权限
    List findPermissionByUserId(int userId);
}
