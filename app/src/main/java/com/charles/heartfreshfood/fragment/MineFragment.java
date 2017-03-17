package com.charles.heartfreshfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.activity.AddressActivity;
import com.charles.heartfreshfood.activity.LoginActivity;
import com.charles.heartfreshfood.activity.MyOrderActivity;
import com.charles.heartfreshfood.activity.MyPicUploadActivity;
import com.charles.heartfreshfood.model.User;
import com.charles.heartfreshfood.util.HttpUrls;
import com.charles.heartfreshfood.wiget.CircleHeadView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;


public class MineFragment extends Fragment {
    @Bind(R.id.mine_order)
    Button mineOrder;
    @Bind(R.id.mine_comment)
    Button mineComment;
    @Bind(R.id.mine_address)
    Button mineAddress;


    @Bind(R.id.mine_setting)
    Button mineSetting;
    @Bind(R.id.mine_balance)
    TextView mineBalance;
    private View view;
    private ImageView loginBtn;
    private CircleHeadView userHead;
    private User user;
    private ImageView background;
    private String TAG = "Minefragment";
    private boolean islogined = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        user = MyApplication.getUser();
        if (user.getLoginStatus() == 1)
            islogined = true;
        else
            islogined = false;
        loginBtn = (ImageView) view.findViewById(R.id.mine_login);
        userHead = (CircleHeadView) view.findViewById(R.id.user_img);
        background = (ImageView) view.findViewById(R.id.mine_background);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
        int x = user.getLoginStatus();
        if (islogined) {
            loginBtn.setVisibility(View.GONE);
            Log.d(TAG, HttpUrls.LOCAL_URL + user.getUserPicUrl());
            userHead.setImg(HttpUrls.LOCAL_URL + user.getUserPicUrl());
            userHead.setVisibility(View.VISIBLE);
            Glide
                  .with(getActivity())
                  .load(HttpUrls.LOCAL_URL + user.getUserPicUrl())
                  .bitmapTransform(new BlurTransformation(getActivity()))
                  .into(background);
        } else {
            Glide
                  .with(getActivity())
                  .load(R.drawable.pic_mine_background)
                  .bitmapTransform(new BlurTransformation(getActivity(), 10))
                  .into(background);
            loginBtn.setVisibility(View.VISIBLE);
            userHead.setVisibility(View.GONE);
        }
        mineBalance.setText(String.valueOf(user.getAmount())+"元");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.mine_setting, R.id.mine_order, R.id.mine_comment, R.id.mine_address})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.mine_order:
                if (islogined) {
                    intent = new Intent(getActivity(), MyOrderActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }

                break;
            case R.id.mine_comment:
                break;
            case R.id.mine_address:
                if (user.getLoginStatus() == 1) {
                    intent = new Intent(getActivity(), AddressActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.mine_setting:
                intent = new Intent(getActivity(), MyPicUploadActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
        }
    }

}
