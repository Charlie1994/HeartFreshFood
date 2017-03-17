package com.charles.heartfreshfood.test;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.fragment.PostFragment;

public class TestFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        PostFragment flag = new PostFragment();
        ft.add(R.id.test_fragment,flag);
        ft.commit();

    }
}
