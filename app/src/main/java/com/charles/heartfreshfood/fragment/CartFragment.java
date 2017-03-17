package com.charles.heartfreshfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.activity.ConfirmOrderActivity;
import com.charles.heartfreshfood.activity.LoginActivity;
import com.charles.heartfreshfood.model.CartItem;
import com.charles.heartfreshfood.wiget.CartItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CartFragment extends Fragment {
    View view;
    ArrayList<CartItem> items;
    CartRecyclerAdapter adapter;
    @Bind(R.id.fragment_cart_list)
    RecyclerView cartList;
    HashMap<String, CartItem> cart;
    final String TAG = "Cartfragment";
    @Bind(R.id.fragment_cart_amount)
    TextView cartAmount;
    @Bind(R.id.fragment_cart_submit)
    RelativeLayout fragmentCartSubmit;
    Context context = getActivity();
    @Bind(R.id.fragment_cart_clear)
    TextView clearBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, null);
        ButterKnife.bind(this, view);
        items = new ArrayList<>();
        cart = MyApplication.getCart();
        setupCart();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cart = MyApplication.getCart();
        if (cart.size() == 0)
            cartAmount.setText("￥0.00");
        getCart();

    }


    public void setupCart() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new CartRecyclerAdapter(items);
        cartList.setLayoutManager(layoutManager);
        cartList.setAdapter(adapter);
        getCart();
    }

    public void getCart() {
        double amount = 0;
        items.clear();
        Iterator iter = cart.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            CartItem item = (CartItem) entry.getValue();
            amount = amount + item.getNumber() * item.getPrice();
            items.add(item);
        }
        cartAmount.setText("￥" + String.valueOf(amount));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.fragment_cart_clear, R.id.fragment_cart_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_cart_clear:
                HashMap<String, CartItem> cartMap = MyApplication.getCart();
                cartMap.clear();
                getCart();
                break;
            case R.id.fragment_cart_submit:
                if(MyApplication.getUser().getUserId()==null){
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }else {
                    Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
        }
    }


    private class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {
        private ArrayList<CartItem> mData;
        private CartItem item;
        private CartItemView cartItemView;

        public CartRecyclerAdapter(ArrayList<CartItem> mData) {
            this.mData = mData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recycler_layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            item = mData.get(position);
            cartItemView = holder.cartItemView;
            cartItemView.setCart(item,CartFragment.this);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CartItemView cartItemView;

            public ViewHolder(View v) {
                super(v);
                // Define click listener for the ViewHolder's View.
                cartItemView = (CartItemView) v;
            }

            @Override
            public void onClick(View v) {

            }
        }
    }
}
