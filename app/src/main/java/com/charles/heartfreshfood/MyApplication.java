package com.charles.heartfreshfood;

import android.app.Application;

import com.charles.heartfreshfood.model.CartItem;
import com.charles.heartfreshfood.model.ChatMessage;
import com.charles.heartfreshfood.model.Material;
import com.charles.heartfreshfood.model.User;
import com.charles.heartfreshfood.model.UserBrief;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Charles on 16/4/20.
 */
public class MyApplication extends Application{
    public static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final int RECIPE_ADD = 4;//菜谱步骤添加
    private static HashMap<String,CartItem> cart;//购物车全局变量
    private static HashMap<Integer,ArrayList<ChatMessage>> msgMap;
    private static int cityid;//随时根据定位确定地点
    private static User user;
    private static HashMap<Integer,ChatMessage> lastMessages;
    private static HashMap<Integer,UserBrief> userBriefs;
    private static String clientid = "";
    private static ArrayList<Material> buyList;
    public static String PICTURE_DIR = "sdcard/HeartFresh/pictures/";
    @Override
    public void onCreate() {
        super.onCreate();
        cart = new HashMap<>();
    }
    public static HashMap<String,CartItem> getCart(){
        if(cart == null)
            cart = new HashMap<>();
        return cart;
    }
    public static String getClientid(){
        return clientid;
    }
    public static void setClientid(String id){
        clientid = id;
    }
    public static int getCityid(){
        cityid = 5;
        return cityid;
    }
    public static User getUser(){
        if(user == null)
            user = new User();
        return user;
    }
    //购物清单
    public static ArrayList<Material> getBuyList(){
        if(buyList==null)
            buyList = new ArrayList<>();
        return buyList;
    }
    //最新消息列表
    public static HashMap<Integer,ChatMessage> getlastMessages(){
        if(lastMessages==null)
            lastMessages = new HashMap<>();
        return lastMessages;
    }
    //
    public static HashMap<Integer,ArrayList<ChatMessage>> getMsgMap(){
        if(msgMap==null)
            msgMap = new HashMap<>();
        return msgMap;
    }
    public static HashMap<Integer,UserBrief> getUserBriefs(){
        if(userBriefs==null)
            userBriefs = new HashMap<>();
        return userBriefs;
    }

    public static void logout(){
        user = null;
    }
}
