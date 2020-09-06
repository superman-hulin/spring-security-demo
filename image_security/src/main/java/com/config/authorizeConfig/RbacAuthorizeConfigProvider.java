package com.config.authorizeConfig;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 将rbac结合到provider
 * @author: Mr.Wang
 * @create: 2020-03-09 10:34
 **/
@Component
@Order(Integer.MAX_VALUE)
public class RbacAuthorizeConfigProvider implements AuthorizeConfigProvider {
    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
      //写一个权限表达式将rbac的权限控制加到springsecurity中
        config.anyRequest().access("@rbacService.hasPermission(request,authentication)");
        //但是上面的代码有两个问题，1.由于写的anyRequest()，而在Manager实现类中循环完所有的provider之后，也写了config.anyRequest().authenticated()
        //所以后面的anyRequest()会覆盖这个。所以先把manager中的注释掉
        //第二个问题，我们需要将该provider最后生效（因为如果这个第一个生效，那么所有请求都走这个配置了），通过注解order来解决
    }
}
