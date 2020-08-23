package com.pojo;

/**
 * @program: image_security
 * @description: 用户实体类
 * @author: Su
 * @create: 2020-08-22 16:54
 **/
public class UserBean {
    private String userName;
    private String password;
    private String mobile;


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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public UserBean(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserBean(String mobile) {
        this.mobile = mobile;
    }
}
