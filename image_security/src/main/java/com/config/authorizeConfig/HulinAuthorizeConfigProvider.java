package com.config.authorizeConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 将一部分需要授权成permitAll的url剥离到该provider中
 * @author: Mr.Wang
 * @create: 2020-03-08 14:25
 **/
@Component
@Order(Integer.MIN_VALUE)
public class HulinAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers("/authentication/require","login.html"
                ,"/code/*","/register.html","/register").permitAll();
    }
}
