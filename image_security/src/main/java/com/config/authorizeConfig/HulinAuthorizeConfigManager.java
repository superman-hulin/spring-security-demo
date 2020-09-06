package com.config.authorizeConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: restfulrcud
 * @description: 收集所有的AuthorizeConfigProvider
 * @author: Mr.Wang
 * @create: 2020-03-08 14:34
 **/
@Component
public class HulinAuthorizeConfigManager implements AuthorizeConfigManager {
    @Autowired
    private List<AuthorizeConfigProvider> authorizeConfigProviders;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
            //遍历所有的AuthorizeConfigProvider，将他们都配置上去
            authorizeConfigProvider.config(config);
        }
        //除了上面的所有AuthorizeConfigProvider中的请求是通过之后，其它任何请求需要验证
       // config.anyRequest().authenticated();
    }
}
//最后将该manager注入到MySecurityConfig里面，将之前的那些授权配置全部去掉
