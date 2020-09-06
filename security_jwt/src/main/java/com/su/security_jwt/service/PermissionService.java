package com.su.security_jwt.service;

import com.su.security_jwt.pojo.Role;

import java.util.List;

public interface PermissionService {
    List<Role> findPermissionByUserId(int userId);
}
