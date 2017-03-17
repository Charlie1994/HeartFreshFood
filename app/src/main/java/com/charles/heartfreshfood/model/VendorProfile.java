package com.charles.heartfreshfood.model;

import java.io.Serializable;

/**
 *  用于展示栏
 */
public class VendorProfile implements Serializable{
    private String vendorid;
    private String vendorName;
    private String addressDetail;
    private String imgUrl;
    private double Rate;

    public VendorProfile() {
    }

    public VendorProfile(String vendorid, String vendorName, String addressDetail, String imgUrl, double rate) {
        this.vendorid = vendorid;
        this.vendorName = vendorName;
        this.addressDetail = addressDetail;
        this.imgUrl = imgUrl;
        Rate = rate;
    }

    public String getVendorid() {
        return vendorid;
    }

    public void setVendorid(String vendorid) {
        this.vendorid = vendorid;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
