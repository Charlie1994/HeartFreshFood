package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.ProductProfile;
import com.charles.heartfreshfood.util.HttpUrls;

/**
 * 产品展示块
 */
public class ProductCardView extends CardView{
    TextView cardName;
    TextView cardPrice;
    TextView cardSellamount;
    CardView productCardview;
    ImageView cardImg;
    Context context;
    private ProductProfile productProfile;

    public ProductCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.productcard_layout, this);
        init();
    }


    private void init(){
        cardName = (TextView)findViewById(R.id.procard_name);
        cardPrice = (TextView)findViewById(R.id.procard_price);
        cardSellamount = (TextView)findViewById(R.id.procard_sellamount);
        cardImg = (ImageView)findViewById(R.id.card_img);
    }
    public void setCardInfo(ProductProfile profile){
        this.productProfile = profile;
        Glide
              .with(context)
              .load(HttpUrls.LOCAL_URL+profile.getProductImgUrl())
              .fitCenter()
              .into(cardImg);
        cardName.setText(profile.getProductName());
        String strprice = "￥" + String.valueOf(profile.getProductPrice());
        cardPrice.setText(strprice);
        String strNumber = String.valueOf(profile.getPurchaseNumber()) + "人付款";
        cardSellamount.setText(strNumber);
    }


}
