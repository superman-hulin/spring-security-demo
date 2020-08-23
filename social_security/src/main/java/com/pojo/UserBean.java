package com.pojo;

/**
 * @program: social_security
 * @description: 用户实体类
 * @author: Su
 * @create: 2020-08-23 11:08
 **/
public class UserBean {
    private String userId;
    private String userName;
    private String password;

    public UserBean(String userId) {
        this.userId = userId;
    }

    public UserBean(String userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
