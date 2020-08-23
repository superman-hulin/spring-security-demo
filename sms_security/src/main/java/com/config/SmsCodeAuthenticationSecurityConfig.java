package com.config;

import com.filter.SmsAuthenticationFilter;
import com.support.SmsCodeAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 将自己写的关于短信验证码的过滤器和provide配置到spring security中
 * @author: Mr.Wang
 * @create: 2019-11-27 20:19
 **/
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain,HttpSecurity> {
    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    @Qualifier("myUserDeatilService")
    private UserDetailsService userDetailsService;
    public void configure(HttpSecurity httpSecurity)throws Exception{
        SmsAuthenticationFilter smsAuthenticationFilter=new SmsAuthenticationFilter();
        smsAuthenticationFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        smsAuthenticationFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider=new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);
        httpSecurity.authenticationProvider(smsCodeAuthenticationProvider) //将我们自己写的provider加到spring security中
                .addFilterAfter(smsAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
    }
}
