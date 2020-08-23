package com.pojo;

import java.time.LocalDateTime;

/**
 * @program: sms_security
 * @description: 短信验证码实体类
 * @author: Su
 * @create: 2020-08-22 20:49
 **/
public class SmsValidateCode extends ValidateCode {
    public SmsValidateCode(String code, int expireIn) {
        super(code,expireIn);
    }
    public SmsValidateCode(String code,LocalDateTime expireTime){
        super(code,expireTime);
    }
}
