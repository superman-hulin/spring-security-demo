package com.support;

import org.springframework.stereotype.Component;

/**
 * @program: restfulrcud
 * @description: 默认的短信验证码发送方式
 * @author: Mr.Wang
 * @create: 2019-11-24 11:02
 **/
@Component
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机"+mobile+"发送短信验证码"+code);
    }
}
