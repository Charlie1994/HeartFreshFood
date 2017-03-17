package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.util.HttpUrls;

/**
 * 用于首页商家推荐
 */
public class HomeAdvertiseView extends RelativeLayout {

    private ImageView img;
    private TextView vendorNameTv;
    private TextView addressTv;
    private Context context;

    public HomeAdvertiseView(Context context) {
        super(context);
    }

    public HomeAdvertiseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_advertise_home_layout, this);
        addressTv = (TextView)findViewById(R.id.view_advertise_address);
        vendorNameTv = (TextView)findViewById(R.id.view_advertise_vendorname);
        img = (ImageView)findViewById(R.id.view_advertise_img);
    }

    public void setVendorInfo(String url,String name,String address){
        vendorNameTv.setText(name);
        addressTv.setText(address);
        Glide
              .with(context)
              .load(HttpUrls.LOCAL_URL+url)
              .fitCenter()
              .into(img);
    }

}
