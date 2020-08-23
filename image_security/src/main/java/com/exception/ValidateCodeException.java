package com.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @program: image_security
 * @description: 验证码自定义异常
 * @author: Su
 * @create: 2020-08-22 16:40
 **/
//AuthenticationException 是security定义的一个抽象异常，是security中所有抛出异常的一个基类
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID=-7285211528095468156L;
    public ValidateCodeException(String msg){
        super(msg);
    }

}
