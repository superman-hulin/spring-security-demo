package com.controller;

import com.pojo.ImageCode;
import com.support.ImageCodeGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: image_security
 * @description: 图形验证码的controller
 * @author: Su
 * @create: 2020-08-22 16:15
 **/
@RestController
public class ImageCodeController {
    //图形验证码的code保存在session中的对应的key
    public static final String IMAGE_SESSION_KEY="SESSION_KEY_IMAGE_CODE";
    @Autowired
    private ImageCodeGenerate imageCodeGenerate;
    //页面请求获取图形验证码
    @GetMapping("/code/image")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //创建图形验证码对象（含图片、验证码、过期时间）
        ImageCode imageCode=(ImageCode) imageCodeGenerate.generate();
        //将imageCode对象存到session中
        request.getSession().setAttribute(IMAGE_SESSION_KEY,imageCode);
        //将图片写到响应的输出流中（返回给前端显示）
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }
}
