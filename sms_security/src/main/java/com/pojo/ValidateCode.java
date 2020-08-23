package com.pojo;

import java.time.LocalDateTime;

/**
 * @program: image_security
 * @description: 验证码基类
 *                 任何验证码都会有两个基本属性 验证码、过期时间
 * @author: Su
 * @create: 2020-08-22 16:20
 **/
public class ValidateCode {
    private String code;
    private LocalDateTime expireTime;
    //expireIn代表多少秒过期
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);;
    }

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }
    //判断是否过期
    public boolean isExpried(){
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
