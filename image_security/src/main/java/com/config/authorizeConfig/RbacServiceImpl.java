package com.config.authorizeConfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @program: restfulrcud
 * @description: Rbac的实现类
 * @author: Mr.Wang
 * @create: 2020-03-09 10:21
 **/
@Component("rbacService")
public class RbacServiceImpl implements RbacService {
    private AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal=authentication.getPrincipal();
        boolean hasPermission=false;
        if(principal instanceof UserDetails){
            String username=((UserDetails) principal).getUsername();
            //然后拿着用户名到用户角色关系表查找该用户的角色，然后再拿这些角色去查找角色资源表查找出资源
            Set<String> urls=new HashSet<>();
            for (String url:urls) {
                //由于权限配置中可能配的是/user/*,而用户可能请求的url是/user/1,所以不能简单的用equals去判断url是否相等
                if(antPathMatcher.match(url,request.getRequestURI())){
                    hasPermission=true;
                    break;
                }
            }
        }
        return hasPermission;
    }
}
