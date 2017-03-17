package com.charles.heartfreshfood.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.CartItem;
import com.charles.heartfreshfood.model.User;
import com.charles.heartfreshfood.util.HttpUrls;
import com.charles.heartfreshfood.wiget.CircleHeadView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
public class PayActivity extends AppCompatActivity {
    User user;
    @Bind(R.id.pay_img)
    CircleHeadView payImg;
    @Bind(R.id.pay_username_tv)
    TextView payUsernameTv;
    @Bind(R.id.pay_password)
    EditText payPassword;
    @Bind(R.id.pay_btn)
    Button payBtn;
    @Bind(R.id.recharge_btn)
    Button rechargeBtn;

    String cartjson;
    private double orderamount;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        user = MyApplication.getUser();
        cartjson = getIntent().getStringExtra("cartjson");
        orderamount = getIntent().getDoubleExtra("orderamount",0);
        initViews();
    }
    private void initViews(){
        payImg.setImg(HttpUrls.LOCAL_URL+MyApplication.getUser().getUserPicUrl());
        payUsernameTv.setText(MyApplication.getUser().getUserName());
        TextView title = (TextView)findViewById(R.id.view_banner_head_title);
        title.setText("付款");
        TextView leftbtn = (TextView)findViewById(R.id.view_banner_head_left);
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });

    }



    @OnClick({R.id.pay_btn, R.id.recharge_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_btn:
                try {
                    String type = getIntent().getStringExtra("type");
                    int id = user.getUserId();
                    String pwd = payPassword.getText().toString().trim();
                    NetworkApi service = NetworkApi.retrofit.create(NetworkApi.class);
                    if(type.equals("order")){
                        int pickid = getIntent().getIntExtra("pickid", 0);
                        final String encodedData = URLEncoder.encode(cartjson, "UTF-8");
                        Log.d("pay",encodedData);
                        Call<String> call = service.payOrder(id, pwd,"order",orderamount,pickid, encodedData);
                        new PayTask().execute(call);
                    }
                    if(type.equals("confirm")){
                        String orderid = getIntent().getStringExtra("orderid");
                        Call<String> call = service.confirmPay(id,pwd,"confirm",orderid);
                        new PayTask().execute(call);
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                break;
            case R.id.recharge_btn:
                break;
        }
    }


    private class PayTask extends AsyncTask<Call, Void, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(PayActivity.this, "HeartFresh", "支付中...");
        }

        @Override
        protected String doInBackground(Call... params) {
            try {
                return (String) params[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).
                        show();
                dialog.dismiss();
                HashMap<String,CartItem> cart = MyApplication.getCart();
                cart.clear();
                try{
                Thread.sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(PayActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            } else{
                if(result.equals("fail"))
                    Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).
                          show();
                if(result.equals("wrong"))
                    Toast.makeText(PayActivity.this, "密码错误", Toast.LENGTH_SHORT).
                          show();
            }


        }
    }
}
