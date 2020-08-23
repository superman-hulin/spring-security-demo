package com.connect;

import com.api.QQ;
import com.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * @program: restfulrcud
 * @description: 为spring social适配我们自己的api
 * @author: Mr.Wang
 * @create: 2019-12-01 11:09
 **/
public class QQAdapter implements ApiAdapter<QQ> {
    @Override
    public boolean test(QQ qq) {
        return true;
    }

    @Override
    public void setConnectionValues(QQ qq, ConnectionValues connectionValues) {
        //ConnectionValues是spring social里面规定的用户信息模板，qq的用户信息是我们自己写的api去获取的
        //我们需要对这两者作适配（将ConnectionValues中的值给全部填充）
        QQUserInfo qqUserInfo=qq.getUserInfo();
        connectionValues.setDisplayName(qqUserInfo.getNickname());
        connectionValues.setImageUrl(qqUserInfo.getFigureurl_qq_1());
        connectionValues.setProfileUrl(null);//个人主页的地址，而qq是没有的,微博是有的
        connectionValues.setProviderUserId(qqUserInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        return null;
    }

    @Override
    public void updateStatus(QQ qq, String s) {

    }
}
