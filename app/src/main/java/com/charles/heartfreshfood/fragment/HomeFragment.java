package com.charles.heartfreshfood.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.activity.ChatActivity;
import com.charles.heartfreshfood.activity.LoginActivity;
import com.charles.heartfreshfood.activity.ProductActivity;
import com.charles.heartfreshfood.activity.ProductShowActivity;
import com.charles.heartfreshfood.activity.ScanActivity;
import com.charles.heartfreshfood.activity.VendorActivity;
import com.charles.heartfreshfood.activity.VendorShowActivity;
import com.charles.heartfreshfood.adapter.ProductShowAdapter;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.ProductProfile;
import com.charles.heartfreshfood.model.VendorProfile;
import com.charles.heartfreshfood.wiget.HomeAdvertiseView;
import com.charles.heartfreshfood.wiget.PictureRecycleView;
import com.charles.heartfreshfood.wiget.ProductCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class HomeFragment extends Fragment implements AMapLocationListener, ProductShowAdapter.OnItemClickListener {

    @Bind(R.id.home_recycler)
    PictureRecycleView homeRecycler;
    @Bind(R.id.home_location_tv)
    TextView homeLocationTv;
    @Bind(R.id.fragment_home_code)
    ImageView codebtn;
    @Bind(R.id.fragment_home_msg)
    ImageView msgbtn;
    @Bind(R.id.fragment_home_vendorbtn)
    RelativeLayout vendorbtn;
    @Bind(R.id.fragment_home_productbtn)
    RelativeLayout productbtn;


    private View view;

    private ArrayAdapter<String> location_list = null;
    private ArrayList<String> bannerList = null;//滚动图片播放器图片地址
    private ArrayList<VendorProfile> vendorList = null;//推荐供应商
    private ArrayList<ProductProfile> productList = null;//推荐商品
    private ArrayList<String> cities = null;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private AMapLocation location = null;
    private String city = "";
    private String region = "";
    private ProgressDialog dialog;
    private HashMap<String, String> cityMap;
    private final String TAG = "homefragment";
    private SwipeRefreshLayout refreshLayout;
    private ProductShowAdapter productadapter;
    private MyVendorAdapter vendorAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_home_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bannerList.clear();
                vendorList.clear();
                productList.clear();
                httpQueue(city);
            }
        });
        productList = new ArrayList<>();
        vendorList = new ArrayList<>();
        bannerList = new ArrayList<>();
        dialog = ProgressDialog.show(getActivity(), "", "加载中...");
        cities = new ArrayList<>();
        city = "杭州市";
        initLocation();
        initProducts();
        initVendor();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    /*
         * 初始化地图定位功能
         */
    public void initLocation() {
        locationClient = new AMapLocationClient(this.getActivity().getApplicationContext());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置一次定位
        locationOption.setOnceLocation(true);
        // 设置定位监听
        locationClient.setLocationListener(this);
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
    }

    /*
     * 网络通信方法，得到推荐列表，广告条列表，异步得到数据
     */
    public void httpQueue(final String city) {
        NetworkApi service = NetworkApi.retrofit.create(NetworkApi.class);
        //取得播放条数列
        Call<ArrayList<String>> call1 = service.getBannerList();
        call1.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Response<ArrayList<String>> response, Retrofit retrofit) {
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
                dialog.dismiss();
                if (response.isSuccess()) {
                    // 请求成功 (状态码 200, 201)
                    bannerList.addAll(response.body());
                    setRecyler();
                } else {
                    //请求不成功 (like 400,401,403 etc)
                    //Handle errors
                    Log.d("homefragment", "请求失败");

                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
                Log.d("homefragment", "发送失败");
            }
        });
        //取得推荐商家信息数组
        Call<ArrayList<VendorProfile>> call2 = service.getHomeVendorList(city);
        call2.enqueue(new Callback<ArrayList<VendorProfile>>() {
            @Override
            public void onResponse(Response<ArrayList<VendorProfile>> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                    // 请求成功 (状态码 200, 201)
                    if (response.body() != null) {
                        vendorList.addAll(response.body());
                        vendorAdapter.notifyDataSetChanged();
                    }
                } else {
                    //请求不成功 (like 400,401,403 etc)
                    //Handle errors
                    Log.d("homefragment", "请求失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("homefragment", "发送失败");
            }
        });
        //取得推荐产品数组
        Call<ArrayList<ProductProfile>> call3 = service.getHomeProductList(city);
        call3.enqueue(new Callback<ArrayList<ProductProfile>>() {
            @Override
            public void onResponse(Response<ArrayList<ProductProfile>> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess()) {
                    // 请求成功 (状态码 200, 201)
                    productList.addAll(response.body());
                    productadapter.notifyDataSetChanged();
                } else {
                    //请求不成功 (like 400,401,403 etc)
                    //Handle errors
                    Log.d("homefragment", "请求失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                Log.d("homefragment", "发送失败");
            }
        });
        //尝试先从sharepreferences中得到city图
        SharedPreferences sp = getActivity().getSharedPreferences("citymap", Context.MODE_PRIVATE);
        if (sp.contains("cities") || sp.getString("cities", "no").equals("no")) {
            String json = sp.getString("cities", "");
            Gson gson = new Gson();
            cityMap = gson.fromJson(json, new TypeToken<List<Map<String, String>>>() {
            }.getType());
        } else {
            //手机里面没有就添加一个网络队列
            //取得city列表并且存到sharedpreferences中
            Call<HashMap<String, String>> call4 = service.getCityMap();
            call4.enqueue(new Callback<HashMap<String, String>>() {
                @Override
                public void onResponse(Response<HashMap<String, String>> response, Retrofit retrofit) {
                    cityMap = response.body();
                    //这里注释一下，因为取到的是HashMap，不能直接放进SharePreferences
                    //所以我用Gson将Map转换成json字符串再放进去,取出来同样使用Gson
                    //其实还可以把序列化的对象转化成16进制的byte进行存储
                    //甚至可以，将map拆开来一个一个键值存储，因为SharedPreferences是以xml方式存储的
                    String json = new Gson().toJson(cityMap);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("citymap", Context.MODE_PRIVATE).edit();
                    editor.putString("cities", json);
                    editor.apply();
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
        //String cityidentity = MyApplication.getCityid();
        //cityidentity = sp.getString(location.getCity(),"杭州市");
    }

    /*
     * 图片滚动播放器
     */
    public void setRecyler() {
        homeRecycler.setImgUrlList(bannerList);
        homeRecycler.launch();
        homeRecycler.startPlay();
    }


    //设置产品推荐
    private void initProducts() {
        RecyclerView listview = (RecyclerView) view.findViewById(R.id.fragment_home_productlist);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        productadapter = new ProductShowAdapter(productList);
        listview.addItemDecoration(new SpaceItemDecoration(10));
        listview.setLayoutManager(layoutManager);
        listview.setAdapter(productadapter);
        ProductCardView view = new ProductCardView(getActivity(), null);
        view.measure(0, 0);
        int height = (view.getMeasuredHeight() + 10) * 4;
        Log.d("home", String.valueOf(height));
        ViewGroup.LayoutParams layoutParams = listview.getLayoutParams();
        layoutParams.height = height;
        listview.setLayoutParams(layoutParams);
        productadapter.setOnItemClickListener(this);
    }

    //设置商家推荐
    private void initVendor() {
        ListView listView = (ListView) view.findViewById(R.id.fragment_home_vendorlist);
        vendorAdapter = new MyVendorAdapter();
        listView.setAdapter(vendorAdapter);
        HomeAdvertiseView itemview = new HomeAdvertiseView(getActivity(), null);
        itemview.measure(0, 0);
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = (itemview.getMeasuredHeight() + listView.getDividerHeight()) * 3;
        listView.setLayoutParams(layoutParams);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VendorProfile vendor = vendorList.get(position);
                Intent intent = new Intent(getActivity(), VendorActivity.class);
                intent.putExtra("vendorprofile", vendor);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }

    @Override
    public void onItemClick(ProductCardView view, int position, ProductProfile profile) {
        Intent intent = new Intent(getContext(), ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productid", profile.getProductId());
        intent.putExtras(bundle);
        getContext().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);

    }

    // 定位监听
    @Override
    public void onLocationChanged(AMapLocation loc) {
        if (null != loc) {
            //因为是设置一次定位所以不需要停止定位方法，SOK自己会移除
            location = loc;
            city = location.getCity();
            region = location.getDistrict();
            homeLocationTv.setText(city + region);
            httpQueue(city);
        }
    }

    @OnClick({R.id.fragment_home_code, R.id.fragment_home_msg,R.id.fragment_home_vendorbtn, R.id.fragment_home_productbtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_home_code:
                Intent intent1;
                if (MyApplication.getUser().getLoginStatus() != 1) {
                    intent1 = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                } else {
                    intent1 = new Intent(getActivity(), ScanActivity.class);
                    getActivity().startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.fragment_home_msg:
                Intent intent2;
                if (MyApplication.getUser().getLoginStatus() != 1) {
                    intent2 = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                } else {
                    intent2 = new Intent(getActivity(), ChatActivity.class);
                    getActivity().startActivity(intent2);
                    getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.fragment_home_vendorbtn:
                Intent intent3 = new Intent(getActivity(), VendorShowActivity.class);
                getActivity().startActivity(intent3);
                getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
            case R.id.fragment_home_productbtn:
                Intent intent4 = new Intent(getActivity(),ProductShowActivity.class);
                getActivity().startActivity(intent4);
                getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if (parent.getChildPosition(view) != 0 && parent.getChildPosition(view) != 1)
                outRect.top = space;
        }
    }

    private class MyVendorAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return vendorList.size();
        }

        @Override
        public Object getItem(int position) {
            return vendorList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HomeAdvertiseView view = new HomeAdvertiseView(getActivity(), null);
            VendorProfile p = vendorList.get(position);
            view.setVendorInfo(p.getImgUrl(), p.getVendorName(), p.getAddressDetail());
            return view;
        }
    }
}
