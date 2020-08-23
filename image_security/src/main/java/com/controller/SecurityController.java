package com.controller;

import com.config.SecurityPropertiesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: restfulrcud
 * @description: 验证请求时来到该controller
 * @author: Mr.Wang
 * @create: 2019-11-21 15:53
 **/
@RestController
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
//当请求不是从html格式发出的话，则只返回状态码和json数据
public class SecurityController {
    //spring security决定了是否需要验证，当需要验证时，根据loginPage()来决定跳转到哪里去验证，在跳转之前该对象会将需要验证的请求存放到session中
    private RequestCache requestCache=new HttpSessionRequestCache();
    //利用该对象作跳转
    private RedirectStrategy redirectStrategy=new DefaultRedirectStrategy();
    @Autowired
    private SecurityPropertiesConfig securityProperties;

    //当需要身份认证时，跳到该方法
    @RequestMapping("/authentication/require")
    public  Object requireAuthentication(HttpServletRequest request, HttpServletResponse response){
        //从session中存储的请求中取出请求
        SavedRequest savedRequest=requestCache.getRequest(request,response);
        if(savedRequest!=null){
            //拿到引发跳转请求的url
            String targetUrl=savedRequest.getRedirectUrl();
            //如果请求是以.html结尾
            if(StringUtils.endsWithIgnoreCase(targetUrl,".html")){
                try {
                    //跳转到用户在配置文件中配置的登录页securityProperties.getBrowser().getLoginPage()
                    redirectStrategy.sendRedirect(request, response,securityProperties.getLoginPage());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        //返回json
        return "";
    }
}
