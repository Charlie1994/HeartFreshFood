package com.charles.heartfreshfood.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.fragment.CartFragment;
import com.charles.heartfreshfood.fragment.CommunityFragment;
import com.charles.heartfreshfood.fragment.HomeFragment;
import com.charles.heartfreshfood.fragment.MapFragment;
import com.charles.heartfreshfood.fragment.MineFragment;
import com.charles.heartfreshfood.model.User;
import com.charles.heartfreshfood.util.DBService;
import com.igexin.sdk.PushManager;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    NetworkApi service = NetworkApi.retrofit.create(NetworkApi.class);
    /*底部导航栏按键的数组*/
    private ViewGroup[] tabGroup= new ViewGroup[5];
    /*tab选择后图像数组*/
    private int[] tabImageSelected = {R.drawable.home_selected,R.drawable.cart_selected,R.drawable.map_selected,
          R.drawable.society_selected,R.drawable.mine_selected};
    /*tab未选择图像数组*/
    private int[] tabImageUnselected = {R.drawable.home_unselected,R.drawable.cart_unselected,R.drawable.map_unselected,
          R.drawable.society_unselected,R.drawable.mine_unselected};
    /*tab容器数组*/
    private int[] tabContent = {R.id.tab_home_btn,R.id.tab_cart_btn,R.id.tab_map_btn,R.id.tab_society_btn,
          R.id.tab_mine_btn};
    private String[] tabText = {"首页","购物车","附近","爱分享","我的"};
    //首页页面
    private HomeFragment homeFragment;
    //购物车页面
    private CartFragment cartFragment;
    //社交页面
    private CommunityFragment communityFragment;
    //地图页面
    private MapFragment mapFragment;
    //我的页面
    private  MineFragment mineFragment;
    //当前位置
    private int currentpos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentpos =0;
        initViews();
        getUserfromspf();//自动登录
    }
    /*底部导航栏初始化*/
    private void  initViews(){
        for(int i = 0;i<5;i++){
            tabGroup[i] = (ViewGroup)findViewById(tabContent[i]);
            if(i==0){
                ((ImageView)tabGroup[i].getChildAt(0)).setImageResource(tabImageSelected[i]);
                ((TextView)tabGroup[i].getChildAt(1)).setTextColor(Color.rgb(255, 90, 30));
            }else{
                ((ImageView)tabGroup[i].getChildAt(0)).setImageResource(tabImageUnselected[i]);
                ((TextView)tabGroup[i].getChildAt(1)).setTextColor(Color.rgb(108,108,108));
            }
            ((TextView)tabGroup[i].getChildAt(1)).setText(tabText[i]);
            tabGroup[i].setOnClickListener(new TabClickListener(i));
        }
        // 初始化默认显示的界面
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
            addFragment(homeFragment);
            showFragment(homeFragment,0);
        } else {
            showFragment(homeFragment,0);
        }

    }

    /** 添加Fragment **/
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.tab_realcontent, fragment);
        ft.commit();
    }

    /** 删除Fragment **/
    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    /*点击事件监听器，点击对应的图标*/
    private class TabClickListener implements View.OnClickListener{
        private int index;
        public TabClickListener(int index){
            this.index = index;
        }
        @Override
        public void onClick(View v) {
            ((ImageView)tabGroup[index].getChildAt(0)).setImageResource(tabImageSelected[index]);
            ((TextView)tabGroup[index].getChildAt(1)).setTextColor(Color.rgb(255, 90, 30));

            for(int j = 1;j<5;j++) {
                ((ImageView)tabGroup[(index+j)%5].getChildAt(0)).setImageResource(tabImageUnselected[(index+j)%5]);
                ((TextView)tabGroup[(index+j)%5].getChildAt(1)).setTextColor(Color.rgb(108, 108, 108));
            }
            switch (index){
                case 0 :{
                    // 主界面
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                        // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                        addFragment(homeFragment);
                        showFragment(homeFragment,index);
                    } else {
                        if (homeFragment.isHidden()) {
                            showFragment(homeFragment,index);
                        }
                    }
                    break;
                }
                case 1 :{
                    // 购物车界面
                    if (cartFragment == null) {
                        cartFragment = new CartFragment();
                        // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                        addFragment(cartFragment);
                        showFragment(cartFragment,index);
                    } else {
                        if (cartFragment.isHidden()) {
                            showFragment(cartFragment,index);
                        }
                    }
                    break;
                }
                case 2 :{
                    // 地图界面
                    if (mapFragment == null) {
                        mapFragment = new MapFragment();
                        // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                        addFragment(mapFragment);
                        showFragment(mapFragment,index);
                    } else {
                        if (mapFragment.isHidden()) {
                            showFragment(mapFragment,index);
                        }
                    }
                    break;
                }
                case 3 :{
                    // 社交界面
                    if (communityFragment == null) {
                        communityFragment = new CommunityFragment();
                        // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                        addFragment(communityFragment);
                        showFragment(communityFragment,index);
                    } else {
                        if (communityFragment.isHidden()) {
                            showFragment(communityFragment,index);
                        }
                    }
                    break;
                }
                case 4 :{
                    // 用户界面
                    if (mineFragment == null) {
                        mineFragment = new MineFragment();
                        // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                        addFragment(mineFragment);
                        showFragment(mineFragment,index);
                    } else {
                        if (mineFragment.isHidden()) {
                            showFragment(mineFragment,index);
                        }
                    }
                    break;
                }

            }
        }
    }

    /** 显示Fragment **/
    public void showFragment(Fragment fragment,int index) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        // 设置Fragment的切换动画
        if(index>currentpos)
            ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);
        else
            ft.setCustomAnimations(R.anim.cu_push_left_in,R.anim.cu_push_right_out);
        // 判断页面是否已经创建，如果已经创建，那么就隐藏掉
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (cartFragment != null) {
            ft.hide(cartFragment);
        }
        if (communityFragment != null) {
            ft.hide(communityFragment);
        }
        if (mapFragment != null) {
            ft.hide(mapFragment);
        }
        if (mineFragment != null) {
            ft.hide(mineFragment);
        }

        ft.show(fragment);
        currentpos = index;
        ft.commitAllowingStateLoss();

    }
    //初始化推送服务
    private void initPush(int userid){
        String alias = String.valueOf(userid);
        PushManager pm = PushManager.getInstance();
        pm.initialize(this.getApplicationContext());
        boolean result = pm.bindAlias(this,alias);
        Log.d("main",String.valueOf(result));
    }
    //判断是否已经登录过
    private void getUserfromspf(){
        SharedPreferences spf = getSharedPreferences("user", MODE_PRIVATE);
        String password = spf.getString("password", "");
        String phone = spf.getString("mobilephone", "");
        Call<User> call = service.getUserInfo(phone, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    User result = response.body();
                    if(result.getLoginStatus()==1){
                        User user = MyApplication.getUser();
                        user.setLoginStatus(result.getLoginStatus());
                        user.setUserId(result.getUserId());
                        user.setUserName(result.getUserName());
                        user.setAmount(result.getAmount());
                        user.setUserPicUrl(result.getUserPicUrl());
                        user.setPickloclist(result.getPickloclist());
                        user.setVendorid(result.getVendorid());
                        //登录后启动推送服务
                        initPush(result.getUserId());
                        new GetMsgThread(MainActivity.this,user.getUserId()).start();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private class GetMsgThread extends Thread{
        int userid;
        Context context;
        public GetMsgThread(Context context,int userid){
            this.userid = userid;
            this.context = context;
        }
        @Override
        public void run() {
            super.run();
            DBService dbService = new DBService(context);
            dbService.getLatestMsgs(userid);
        }
    }
}
