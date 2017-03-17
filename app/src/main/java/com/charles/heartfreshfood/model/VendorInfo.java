package com.charles.heartfreshfood.model;

/**
 * 商家页面信息
 */
public class VendorInfo extends VendorProfile{
    private int reviewCount;
    private int productCount;

    public VendorInfo() {
    }

    public VendorInfo(String vendorid, String vendorName, String addressDetail, String imgUrl, double rate, int reviewCount, int productCount) {
        super(vendorid, vendorName, addressDetail, imgUrl, rate);
        this.reviewCount = reviewCount;
        this.productCount = productCount;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
