package com.charles.heartfreshfood.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * user information
 */
public class User implements Serializable{
    private String   userName;
    private String   userPicUrl;
    private String   mobilePhone;
    private Integer  userId;
    private String   vendorid;
    private Integer  loginStatus;
    private Double   amount;
    private ArrayList<Integer> pickloclist;


    public User(){
        loginStatus = 0;
    }

    public ArrayList<Integer> getPickloclist() {
        return pickloclist;
    }

    public void setPickloclist(ArrayList<Integer> pickloclist) {
        this.pickloclist = pickloclist;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }
}
