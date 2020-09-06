package com.su.security_jwt.controller;

import com.su.security_jwt.pojo.User;
import com.su.security_jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @program: security_jwt
 * @description: 用户的controller层
 * @author: Mr.Wang
 * @create: 2020-04-30 21:27
 **/
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(String userName,String userPassword){
        return userService.login(userName,userPassword);
    }
    @PostMapping("/register")
    public int register(@RequestBody User user){
        return userService.register(user);
    }
    @GetMapping("/hello")
    public String get(){
        return "hello";
    }
    @GetMapping("/normal")
    @PreAuthorize("hasAnyAuthority('ROLE_NORMAL')")
    public String test1(){
        return "normal成功";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String test2(){
        return "admin成功";
    }
}
