package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;


@EnableWebSecurity
// spring security的配置类，需要继承配置适配器
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler; //注入自己重写的成功验证后的处理
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    private SpringSocialConfigurer springSocialConfigurer; //该配置是将SocialAuthenticationFilter加到过滤器链上。
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();   //这个对象就是PasswordEncoder的实现类，用于我们完成密码加密和匹配密码
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

     http.apply(springSocialConfigurer)
                .and()
                .csrf().disable();
    }

}
