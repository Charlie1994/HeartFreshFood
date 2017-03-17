package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.Comment;
import com.charles.heartfreshfood.util.HttpUrls;

/**
 *  用户评价条
 */
public class ReviewItemView extends RelativeLayout {
    private TextView username;
    private TextView reviewContent;
    private TextView time;
    private RatingBar rateBar;
    private ImageView img;
    private Context context;
    public ReviewItemView(Context context) {
        super(context);
    }

    public ReviewItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_review_item_layout,this);
        username = (TextView)findViewById(R.id.view_review_item_username);
        reviewContent = (TextView)findViewById(R.id.view_review_item_content);

        rateBar = (RatingBar)findViewById(R.id.view_review_item_rate);
    }

    public void setResource(Comment comment){
         String name = comment.getUsername();
         String imgUrl = comment.getImgUrl();
         String content = comment.getContent();
         String date = comment.getDate();
        username.setText(name);
        Glide
              .with(context)
              .load(HttpUrls.LOCAL_URL+imgUrl)
              .into(img);
        reviewContent.setText(content);
        time.setText(date);
    }

}
