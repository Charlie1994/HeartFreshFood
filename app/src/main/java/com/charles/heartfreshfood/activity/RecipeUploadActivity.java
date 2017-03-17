package com.charles.heartfreshfood.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.PicUploadStatus;
import com.charles.heartfreshfood.model.Recipe;
import com.charles.heartfreshfood.model.RecipeStep;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RecipeUploadActivity extends AppCompatActivity {
    String recipeid;
    @Bind(R.id.activity_recipe_upload_btn)
    Button uploadBtn;
    @Bind(R.id.activity_recipe_upload_tv)
    TextView uploadTv;
    @Bind(R.id.activity_recipe_upload_img)
    ImageView uploadImg;
    ArrayList<RecipeStep> list;
    NetworkApi service;
    Gson gson = new Gson();
    int num;//菜谱步骤数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_upload);
        ButterKnife.bind(this);
        //生成菜单编码
        int tmpi = (int)(Math.random()*5);
        recipeid = UUID.randomUUID().toString().replace("-", "q").substring(tmpi, tmpi + 8);
        service = NetworkApi.retrofit.create(NetworkApi.class);
        initHead();
        initViews();
    }

    private void initHead() {

        TextView leftbtn = (TextView) findViewById(R.id.view_banner_head_left);
        TextView title = (TextView) findViewById(R.id.view_banner_head_title);
        title.setText("上传菜谱");
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
    }

    private void initViews() {
        list = new ArrayList<>();
        HashMap<Integer,RecipeStep> steps = (HashMap<Integer,RecipeStep>)getIntent().getSerializableExtra("steps");
        Iterator iter = steps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            RecipeStep val = (RecipeStep) entry.getValue();
            list.add(val);
        }
        num = list.size();
        Uri uri = Uri.parse(list.get(list.size()-1).getUri());
        Glide
              .with(this)
              .load(uri)
              .bitmapTransform(new RoundedCornersTransformation(this, 15, 0))
              .into(uploadImg);
        uploadTv.setText("成品图");
        uploadBtn.setText("上传菜谱");
    }

    void uploadFile() {

        for(RecipeStep s : list){
            int step = s.getIndex();
            String content = s.getContent();
            String path = getRealPath(Uri.parse(s.getUri()));
            String extension = getExtension(path);
            Log.d("upload",extension);
            Log.d("upload",recipeid);
            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
            Call<PicUploadStatus> call = service.uploadRecipeStep(extension,recipeid,content,step,num,requestBody);
            call.enqueue(new Callback<PicUploadStatus>() {
                @Override
                public void onResponse(Response<PicUploadStatus> response, Retrofit retrofit) {
                    PicUploadStatus result = response.body();
                    if(result.getStatus().equals("fail"))
                        Toast.makeText(RecipeUploadActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                    if(result.getStatus().equals("last")){
                        String imgurl = result.getUrl();
                        uploadRecipe(imgurl);//当最后一张图片录入的时候返回图片路径此时可以开始录入菜谱信息
                    }


                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
    }

    void uploadRecipe(String imgurl) {
        int userid = MyApplication.getUser().getUserId();
        int num = list.size();
        Recipe r = (Recipe)getIntent().getSerializableExtra("recipe");
        String json = "";
        json = gson.toJson(r);
        try{
        final String encodedData = URLEncoder.encode(json,"UTF-8");
        Log.d("recipe",encodedData);
        Call<String> call = service.uploadRecipeinfo(recipeid,encodedData,imgurl,num,userid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                String result = response.body();
                if(result.equals("fail"))
                    Toast.makeText(RecipeUploadActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                if(result.equals("success")) {
                    Toast.makeText(RecipeUploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RecipeUploadActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    /*
     * 得到真实路径
     */
    private String getRealPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    /*
     * 得到扩展名
     */
    private String getExtension(String path){
        return path.substring(path.lastIndexOf("."), path.length());
    }

    @OnClick(R.id.activity_recipe_upload_btn)
    public void onClick() {
        uploadFile();
    }
}
