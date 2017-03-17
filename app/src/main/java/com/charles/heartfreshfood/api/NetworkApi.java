package com.charles.heartfreshfood.api;

import com.charles.heartfreshfood.model.ChatMessage;
import com.charles.heartfreshfood.model.CustomerAddress;
import com.charles.heartfreshfood.model.Imgurl;
import com.charles.heartfreshfood.model.Order;
import com.charles.heartfreshfood.model.PicUploadStatus;
import com.charles.heartfreshfood.model.PickingLocation;
import com.charles.heartfreshfood.model.PostBrief;
import com.charles.heartfreshfood.model.PostComment;
import com.charles.heartfreshfood.model.PostDetail;
import com.charles.heartfreshfood.model.ProductInfo;
import com.charles.heartfreshfood.model.ProductProfile;
import com.charles.heartfreshfood.model.RecipeBrief;
import com.charles.heartfreshfood.model.RecipeDetail;
import com.charles.heartfreshfood.model.Review;
import com.charles.heartfreshfood.model.User;
import com.charles.heartfreshfood.model.UserBrief;
import com.charles.heartfreshfood.model.VendorLocationProfile;
import com.charles.heartfreshfood.model.VendorProfile;
import com.charles.heartfreshfood.util.HttpUrls;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * 本程序使用retrofit作为网络传输工具
 */
