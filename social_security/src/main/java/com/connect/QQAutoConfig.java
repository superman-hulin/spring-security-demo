package com.connect;

import com.config.SocialPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @program: restfulrcud
 * @description: QQ自动配置
 * @author: Mr.Wang
 * @create: 2019-12-01 13:50
 **/
@Configuration
@ConditionalOnProperty(prefix = "social",name = "app-id")//只有当配置文件中配置了hulin.security.social.qq.app-id的值后，该配置类才生效。
public class QQAutoConfig extends SocialAutoConfigurerAdapter {
    @Autowired
    private SocialPropertiesConfig socialPropertiesConfig;
    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new QQConnectionFactory(socialPropertiesConfig.getProviderId(),"5161","5155");
    }
}
