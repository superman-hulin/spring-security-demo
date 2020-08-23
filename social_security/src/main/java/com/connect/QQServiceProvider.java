package com.connect;

import com.api.QQ;
import com.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @program: restfulrcud
 * @description: sd
 * @author: Mr.Wang
 * @create: 2019-11-29 15:04
 **/
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {//这个泛型是api接口的类型
    private String appId;
    private static final String URL_AUTHORIZE="https://graph.qq.com/oauth2.0/authorize";
    private static final String URL_ACESS_TOKEN="https://graph.qq.com/oauth2.0/token";
    public QQServiceProvider(String appId,String appSecret) {
        //创建该对象要有四个参数 1.应用id 2.应用的密码 （1和2是在互联平台创建应用时生成）
        // 3.authorizeUrl:该url是将用户导向认证服务器同意授权的地址。
        //4.accessTokenUrl：该url是应用拿着授权码向认证服务器申请令牌的地址。
        super(new OAuth2Template(appId,appSecret,URL_AUTHORIZE,URL_ACESS_TOKEN));
        this.appId=appId;
    }
    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken,appId);
    }
}
