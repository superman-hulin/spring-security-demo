package com.su.security_jwt.exception;

import com.su.security_jwt.support.ResultCode;

/**
 * @program: security_jwt
 * @description: 用户异常
 * @author: Mr.Wang
 * @create: 2020-04-30 21:43
 **/
public class UserException extends RuntimeException {
    private ResultCode code;

    private String msg;

    public UserException(String msg, ResultCode code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public UserException(String msg) {
        super(msg);
        this.msg = msg;
        this.code = ResultCode.FAILED;
    }
}
