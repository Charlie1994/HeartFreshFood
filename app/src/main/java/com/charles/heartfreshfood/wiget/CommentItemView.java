package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.Review;
import com.charles.heartfreshfood.util.HttpUrls;

/**
 * 用户评价布局
 */
public class CommentItemView extends RelativeLayout {
    CircleHeadView viewReviewItemImg;
    RatingBar viewReviewItemRate;
    TextView viewReviewItemUsername;
    TextView viewReviewItemContent;
    TextView viewReviewItemTime;
    private Context context;

    public CommentItemView(Context context) {
        super(context);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_review_item_layout, this);
        viewReviewItemImg = (CircleHeadView)findViewById(R.id.view_review_item_img);
        viewReviewItemRate = (RatingBar)findViewById(R.id.view_review_item_rate);
        viewReviewItemUsername = (TextView)findViewById(R.id.view_review_item_username);
        viewReviewItemContent = (TextView)findViewById(R.id.view_review_item_content);
        viewReviewItemTime = (TextView)findViewById(R.id.view_review_item_time);
    }
    public void setProductReview(Review review){
        String username = review.getUsername();
        String date = review.getDate();
        String content = review.getContent();
        String url = review.getImgUrl();
        viewReviewItemImg.setImg(HttpUrls.LOCAL_URL+url);
        viewReviewItemUsername.setText(username);
        viewReviewItemContent.setText(content);
        viewReviewItemTime.setText(date);
        viewReviewItemRate.setRating(Float.valueOf(review.getRate()));
        viewReviewItemRate.setVisibility(View.VISIBLE);
    }

    public void setPostComment(){}
}
