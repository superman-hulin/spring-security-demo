package com.config;

import com.support.LoginType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: image_security
 * @description: spring security的参数映射
 * @author: Su
 * @create: 2020-08-22 16:47
 **/
@Configuration
@EnableConfigurationProperties({SecurityPropertiesConfig.class})
@ConfigurationProperties(prefix = "security")
public class SecurityPropertiesConfig {
    private String loginPage="/login.html"; //默认值，当用户在配置文件中不配置时，则跳到该默认页
    //有的请求（表单提交）验证成功后希望是跳转，有的是ajax请求希望是返回json数据，则我们写一个配置类LoginType，默认是返回json
    private LoginType loginType=LoginType.JSON;
    //配置记住我功能的时间有效期
    private int rememberMeSeconds=3600;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
