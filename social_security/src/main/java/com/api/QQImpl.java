package com.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

/**
 * @program: restfulrcud
 * @description: 获取qq用户信息的实现类
 * @author: Mr.Wang
 * @create: 2019-11-29 10:11
 **/
/*
在qq互联开放平台上查看获取用户信息的接口文档
需要我们传入三个参数：token（令牌），该参数在AbstractOAuth2ApiBinding中定义了
                  appId：在qq互联平台注册后会有一个appId
                  openId:用户的id，与QQ号码一一对应(携带token向qq发起请求时会返回的)
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {
    //获取openid
    private static final String URL_GET_OPENID="https://graph.qq.com/oauth2.0/me?access_token=%s";
    //获取用户信息
    private static final String URL_GET_USERINFO="https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
    private String appId;
    private String openId; //用户在服务提供商中的唯一标识
    @Autowired
    private ObjectMapper objectMapper; //该类用于将json封装成对象
    public QQImpl(String accessToken,String appId){ //这两个参数是外面提供的
        //父类有两个构造函数（一个只需要一个参数，一个需要两个），当调用只需要token的那个构造函数时，
        // 它会默认再传一个默认的TokenStrategy（获取用户信息时的请求中将token放到请求头中）去调用两个参数的构造函数。
        //而这种默认的TokenStrategy不满足qq的要求（qq要求token作为请求地址的参数），所以我们再自己加一个TokenStrategy的参数。
        //这样调用父类的构造之后，父类会自动将accessToken补到获取用户信息的url后面的参数上。
        super(accessToken,TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId=appId;
        String openId_url=String.format(URL_GET_OPENID,accessToken);
        //RestTemplate是父类的，它是帮我们发请求的。
        String openId_result=getRestTemplate().getForObject(openId_url,String.class);
        System.out.println(openId_result);
        this.openId=StringUtils.substringBetween(openId_result,"\"openid\":\"","\"}");
    }
    @Override
    public QQUserInfo getUserInfo() {
        String userInfo_url=String.format(URL_GET_USERINFO,appId,openId);
        String userInfo_result=getRestTemplate().getForObject(userInfo_url,String.class);
        System.out.println(userInfo_result);
        QQUserInfo qqUserInfo=null ;
        try {
             qqUserInfo=objectMapper.readValue(userInfo_result,QQUserInfo.class);
             qqUserInfo.setOpenId(openId);
             return qqUserInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
