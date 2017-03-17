package com.charles.heartfreshfood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.CartItem;
import com.charles.heartfreshfood.model.CustomerAddress;
import com.charles.heartfreshfood.wiget.AddressItemView;
import com.charles.heartfreshfood.wiget.ConfirmOrderItemView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ConfirmOrderActivity extends AppCompatActivity {

    HashMap<String, CartItem> cart;
    @Bind(R.id.activity_confirm_order_amount)
    TextView activityConfirmOrderAmount;
    @Bind(R.id.activity_confirm_order_submit)
    RelativeLayout activityConfirmOrderSubmit;
    Context context = ConfirmOrderActivity.this;
    @Bind(R.id.activity_confirm_order_address)
    AddressItemView addressBtn;
    private ArrayList<CartItem> list1;
    private HashMap<String, ArrayList<CartItem>> orders;
    private double orderamount;
    private String cartjson;
    private int pickid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        ButterKnife.bind(this);
        cart = MyApplication.getCart();
        initOrder();
        initHeader();
        getDefaultPick();
    }

    private void initHeader() {
        TextView leftbtn = (TextView) findViewById(R.id.view_banner_head_left);
        TextView title = (TextView) findViewById(R.id.view_banner_head_title);
        title.setText("提交订单");
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
    }

    private void initOrder() {
        orders = new HashMap<>();
        HashMap<String, CartItem> cart = MyApplication.getCart();
        Iterator iter = cart.entrySet().iterator();
        ArrayList<CartItem> list = null;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            CartItem val = (CartItem) entry.getValue();
            double subtotal = val.getSubtotal();
            orderamount = orderamount + subtotal;
            String id = val.getVendorid();
            Log.d("confirm", id);
            if (orders.containsKey(id)) {
                list = orders.get(id);
                list.add(val);
            } else {
                list = new ArrayList<>();
                list.add(val);
                orders.put(id, list);
            }
        }
        activityConfirmOrderAmount.setText("￥" + String.valueOf(orderamount));
        Iterator iter1 = orders.entrySet().iterator();
        list1 = new ArrayList<>();
        while (iter1.hasNext()) {
            Map.Entry entry = (Map.Entry) iter1.next();
            ArrayList<CartItem> itemlist = (ArrayList<CartItem>) entry.getValue();
            String title = itemlist.get(0).getVendorName();
            CartItem newitem = new CartItem();
            newitem.setVendorName(title);
            list1.add(newitem);
            for (CartItem item : itemlist) {
                list1.add(item);
            }
        }
        Myadapter adapter = new Myadapter();
        ListView listView = (ListView) findViewById(R.id.activity_confirm_order_list);
        listView.setAdapter(adapter);
        Gson gson = new Gson();
        cartjson = gson.toJson(orders);
    }

    public void getDefaultPick() {
        NetworkApi service = NetworkApi.retrofit.create(NetworkApi.class);

        Call<CustomerAddress> call = service.getDefaultPick(MyApplication.getUser().getUserId());
        call.enqueue(new Callback<CustomerAddress>() {
            @Override
            public void onResponse(Response<CustomerAddress> response, Retrofit retrofit) {
                CustomerAddress r = response.body();
                if (!r.getCity().equals("null")) {
                    addressBtn.setAddress(r);
                    pickid = r.getPickinglocationid();
                } else {
                    addressBtn.setWarning();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    @OnClick({R.id.activity_confirm_order_submit,R.id.activity_confirm_order_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_confirm_order_submit:
                Intent intent = new Intent(ConfirmOrderActivity.this, PayActivity.class);
                intent.putExtra("cartjson", cartjson);
                intent.putExtra("orderamount", orderamount);
                intent.putExtra("pickid", pickid);
                intent.putExtra("type", "order");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
            case R.id.activity_confirm_order_address:
                Intent intent1 = new Intent(ConfirmOrderActivity.this,ChooseAddressActivity.class);
                startActivityForResult(intent1,1);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ChooseAddressActivity.RESULT_OK:
                CustomerAddress address = (CustomerAddress)data.getSerializableExtra("address");
                addressBtn.setAddress(address);
                break;
            case ChooseAddressActivity.RESULT_CANCLE:
                break;
            default:
                break;

        }
    }

    private class Myadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list1.size();
        }

        @Override
        public Object getItem(int position) {
            return list1.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CartItem item = list1.get(position);
            View view;
            if (item.getProductName() != null) {
                view = LayoutInflater.from(context).inflate(R.layout.confirm_order_recycler_layout, null);
                ConfirmOrderItemView itemView = (ConfirmOrderItemView) view.findViewById(R.id.confirm_list_item_view);
                itemView.setViews(item);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.view_list_title, null);
                TextView textView = (TextView) view.findViewById(R.id.list_title_tv);
                textView.setText(item.getVendorName());
            }
            return view;
        }
    }
}
