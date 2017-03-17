package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.charles.heartfreshfood.R;

/**
 * Created by Charles on 16/4/27.
 */
public class CircleHeadView extends RelativeLayout {
    public CircleHeadView(Context context) {
        super(context);
    }

    public CircleHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_image_circle, this);
        //setWidth(50);
       // setHeight(50);
    }
    public void setHeight(int height){
        setHeight(height);
    }
    public void setWidth(int width){
        setWidth(width);
    }
}
