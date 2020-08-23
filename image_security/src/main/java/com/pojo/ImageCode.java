package com.pojo;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @program: image_security
 * @description: 图形验证码实体类
 * @author: Su
 * @create: 2020-08-22 16:19
 **/
public class ImageCode extends ValidateCode {
    private BufferedImage image; //展示给用户看的图片

    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code,expireIn);
        this.image = image;
    }

    public ImageCode(BufferedImage image,String code,LocalDateTime expireTime){
        super(code,expireTime);
        this.image=image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
