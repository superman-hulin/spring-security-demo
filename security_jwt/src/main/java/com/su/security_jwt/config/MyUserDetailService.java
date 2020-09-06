package com.su.security_jwt.config;

import com.su.security_jwt.pojo.User;
import com.su.security_jwt.service.PermissionService;
import com.su.security_jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @program: security_jwt
 * @description: 自定义的UserDetailService
 * @author: Mr.Wang
 * @create: 2020-04-30 21:32
 **/
@Component
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user=userService.findUserByUserName(userName);
        if(user==null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        return new MyUserDetail(user,permissionService.findPermissionByUserId(user.getUserId()));
    }
}
