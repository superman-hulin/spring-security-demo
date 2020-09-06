package com.su.security_jwt.service.serviceImpl;

import com.su.security_jwt.config.MyUserDetail;
import com.su.security_jwt.config.MyUserDetailService;
import com.su.security_jwt.exception.UserException;
import com.su.security_jwt.mapper.UserMapper;
import com.su.security_jwt.pojo.User;
import com.su.security_jwt.service.UserService;
import com.su.security_jwt.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: security_jwt
 * @description: 用户业务层的实现
 * @author: Mr.Wang
 * @create: 2020-04-30 21:30
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        UserDetails userDetails=myUserDetailService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("密码不正确");
        }
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,null,
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token=jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public int register(User user) {
        String userName=user.getUserName();
        if(findUserByUserName(userName)!=null){
            return 0;
        }
        String password=passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(password);
        return userMapper.insert(user);
    }

    @Override
    public User findUserByUserName(String userName) {
        Map<String,Object> condition=new HashMap<>();
        condition.put("user_name",userName);
        List<User> users=userMapper.selectByMap(condition);
        if (users.size()==0){
            return null;
        }
        if(users.size()>1){
            throw new UserException("相同用户名存在多个用户，请管理员检查");
        }
        return users.get(0);
    }
}
