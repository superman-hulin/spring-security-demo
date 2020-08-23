package com.component;

import com.pojo.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 用来处理用户信息获取逻辑
 * @author: Mr.Wang
 * @create: 2019-11-20 10:47
 **/
@Component("myUserDeatilService")
public class MyUserDeatilService implements SocialUserDetailsService {


    //社交登录时候用的，传进来的userId是通过数据库中的userconnection表中的根据providerId和ProviderUserId（该用户在该社交上的openId）联合查出在应用上的userId。
    //然后我们再根据传来的userId来找到该用户的用户信息封装到SocialUserDetails中
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        UserBean userBean =new UserBean(userId); //这个userId并不是说一定是用户的id号，只要是用户的主键就好（用户名为主键时，那就是用户名）
        return new SocialUser(userId,userBean.getPassword(),AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