public interface NetworkApi {
    /*public network ip*/
    //"http://114.215.121.201:8080/HeartFreshFoodServer/"
    /*local test ip
    * "http://192.168.1.101:8080/HeartFreshFoodServer/"
    */
    public static Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create())
          .baseUrl(HttpUrls.LOCAL_URL)
          .build();
    // 登录
    @POST("Login.do")
    Call<User> getUserInfo(@Query("phone") String phone,
                           @Query("password") String password);
    //商家得到用户编号
    @GET("ChatServlet")
    Call<String> getvendor2User(@Query("vendorid")String vendorid);
    //取得首页滚动播放条数据
    @GET("FrontpageService.do?block=banner")
    Call<ArrayList<String>> getBannerList();

    //取得首页推荐商家
    @GET("FrontpageService.do?block=vendor")
    Call<ArrayList<VendorProfile>> getHomeVendorList(@Query("city") String city);

    //取得首页推荐产品
    @GET("FrontpageService.do?block=product")
    Call<ArrayList<ProductProfile>> getHomeProductList(@Query("city") String city);

    //取得城市列表
    @GET("FrontpageService.do?block=citylist")
    Call<HashMap<String,String>> getCityMap();

    //取得产品详细的信息
    @GET("Product.do")
    Call<ProductInfo> getProductInfo(@Query("productid") String productid);

    //取得供应商地理位置
    @GET("Map.do?type=vendor")
    Call<ArrayList<VendorLocationProfile>> getVendorLocationProfile(@Query("cityid") int cityid);

    //取得收获点地理位置
    @GET("Map.do?type=pick")
    Call<ArrayList<PickingLocation>> getPickingLocation(@Query("cityid") int cityid);


    //用户支付操作
    @POST("CheckPayServlet")
    Call<String> payOrder(@Query("userid") int userid,@Query("password") String password,@Query("type") String type,@Query("amount") double amount,@Query("pickid") int pickid,@Query(value="cart",encoded = true) String cart);
    //用户确认收货
    @POST("CheckPayServlet")
    Call<String> confirmPay(@Query("userid") int userid,@Query("password") String password,@Query("type")String type,@Query("orderid") String orderid);

    //取得用户收货表信息
    @GET("AddressServlet?type=get")
    Call<ArrayList<CustomerAddress>> getPickingList(@Query("userid") int userid);

    //添加收货点地址
    @GET("AddressServlet?type=add")
    Call<String> addPickLoc(@Query("userid") int userid,@Query("picklocid") int picklocid,@Query("pickname") String pickname);

    //得到默认收货点
    @GET("AddressServlet?type=getdefault")
    Call<CustomerAddress> getDefaultPick(@Query("userid") int userid);

    //上传头像
    @Multipart
    @POST("MyHeadUploadServlet")
    Call<Imgurl> upLoadMyimg(@Part("file\"; filename=\"1\"")RequestBody img,@Part("extension") String extension,@Part("userid") int userid);
    //修改个人资料
    //获得我的订单信息
    @POST("QueryOrderServlet")
    Call<ArrayList<Order>> getMyOrders(@Query("userid") int userid);
    //上传菜谱详细步骤
    @Multipart
    @POST("RecipeStepUploadServlet")
    Call<PicUploadStatus> uploadRecipeStep(@Part("extension") String extension,@Part("recipeid") String recipeid,@Part("introduction") String introduction,@Part("step") int step,@Part("num")int num,@Part("file\"; filename=\"1\"")RequestBody img);
    //上传菜谱用料
    @POST("RecipeServlet")
    Call<String> uploadMaterial(@Query("recipeid") String recipeid, @Query("materiallist") String json,@Query("type") String type);
    //上传菜谱信息
    @POST("RecipeServlet")
    Call<String> uploadRecipeinfo(@Query("recipeid") String recipeid,@Query(value = "recipejson",encoded = true)String json,@Query("imgurl")String imgurl,@Query("num")int num,@Query("userid")int userid);
    //得到菜谱
    @POST("QueryRecipeServlet")
    Call<RecipeDetail> getRecipeDetail(@Query("recipeid") String recipeid);
    //获得菜谱
    @GET("QueryRecipeServlet?type=all")
    Call<ArrayList<RecipeBrief>> getRecipelist();
    //发帖不带图片
    @POST("ForumTextServlet")
    Call<String> uploadPostText(@Query("title")String title,@Query("content")String content,@Query("cateid")int cateid,@Query("userid")int userid);
    //发帖带图片
    @Multipart
    @POST("ForumPicServlet")
    Call<String> uploadPostPic(@Part("title")String title,@Part("content")String content,@Part("cateid")int cateid,@Part("userid")int userid,@Part("extension")String extension,@Part("file\"; filename=\"1\"")RequestBody img);
    //浏览帖子
    @POST("ForumnServlet")
    Call<ArrayList<PostBrief>> getPostBrief(@Query("cateid")int cateid,@Query("type")String type,@Query("page")int page);
    //回帖带图
    @Multipart
    @POST("ForumnCommentServlet")
    Call<String> uploadPostCommentwithpic(@Part("postid")String postid,@Part("userid")int userid,@Part("content")String content,@Part("file\"; filename=\"1\"")RequestBody img,@Part("extension")String extension);
    //回帖不带图
    @Multipart
    @POST("ForumnCommentServlet")
    Call<String> uploadPostCommentnopic(@Part("postid")String postid,@Part("userid")int userid,@Part("content")String content);
    //得到帖子详细
    @GET("ForumnServlet?type=detail")
    Call<PostDetail> getPostDetail(@Query("postid")String postid);
    //得到帖子评论
    @GET("ForumnServlet?type=comment")
    Call<ArrayList<PostComment>> getPostComments(@Query("PostID")String postid,@Query("page")int page);
    //商家页面商家详细信息
    @GET("vendor.do?type=vendorinfo")
    Call<VendorProfile> getVendorInfo(@Query("vendorid") String vendorid);
    //取得商家全部商品
    @GET("vendor.do?type=product")
    Call<ArrayList<ProductProfile>> getVendorProduct(@Query("vendorid") String vendorid);
    //取得商家三个商品图片
    @POST("vendor.do?type=productimg")
    Call<ArrayList<String>> getThreeProducts(@Query("vendorid") String vendorid);
    //取得商家页面的评价信息
    @GET("vendor.do?type=review")
    Call<ArrayList<Review>> getComment(@Query("vendorid") String vendorid);

    //用户产品评价上传
    @POST("OrderReviewServlet?type=comment")
    Call<String> uploadReview(@Query(value = "commentjson",encoded = true)String json);

    //产品评价查询
    @GET("QueryReviewServlet")
    Call<ArrayList<Review>> getReviewList(@Query("productid")String productid,@Query("page")int page);

    //取得用户的离线消息
    @POST("ChatServlet?type=getmessage")
    Call<ArrayList<ChatMessage>> getMessages(@Query("userid") int userid);

    //发送消息
    @POST("ChatServlet?type=sendmessage")
    Call<String> sendMessage(@Query(value="msg",encoded = true)String msg);

    //得到二维码扫描后的网址信息
    @GET("QueryOrderServlet")
    Call<Order> getCodeOrder(@Query("orderid")String orderid);

    //发货
    @POST("WebModifyOrderServlet?type=deliver")
    Call<String> deliverOrder(@Query("orderids")String orderid);

    //用户收货
    @POST("WebModifyOrderServlet?type=customerpick")
    Call<String> pickOrder(@Query("orderids")String orderid);

    //得到商家用户编号
    @GET("vendor.do?type=userid")
    Call<Integer> getVendor2Userid(@Query("vendorid")String vendorid);

    //得到用户头像
    @GET("UserServlet")
    Call<UserBrief> getUserBrief(@Query("userid")int userid);

    //设置默认收货地址
    @GET("AddressServlet?type=setdefault")
    Call<String> setDefaultPick(@Query("userid")int userid,@Query("pickid")int pickid);

    //删除收货地址
    @GET("AddressServlet?type=delete")
    Call<String> deletePick(@Query("userid")int userid,@Query("picklocid")int pickid);

    //取得默认取货id
    @GET("AddressServlet?type=getdefaultid")
    Call<Integer> getDefaultPickid(@Query("userid")int userid);
    //得到商家列表信息
    @POST("QueryVendorListServlet")
    Call<ArrayList<VendorProfile>> getVendors(@Query("cityid")int cityid);

    //得到产品
    @GET("QueryProductServlet")
    Call<ArrayList<ProductProfile>> getProducts(@Query("productname")String productname,@Query("page")int page);

    //收货
    @POST("PickServlet")
    Call<String> pick(@Query("orderid")String orderid);
}
