package com.component;

import com.config.SecurityPropertiesConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: restfulrcud
 * @description: 验证成功后的请求处理
 * @author: Mr.Wang
 * @create: 2019-11-21 10:14
 **/
//默认的登录成功后会跳转到我们请求的网页，但是现在大部分通过ajax发起请求，验证成功后希望得到状态码和json数据，而不是跳转
//所以自己重写验证成功后的处理
@Component(value = "myAuthenticationSuccessHandler")
//当成功处理不做根据用户配置返回格式为json还是跳转时就implements AuthenticationFailureHandler，如果加上这种用户配置判断的话就extends SavedRequestAwareAuthenticationSuccessHandler
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityPropertiesConfig securityPropertiesConfig;
    //验证成功后执行的方法，参数Authentication中封装了请求的ip、session和封装的userdetail对象
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        System.out.println("登录成功");
        //将authentication以json的格式写给前台
        if(LoginType.JSON.equals(securityPropertiesConfig.getLoginType())) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            try {
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            try {
                //如果不是json形式，则调用父类的方法（即跳转）
                super.onAuthenticationSuccess(httpServletRequest,httpServletResponse,authentication);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
