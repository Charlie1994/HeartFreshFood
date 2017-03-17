package com.charles.heartfreshfood.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.charles.heartfreshfood.R;


public class CommunityFragment extends Fragment {
    private TextView[] btnSet = new TextView[2];
    private View view;
    private RecipeFragment recipeFrag;
    private PostFragment postFrag;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community,container,false);
        initBtn();
        recipeFrag = new RecipeFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.community_fragment_content,recipeFrag);
        ft.commit();
        return view;
    }
    //初始化顶部按键
    private void initBtn(){
        btnSet[0] = (TextView)view.findViewById(R.id.community_top_btn_cook);
        btnSet[1] = (TextView)view.findViewById(R.id.community_top_btn_share);
        for (int i=0;i<2;i++){
            btnSet[i].setOnClickListener(new TopClickListener(i));
        }
    }
    public void showFragment(int index){
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        if(recipeFrag!=null)
            if(!recipeFrag.isHidden())
                ft.hide(recipeFrag);
        if(postFrag!=null)
            if(!postFrag.isHidden())
                ft.hide(postFrag);
        switch (index){
            case 0:
                if(recipeFrag==null) {
                    recipeFrag = new RecipeFragment();
                    ft.add(R.id.community_fragment_content,recipeFrag);
                }
                ft.show(recipeFrag);
                break;
            case 1:
                if(postFrag==null) {
                    postFrag = new PostFragment();
                    ft.add(R.id.community_fragment_content,postFrag);
                }
                ft.show(postFrag);
                break;
            default:
                break;
        }
        ft.commit();
    }
    private class TopClickListener implements View.OnClickListener{
        private int index;
        public TopClickListener(int i){
            this.index = i;
        }
        @Override
        public void onClick(View v) {
            showFragment(index);
            for(int j = 0;j<2;j++) {
                btnSet[j].setTextColor(Color.rgb(50, 50, 50));
                btnSet[j].setBackgroundResource(R.drawable.community_top_button_unselected);
            }
            btnSet[index].setTextColor(Color.rgb(255, 90, 30));
            btnSet[index].setBackgroundResource(R.drawable.community_top_button_selected);
        }
    }
}
