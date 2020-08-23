package com.filter;

import com.controller.SmsValidateCodeController;
import com.exception.ValidateCodeException;
import com.pojo.ValidateCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: restfulrcud
 * @description: 短信验证码的验证过滤器
 * @author: Mr.Wang
 * @create: 2019-11-27 12:29
 **/
@Component
public class SmsCodeFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/authentication/mobile",httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(),"post")){
            try {
                //该校验需要从session中拿短信验证码。
                validate(httpServletRequest);
            }catch (ValidateCodeException e){
                myAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                //出现异常的话就不走后面的过滤器链
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

    private void validate(HttpServletRequest request) {
        //从session中取出SmsCode对象
        ValidateCode codeInSession=(ValidateCode) request.getSession().getAttribute(SmsValidateCodeController.SMS_SESSION_KEY);
        //获取表单中用户写的短信验证码
        String codeInRequest=request.getParameter("smsCode");
        if(StringUtils.isEmpty(codeInRequest)){
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if(codeInSession==null){
            throw new ValidateCodeException("验证码不存在");
        }
        if(codeInSession.isExpried()){
            //将session中的短信验证码移除
            request.getSession().removeAttribute(SmsValidateCodeController.SMS_SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }
        if(!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            throw new ValidateCodeException("验证码不正确");
        }
        //验证完成后移除session中的验证码
        request.getSession().removeAttribute(SmsValidateCodeController.SMS_SESSION_KEY);
    }
}
