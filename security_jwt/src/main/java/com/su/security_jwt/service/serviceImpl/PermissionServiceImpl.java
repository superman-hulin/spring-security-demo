package com.su.security_jwt.service.serviceImpl;

import com.su.security_jwt.mapper.PermissionMapper;
import com.su.security_jwt.pojo.Role;
import com.su.security_jwt.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: security_jwt
 * @description: 用户权限
 * @author: Mr.Wang
 * @create: 2020-05-03 08:53
 **/
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public List<Role> findPermissionByUserId(int userId) {
       return permissionMapper.findPermissionByUserId(userId);
    }
}
