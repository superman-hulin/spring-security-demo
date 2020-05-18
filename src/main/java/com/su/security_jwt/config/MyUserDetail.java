package com.su.security_jwt.config;

import com.su.security_jwt.pojo.Role;
import com.su.security_jwt.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: security_jwt
 * @description: 自定义的UserDetail
 * @author: Mr.Wang
 * @create: 2020-04-30 21:33
 **/
public class MyUserDetail implements UserDetails {
    //用户信息
    private User user;
    //用户权限
    private List<Role> roles;
    public MyUserDetail(User user,List<Role> roles){
        this.user=user;
        this.roles=roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       List<GrantedAuthority> authorities=new ArrayList<>();
       for(Role role:roles){
           authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
       }
       return authorities;
    }
    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
