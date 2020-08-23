package com.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @program: restfulrcud
 * @description: 处理短信验证码token的provider
 * @author: Mr.Wang
 * @create: 2019-11-27 12:15
 **/
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken=(SmsCodeAuthenticationToken) authentication;
        UserDetails user=userDetailsService.loadUserByUsername(authenticationToken.getPrincipal()+"");
        if(user==null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        SmsCodeAuthenticationToken authenticationResult=new SmsCodeAuthenticationToken(user,user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
