package com.support;

//短信验证码发送的接口
public interface SmsCodeSender {
    void send(String mobile, String code);
}
