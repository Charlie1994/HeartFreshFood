package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.RecipeStep;

/**
 *  菜谱步骤展示项
 */
public class RecipeStepShowView extends RelativeLayout {
    View view;
    Context context;
    public RecipeStepShowView(Context context) {
        super(context);
    }

    public RecipeStepShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.view_step_rercipe_show,this);
    }

    public void setViews(RecipeStep step){

    }
}
