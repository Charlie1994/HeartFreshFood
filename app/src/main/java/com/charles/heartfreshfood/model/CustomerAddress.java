package com.charles.heartfreshfood.model;

import java.io.Serializable;

/**
 *  地址类
 */
public class CustomerAddress implements Serializable{
    private String province;
    private String city;
    private String region;
    private String detail;//详细地址
    private String locationName;
    private String pickingName;
    private String phone;
    private Integer pickinglocationid;
    public CustomerAddress() {
    }

    public CustomerAddress(String province, String city, String region, String detail, String locationName, String pickingName, String phone, Integer pickinglocationid) {
        this.province = province;
        this.city = city;
        this.region = region;
        this.detail = detail;
        this.locationName = locationName;
        this.pickingName = pickingName;
        this.phone = phone;
        this.pickinglocationid = pickinglocationid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPickingName() {
        return pickingName;
    }

    public void setPickingName(String pickingName) {
        this.pickingName = pickingName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPickinglocationid() {
        return pickinglocationid;
    }

    public void setPickinglocationid(Integer pickinglocationid) {
        this.pickinglocationid = pickinglocationid;
    }
}
