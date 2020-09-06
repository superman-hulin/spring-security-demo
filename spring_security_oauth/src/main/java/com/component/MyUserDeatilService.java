package com.component;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 用来处理用户信息获取逻辑
 * @author: Mr.Wang
 * @create: 2019-11-20 10:47
 **/
@Component("myUserDeatilService")
public class MyUserDeatilService implements UserDetailsService,SocialUserDetailsService {
   @Autowired
   private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //ROLE_前缀必要的，后面的名称可以随意
        return new User(username,passwordEncoder.encode("123456"),AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER"));
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        return new SocialUser(userId,passwordEncoder.encode("123456"),AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

    }

}
