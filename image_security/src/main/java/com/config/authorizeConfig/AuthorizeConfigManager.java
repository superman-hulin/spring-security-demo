package com.config.authorizeConfig;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/*Spring Security控制授权
        简单的授权控制有用户是否需要登录（通过antMatchers().permitAll()就可以实现）和该请求需要什么权限（通过antMatchers().hasRole()就可以。)
        但是复杂的授权控制还是需要利用数据库
        通用RBAC（Role-Based Access Control）数据模型
        该数据模型有五张表（三张实体表，两张关系表），实体表：用户表、角色表、资源表（存储资源信息，菜单、按钮及其url）
        关系表：用户和角色的关系表（多对多），角色和资源的关系表（多对多）
        */
public interface AuthorizeConfigManager {
    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);
}
