package com.filter;

import com.controller.ImageCodeController;
import com.exception.ValidateCodeException;
import com.pojo.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: image_security
 * @description: 图形验证码的过滤器
 * @author: Su
 * @create: 2020-08-22 16:17
 **/
@Component
public class ImageCodeFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/userlogin",httpServletRequest.getRequestURI())
                && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(),"post")){
            try {
                //该校验需要从session中拿图形验证码的随机数。需要传入这个参数才能从session拿东西
                validate(httpServletRequest);
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
                //出现异常的话就不走后面的过滤器链
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        //从session中取出imageCode对象
        ImageCode codeInSession=(ImageCode)request.getSession().getAttribute(ImageCodeController.IMAGE_SESSION_KEY);
        //获取表单中用户写的图形验证码
        String codeInRequest=request.getParameter("imageCode");
        if(StringUtils.isEmpty(codeInRequest)){
            throw new ValidateCodeException("验证码的值不能为空");
        }
        if(codeInSession==null){
            throw new ValidateCodeException("验证码不存在");
        }
        if(codeInSession.isExpried()){
            //将session中的图形验证码移除
            request.getSession().removeAttribute(ImageCodeController.IMAGE_SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }
        if(!StringUtils.equals(codeInSession.getCode(),codeInRequest)){
            throw new ValidateCodeException("验证码不正确");
        }
        //验证完成后移除session中的验证码
        request.getSession().removeAttribute(ImageCodeController.IMAGE_SESSION_KEY);
    }
}
