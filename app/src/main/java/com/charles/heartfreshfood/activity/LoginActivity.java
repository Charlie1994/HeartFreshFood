package com.charles.heartfreshfood.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.charles.heartfreshfood.model.User;
import com.charles.heartfreshfood.util.DBService;
import com.igexin.sdk.PushManager;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {


    String phone;
    String password;

    private TextView leftbtn;
    private TextView title;
    @Bind(R.id.username)
    EditText loginusername;
    @Bind(R.id.password)
    EditText loginpassword;
    @Bind(R.id.login_btn)
    Button loginBtn;
    @Bind(R.id.register_btn)
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initHead();
    }

    //click event
    @OnClick({R.id.login_btn, R.id.register_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn: {
                phone = loginusername.getText().toString().trim();
                password = loginpassword.getText().toString();
                login(phone, password);
                break;
            }
            case R.id.register_btn:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
        }
    }

    private void initHead(){
        leftbtn = (TextView)findViewById(R.id.view_banner_head_left);
        title = (TextView)findViewById(R.id.view_banner_head_title);
        title.setText("登录爱心鲜");
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
    }
    //sign in
    public void login(String name, String pwd) {
        Retrofit retrofit = NetworkApi.retrofit;
        NetworkApi service = retrofit.create(NetworkApi.class);
        Call<User> call = service.getUserInfo(name, pwd);
        new LoginTask().execute(call);
    }

    private void addSharedPreference(){
        //登录成功后将这个数据放到sharedprefernence里面
        SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("password", password);
        editor.putString("mobilephone",phone);
        editor.apply();
        SharedPreferences spf= getSharedPreferences("user", MODE_PRIVATE);
        int id = spf.getInt("userid", 0);
        Log.d("chat", String.valueOf(id));
    }

    private class LoginTask extends AsyncTask<Call, Void, User> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LoginActivity.this,"爱心鲜","登录中……");
        }

        @Override
        protected User doInBackground(Call... params) {
            try {
                return (User) params[0].execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(User result) {
            dialog.dismiss();
            if (result.getLoginStatus() == 1) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).
                        show();
                User user = MyApplication.getUser();
                user.setLoginStatus(result.getLoginStatus());
                user.setUserId(result.getUserId());
                user.setUserName(result.getUserName());
                user.setUserPicUrl(result.getUserPicUrl());
                user.setPickloclist(result.getPickloclist());
                user.setVendorid(result.getVendorid());
                new GetMsgThread(LoginActivity.this,user.getUserId()).start();
                //存入sharedpreference中
                addSharedPreference();
                PushManager pm = PushManager.getInstance();
                pm.initialize(getApplicationContext());
                boolean r = pm.bindAlias(getApplicationContext(),String.valueOf(user.getUserId()));
                Log.d("login",String.valueOf(r));
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            } else
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).
                        show();
        }
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
