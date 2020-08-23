package com.connect;


import com.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @program: restfulrcud
 * @description: connet
 * @author: Mr.Wang
 * @create: 2019-12-01 13:10
 **/
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    public QQConnectionFactory(String providerId, String appId,String appSecret) {
        super(providerId, new QQServiceProvider(appId,appSecret), new QQAdapter());
    }//泛型为api的类型
}
