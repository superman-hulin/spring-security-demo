package com.su.security_jwt.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: security_jwt
 * @description: 用户角色实体类
 * @author: Mr.Wang
 * @create: 2020-05-03 08:34
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("role")
public class Role {
    private int roleId;
    private String roleName;
}
