package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.RecipeBrief;
import com.charles.heartfreshfood.util.HttpUrls;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 菜谱显示类
 */
public class RecipeItemView extends RelativeLayout {
    Context context;
    TextView viewItemRecipeTitle;
    TextView viewItemRecipeCollectnum;
    ImageView viewItemRecipeTitleimg;
    CircleHeadView viewItemRecipeUserimg;
    TextView usernameTv;
    RecipeBrief brief;

    public RecipeItemView(Context context) {
        super(context);
    }

    public RecipeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_item_recipe_layout, this);
        viewItemRecipeTitle = (TextView)findViewById(R.id.view_item_recipe_title);
        viewItemRecipeCollectnum = (TextView)findViewById(R.id.view_item_recipe_collectnum);
        viewItemRecipeUserimg = (CircleHeadView)findViewById(R.id.view_item_recipe_userimg);
        viewItemRecipeTitleimg = (ImageView)findViewById(R.id.view_item_recipe_titleimg);
        usernameTv = (TextView)findViewById(R.id.view_item_recipe_username);
    }
    public void setViews(RecipeBrief rb){
        brief = rb;
        viewItemRecipeTitle.setText(rb.getRecipename());
        viewItemRecipeCollectnum.setText(String.valueOf(rb.getCollectnum())+"人收藏");
        usernameTv.setText(rb.getUsername());
        Glide
              .with(context)
              .load(HttpUrls.LOCAL_URL+rb.getRecipeurl())
              .bitmapTransform(new RoundedCornersTransformation(context, 30, 0))
              .into(viewItemRecipeTitleimg);
        viewItemRecipeUserimg.setImg(HttpUrls.LOCAL_URL+rb.getUserurl());
    }
}
