package com.component;


import com.pojo.UserBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 用来处理用户信息获取逻辑
 * @author: Mr.Wang
 * @create: 2019-11-20 10:47
 **/
@Component("myUserDeatilService")
public class MyUserDeatilService implements UserDetailsService{

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //到数据库根据手机号查找用户信息
        UserBean user =new UserBean(s);
        //这个user是spring security里面封装的实现了UserDetails的对象
        //第三个参数是用户的权限，是一个集合，用来作授权
        //第一个和第二个参数是用来做认证
        //这个user是三个参数的构造函数，也有7个参数的构造函数，会多以下四个参数
        // isAccountNonExpired();  账户是否没过期
        //  isAccountNonLocked();  账户是否没冻结
        //  isCredentialsNonExpired(); 密码是否没过期（有的网站会要求多久必须更换一次密码）
        // isEnabled();   账户是否可用（是否被删除）
        //这个user实现类我们也可以自己定义，只需要将实体类继承UserDetails接口就好
        return new User(user.getUserName(),user.getPassword(),AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_USER"));//
        //ROLE_前缀必要的，后面的名称可以随意
    }
}
