package com.su.security_jwt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: security_jwt
 * @description: 用户实体类
 * @author: Mr.Wang
 * @create: 2020-04-30 21:25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    private int userId;
    private String userName;
    private String userPassword;

}

