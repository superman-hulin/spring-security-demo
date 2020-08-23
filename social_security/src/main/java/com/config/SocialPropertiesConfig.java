package com.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: restfulrcud
 * @description: social相关的配置
 * @author: Mr.Wang
 * @create: 2019-12-01 13:47
 **/
@Configuration
@EnableConfigurationProperties(SocialPropertiesConfig.class)
@ConfigurationProperties(prefix = "social")
public class SocialPropertiesConfig {
    private String providerId ="qq";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

}
