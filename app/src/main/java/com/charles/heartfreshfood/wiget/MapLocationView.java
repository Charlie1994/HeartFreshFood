package com.charles.heartfreshfood.wiget;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.activity.VendorActivity;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.PickingLocation;
import com.charles.heartfreshfood.model.VendorLocationProfile;
import com.charles.heartfreshfood.model.VendorProfile;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 地图版块显示
 */
public class MapLocationView extends RelativeLayout {
    private TextView nametv;
    private TextView addresstv;
    private RatingBar rateBar;
    private ImageView img;
    private Button button;
    private Context context;
    private TextView distanceTv;

    public MapLocationView(Context context) {
        super(context);
    }

    public MapLocationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_location_map,this);
        nametv = (TextView)findViewById(R.id.view_location_map_name);
        addresstv = (TextView)findViewById(R.id.view_location_map_addressdetail);
        rateBar = (RatingBar)findViewById(R.id.view_location_map_rate);
        button = (Button)findViewById(R.id.view_location_map_go);
        distanceTv = (TextView)findViewById(R.id.view_location_map_distance);
    }

    public void setVendorProfile(final VendorLocationProfile profile,final Activity activity){
        String name = profile.getVendorName();
        String address = profile.getAddressDetail();
        double rate = profile.getRate();
        String id = profile.getVendorID();
        nametv.setVisibility(View.VISIBLE);
        nametv.setText(name);
        addresstv.setText(address);
        addresstv.setVisibility(View.VISIBLE);
        rateBar.setRating((float) rate);
        rateBar.setVisibility(View.VISIBLE);
        button.setText("赶紧去看看");
        button.setEnabled(true);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorActivity.class);
                VendorProfile vendorProfile = new VendorProfile(profile.getVendorID(),profile.getVendorName(),profile.getAddressDetail(),
                      profile.getImgurl(),profile.getRate());
                intent.putExtra("vendorprofile", vendorProfile);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }

    public void setPickingProfile(final PickingLocation loc){
        final ArrayList<Integer> list = MyApplication.getUser().getPickloclist();
        final int pickid = loc.getLocationID();
        String name = loc.getName();
        String address = loc.getAddressDetail();
        nametv.setText(name);
        addresstv.setText(address);
        if(!list.contains(pickid)){
            button.setText("设为收货地址");
            button.setEnabled(true);
            button.setOnClickListener(new OnClickListener() {
                Dialog dialog;
                @Override
                public void onClick(View v) {
                    dialog = ProgressDialog.show(context, "爱心鲜", "数据更新中，请稍候……");
                    int userid = MyApplication.getUser().getUserId();
                    final NetworkApi service = NetworkApi.retrofit.create(NetworkApi.class);
                    Call<String> call = service.addPickLoc(userid,pickid,loc.getName());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Response<String> response, Retrofit retrofit) {
                            dialog.dismiss();
                            String result = response.body();
                            if(result.equals("success")){
                                Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
                                list.add(pickid);
                                button.setText("已为收货地址");
                                button.setEnabled(false);
                            }else{
                                Toast.makeText(context,"添加失败",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Toast.makeText(context,"网络出错",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else{
            button.setText("已为收货地址");
            button.setEnabled(false);
        }
        rateBar.setVisibility(View.INVISIBLE);
        button.setVisibility(View.VISIBLE);
    }
    public void setDistance(int d){
        distanceTv.setText("距离约"+String.valueOf(d)+"米");
    }
}
