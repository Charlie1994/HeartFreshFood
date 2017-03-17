package com.charles.heartfreshfood.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.CartItem;
import com.charles.heartfreshfood.model.ProductInfo;
import com.charles.heartfreshfood.model.UserBrief;
import com.charles.heartfreshfood.wiget.PictureRecycleView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProductActivity extends AppCompatActivity {
    @Bind(R.id.product_minus)
    Button minusBtn;
    @Bind(R.id.product_num)
    TextView productNum;
    @Bind(R.id.product_plus)
    Button plusBtn;
    @Bind(R.id.product_cart)
    Button cartBtn;
    @Bind(R.id.picture_view)
    PictureRecycleView picViewPager;
    @Bind(R.id.prodetail_name)
    TextView productName;
    @Bind(R.id.prodetail_specification)
    TextView specification;//规格
    @Bind(R.id.prodetail_price)
    TextView price;
    @Bind(R.id.prodetail_back)
    ImageButton backbtn;
    @Bind(R.id.prodetail_rate)
    RatingBar rateBar;
    private ProductInfo productInfo;
    private View view;
    private ArrayList<String> urlList;//产品图片列表
    private int num = 1;//产品计数
    private String productid;
    private HashMap<String, CartItem> cart;
    private double productprice;
    private int vendoruserid = 0;
    private UserBrief brief;
    private HashMap<Integer,UserBrief> contactmap;
    NetworkApi service = NetworkApi.retrofit.create(NetworkApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);
        contactmap = MyApplication.getUserBriefs();
        cart = MyApplication.getCart();
        productid = getIntent().getStringExtra("productid");
        getProductInfo();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
        RelativeLayout commentBtn = (RelativeLayout) findViewById(R.id.prodetail_comment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, ProductReviewShowActivity.class);
                intent.putExtra("productid", productid);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
        RelativeLayout contactBtn = (RelativeLayout)findViewById(R.id.prodetail_contact);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyApplication.getUser().getLoginStatus()!=0) {
                    if (vendoruserid != 0 && productInfo != null) {
                        if(contactmap.containsKey(vendoruserid)){
                            Intent intent = new Intent(ProductActivity.this, ChatDetailActivity.class);
                            intent.putExtra("brief", contactmap.get(vendoruserid));
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        }else{
                            Toast.makeText(ProductActivity.this, "对不起，您的网络出现问题", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProductActivity.this, "对不起，您的网络出现问题", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
            }
        });
    }


    /*
     * 根据点击传过来的productid进行异步网络操作
     */
    public void getProductInfo() {
        Call<ProductInfo> call1 = service.getProductInfo(productid);
        call1.enqueue(new Callback<ProductInfo>() {
            ProgressDialog dialog = ProgressDialog.show(ProductActivity.this, "爱心鲜", "加载中……");

            @Override
            public void onResponse(Response<ProductInfo> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                    // 请求成功 (状态码 200, 201)
                    if (response.body() != null) {
                        productInfo = response.body();
                        setupViews();
                        getVendorContact(productInfo.getVendorID());
                    }

                } else {
                    //请求不成功 (like 400,401,403 etc)
                    //Handle errors
                    Log.d("productactivity", "请求失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                Log.d("productactivity", "发送失败");
            }
        });
    }

    private void getVendorContact(String vendorid){
        Call<Integer> call = service.getVendor2Userid(vendorid);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Response<Integer> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    vendoruserid = response.body();
                    getUserBrief(vendoruserid);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getUserBrief(final int userid){
        if(!contactmap.containsKey(userid)){
            Call<UserBrief> call = service.getUserBrief(userid);
            call.enqueue(new Callback<UserBrief>() {
                @Override
                public void onResponse(Response<UserBrief> response, Retrofit retrofit) {
                    if(response.isSuccess()){
                        contactmap.put(userid,response.body());
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }
    /*
     * 设置各个控件的值
     */
    public void setupViews() {
        picViewPager.setImgUrlList(productInfo.getPicUrlList());
        picViewPager.launch();
        productName.setText(productInfo.getProductName());
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        price.setText("￥" + String.valueOf(dcmFmt.format(productInfo.getPrice())));
        specification.setText(productInfo.getSpecification());
        productNum.setText(String.valueOf(num));
    }

    @OnClick({R.id.product_minus, R.id.product_plus, R.id.product_cart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.product_minus: {
                if (num == 1)
                    Toast.makeText(ProductActivity.this, "不能再减少了", Toast.LENGTH_SHORT).show();
                else {
                    num--;
                    String s = String.valueOf(num);
                    productNum.setText(s);
                }
                break;
            }

            case R.id.product_plus: {
                num++;
                String s = String.valueOf(num);
                productNum.setText(s);
                break;
            }
            case R.id.product_cart: {
                if (cart.containsKey(productid)) {
                    CartItem item = cart.get(productid);
                    int n = item.getNumber();
                    n = n + num;
                    item.setNumber(n);
                    item.setSubtotal(n * productprice);
                    cart.put(productid, item);
                } else {
                    CartItem cartItem = new CartItem(productid, productInfo.getVendorID(), productInfo.getVendorname(),
                          productInfo.getProductName(), productInfo.getSpecification(), productInfo.getPrice(),
                          num, productInfo.getImgurl(), num * productprice);
                    cart.put(productid, cartItem);
                }
                Toast.makeText(this, "已经添加到购物车了", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
