package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.charles.heartfreshfood.R;

/**
 * Created by Charles on 16/4/26.
 */
public class AddressItemView extends RelativeLayout {
    TextView address,name,phone;
    public AddressItemView(Context context) {
        super(context);
    }

    public AddressItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_address_item, null);
        address = (TextView)findViewById(R.id.addresss_item_address);
        name = (TextView)findViewById(R.id.addresss_item_pickname);
        phone = (TextView)findViewById(R.id.addresss_item_phone);
    }

    public void setPhone(String p){
        phone.setText(p);
    }

    public void setName(String n){
        name.setText(n);
    }

    public void setAddress(String a){
        address.setText(a);
    }

}
