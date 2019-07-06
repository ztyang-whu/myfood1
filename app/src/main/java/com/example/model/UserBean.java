package com.example.model;

import java.io.Serializable;

public class UserBean implements Serializable {
    private String userId ="";  //用户账号
    private String userName ="";  //用户姓名或者昵称
    private String userHead = ""; //头像User_head
    private String userGender ="";  //用户性别
    private String userStar =""; //用户信用等级，初始时为4。最高为5。
    private String userCount ="";  //用户成功完成拼单次数。每三次可以升级半颗星。
    private String userAddress =""; //学生地址

    public UserBean() {

    }

    public UserBean(String userId, String userName, String userHead, String userGender, String userStar,
                    String userCount, String userAddress) {
        this.userId = userId;
        this.userName = userName;
        this.userHead = userHead;
        this.userGender = userGender;
        this.userStar = userStar;
        this.userCount = userCount;
        this.userAddress = userAddress;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserHead() {
        return userHead;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserStar() {
        return userStar;
    }

    public String getUserCount() {
        return userCount;
    }

    public String getUserAddress() {
        return userAddress;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserStar(String userStar) {
        this.userStar = userStar;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}
