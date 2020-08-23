package com.component;

import com.config.SecurityPropertiesConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.support.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: restfulrcud
 * @description: 验证失败的处理
 * @author: Mr.Wang
 * @create: 2019-11-22 16:00
 **/
@Component("myAuthenticationFailureHandler")
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityPropertiesConfig securityPropertiesConfig;
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //AuthenticationException 这个异常类包含了在验证过程中所发生的错误异常
        if(LoginType.JSON.equals(securityPropertiesConfig.getLoginType())) {
            //将authentication以json的格式写给前台
            //默认返回的状态码是200，所以我们需要自己来声明失败时的状态码为500
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            //只返回错误的消息，如果是返回异常，则包含大部分无用信息
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString("登录失败"));
        }else {
            super.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
        }
    }
}
