package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.CartItem;
import com.charles.heartfreshfood.util.HttpUrls;

/**
 * 确认订单产品项目布局
 */
public class ConfirmOrderItemView extends LinearLayout {
    Context context;
    ImageView orderItemProductimg;
    TextView orderItemProductname;
    TextView orderItemSpecification;
    TextView orderItemPrice;
    TextView orderItemNumber;
    EditText orderItemMessage;

    public ConfirmOrderItemView(Context context) {
        super(context);
    }

    public ConfirmOrderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_order_item, this);
        orderItemProductimg = (ImageView) findViewById(R.id.view_order_item_productimg);
        orderItemProductname = (TextView)findViewById(R.id.view_order_item_productname);
        orderItemSpecification = (TextView)findViewById(R.id.view_order_item_specification);
        orderItemPrice = (TextView)findViewById(R.id.view_order_item_price);
        orderItemNumber = (TextView)findViewById(R.id.view_order_item_number);
        orderItemMessage = (EditText) findViewById(R.id.view_order_item_message);

    }

    public void setViews(CartItem item) {
        Glide
              .with(context)
              .load(HttpUrls.LOCAL_URL+item.getUrl())
              .centerCrop()
              .into(orderItemProductimg);
        orderItemMessage.clearFocus();
        orderItemProductname.setText(item.getProductName());
        orderItemSpecification.setText(item.getSpecification());
        orderItemPrice.setText("￥"+String.valueOf(item.getPrice()));
        orderItemNumber.setText("x"+item.getNumber());
    }
}
