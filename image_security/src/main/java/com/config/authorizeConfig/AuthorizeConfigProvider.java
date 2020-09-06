package com.config.authorizeConfig;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/*
.authorizeRequests() //代表下面都是授权的配置
.antMatchers(
  "/authentication/require",securityProperties.getBrowser().getLoginPage()
  ,"/code/*","/register.html","/register").permitAll()
  采用AuthorizeConfigProvider接口的多个实现类将上面这些在security配置类里面配置的授权信息全部剥离出去
 */
public interface AuthorizeConfigProvider {
    //这个参数就是.authorizeRequests()的返回对象
    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);
}
