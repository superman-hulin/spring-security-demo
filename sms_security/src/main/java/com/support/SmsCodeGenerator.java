package com.support;


import com.pojo.SmsValidateCode;
import com.pojo.ValidateCode;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 短信验证码生成器
 * @author: Mr.Wang
 * @create: 2019-11-24 12:02
 **/
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerate {

    @Override
    public ValidateCode generate() {
        //随机生成一个长度为n位的验证码（长度可以在配置文件自定义）
        String code=RandomStringUtils.randomNumeric(6);
        return new SmsValidateCode(code,60);
    }
}
