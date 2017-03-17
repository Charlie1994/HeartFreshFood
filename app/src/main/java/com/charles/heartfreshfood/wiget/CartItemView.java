package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.activity.ProductActivity;
import com.charles.heartfreshfood.fragment.CartFragment;
import com.charles.heartfreshfood.model.CartItem;
import com.charles.heartfreshfood.util.HttpUrls;

import java.util.HashMap;


/**
 *  购物车单项布局
 */
public class CartItemView extends RelativeLayout implements View.OnClickListener{

    private CartItem cartItem;
    private ImageView img;
    private TextView productName;
    private TextView specification;
    private TextView price;
    public TextView number;
    public ImageButton minus;
    public ImageButton plus;
    private Context context;
    private String productid;
    private int productnum;
    public CartItemView(Context context) {
        super(context);
    }

    public CartItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_item_cart,this);
        productName = (TextView)findViewById(R.id.view_cart_item_productname);
        specification = (TextView)findViewById(R.id.view_cart_item_specification);
        price = (TextView)findViewById(R.id.view_cart_item_price);
        number = (TextView)findViewById(R.id.view_cart_item_number);
        minus = (ImageButton)findViewById(R.id.view_cart_item_minus);
        plus = (ImageButton)findViewById(R.id.view_cart_item_plus);
        img = (ImageView)findViewById(R.id.view_cart_item_img);
    }
    public void setCart(CartItem item, final CartFragment fragment){
        this.cartItem = item;
        productid = item.getProductid();
        String url = item.getUrl();
        Glide
              .with(context)
              .load(HttpUrls.LOCAL_URL+url)
              .fitCenter()
              .into(img);
        productName.setText(item.getProductName());
        specification.setText(item.getSpecification());
        String p = "￥"+String.valueOf(item.getPrice());
        price.setText(p);
        String n = String.valueOf(item.getNumber());
        number.setText(n);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productnum = cartItem.getNumber();
                if (productnum > 1) {
                    productnum--;
                    HashMap<String, CartItem> cartMap = MyApplication.getCart();
                    cartItem.setNumber(productnum);
                    cartMap.put(cartItem.getProductid(), cartItem);
                    number.setText(String.valueOf(productnum));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                    builder.setMessage("是否删除此商品？");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, CartItem> cartMap = MyApplication.getCart();
                            cartMap.remove(cartItem.getProductid());
                            fragment.getCart();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                fragment.getCart();

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productnum = cartItem.getNumber();
                productnum++;
                number.setText(String.valueOf(productnum));
                cartItem.setNumber(productnum);
                HashMap<String, CartItem> cartMap = MyApplication.getCart();
                cartMap.put(cartItem.getProductid(), cartItem);
                fragment.getCart();
            }
        });
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ProductActivity.class);
        String productid = cartItem.getProductid();
        intent.putExtra("productid",productid);
        getContext().startActivity(intent);
    }
}
