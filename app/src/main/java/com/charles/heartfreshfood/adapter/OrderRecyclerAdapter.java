package com.charles.heartfreshfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.wiget.ConfirmOrderItemView;
import com.charles.heartfreshfood.model.CartItem;

import java.util.ArrayList;

/**
 *  订单的商品项目显示适配器
 */
public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder>{
    private ArrayList<CartItem> mData;

    public OrderRecyclerAdapter(ArrayList<CartItem> mData) {
        this.mData = mData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.order_recycler_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.orderItemView.setViews(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
          implements View.OnClickListener{

        public ConfirmOrderItemView orderItemView;
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            orderItemView = (ConfirmOrderItemView) v;
        };

        @Override
        public void onClick(View v) {

        }
    }
}
