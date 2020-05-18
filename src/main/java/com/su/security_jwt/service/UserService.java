package com.su.security_jwt.service;

import com.su.security_jwt.pojo.User;

public interface UserService {
    String login(String username, String password);
    int register(User user);
    User findUserByUserName(String userName);
}
