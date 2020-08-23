package com.controller;

import com.pojo.ValidateCode;
import com.support.SmsCodeSender;
import com.support.ValidateCodeGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @program: restfulrcud
 * @description: 验证码请求的控制
 * @author: Mr.Wang
 * @create: 2019-11-23 16:38
 **/
@RestController
public class SmsValidateCodeController {
    public static final String SMS_SESSION_KEY="SESSION_KEY_SMS_CODE";
   @Autowired
   private ValidateCodeGenerate smsCodeGenerator;
   @Autowired
   private SmsCodeSender smsCodeSender;
   //请求获取手机验证码
    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建短信验证码
        ValidateCode smsCode=smsCodeGenerator.generate();
        //从用户请求中获取用户填写的手机号
        String mobile=request.getParameter("mobile");
        //将smsCode对象存到session中
        request.getSession().setAttribute(SMS_SESSION_KEY,smsCode);
        //发送验证码给用户
        smsCodeSender.send(mobile,smsCode.getCode());

    }

    @GetMapping("/me")
    public void test(){
        System.out.println("欢迎进入");
    }

}
