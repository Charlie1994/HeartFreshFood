package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.charles.heartfreshfood.R;

/**
 * 用于菜谱的材料添加
 */
public class MaterialItemView extends RelativeLayout{
    private TextView nametv;
    private TextView numtv;
    public MaterialItemView(Context context) {
        super(context);
    }

    public MaterialItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_item_material, this);
        nametv = (TextView)findViewById(R.id.item_material_name);
        numtv = (TextView)findViewById(R.id.item_material_num);
    }
    public void setName(String name){
        nametv.setText(name);
    }
    public void setNum(String num){
        numtv.setText(num);
    }
}
