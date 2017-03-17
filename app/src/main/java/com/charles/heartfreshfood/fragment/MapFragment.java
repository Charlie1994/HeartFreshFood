package com.charles.heartfreshfood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.charles.heartfreshfood.MyApplication;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.PickingLocation;
import com.charles.heartfreshfood.model.VendorLocationProfile;
import com.charles.heartfreshfood.wiget.MapLocationView;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MapFragment extends Fragment implements
      LocationSource,AMapLocationListener,AMap.OnMapClickListener,
      AMap.OnMarkerClickListener{
    private AMap aMap;
    private MapView mapView;
    private Button makerButton;
    private MarkerOptions markerOption;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private ArrayList<VendorLocationProfile> vendors; //商家列表
    private ArrayList<PickingLocation> pickings; //取货点列表
    private Button markerButton;// 获取屏幕内所有marker的button
    private OnLocationChangedListener mListener;
    private NetworkApi service;//网络操作端口
    private View view;
    private MapLocationView locationWindow;
    private final String TAG = "mapfragment";
    private double mLocationLongtitude = 0;
    private double mLocationLatitude = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map,container,false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        locationWindow = (MapLocationView)view.findViewById(R.id.fragment_map_detailwindow);
        locationWindow.setVisibility(View.INVISIBLE);
        initSpinner();
        vendors = new ArrayList<>();
        pickings = new ArrayList<>();
        init();
        return view;
    }
    private void initSpinner(){
        Spinner s = (Spinner)view.findViewById(R.id.fragment_map_selector);
        ArrayList<String> l = new ArrayList<>();
        l.add("显示农场");
        l.add("显示收货点");
        ArrayAdapter<String> madapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,l);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(madapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        getVendorList();
                        break;
                    case 1:
                        getPickingList();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setUpMap();
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        //定义各种点击触发事件
        aMap.setOnMapClickListener(this);

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()

    }

    /**
     * 在地图上添加商家
     */
    private void setVendorMarker(ArrayList<VendorLocationProfile> list) {
        aMap.clear();
        for(VendorLocationProfile vendor:list) {
            Marker marker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                  .position(new LatLng(vendor.getLatitude(), vendor.getLongtitude()))
                  .snippet(vendor.getVendorName()).draggable(true)
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_map_vendor))
                  );
            marker.setObject(vendor);
        }
        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
              .position(new LatLng(mLocationLatitude, mLocationLongtitude))
              .snippet("您当前所在位置").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mylocation)));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }
    /*
     * 添加取货点图标
     */

    public void setPickingMarker(ArrayList<PickingLocation> list){
        aMap.clear();
        for(PickingLocation pick:list) {
            Marker marker =aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                  .position(new LatLng(pick.getLatitude(), pick.getLongtitude()))
                  .snippet(pick.getName()).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_picking)));
            marker.setObject(pick);
        }
        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
              .position(new LatLng(mLocationLatitude, mLocationLongtitude))
              .snippet("您当前所在位置").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mylocation)));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
    }
    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                  && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mLocationLongtitude = amapLocation.getLongitude();
                mLocationLatitude = amapLocation.getLatitude();
                aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                      .position(new LatLng(mLocationLatitude, mLocationLongtitude))
                      .snippet("您当前所在位置").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_mylocation)));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位刷新时间为5分钟
            mLocationOption.setInterval(300000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            CameraUpdateFactory.zoomTo(20);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    /*
     * 各种点击事件的方法实现
     */
    //地图点击事件
    @Override
    public void onMapClick(LatLng latLng) {
        locationWindow.setVisibility(View.INVISIBLE);
    }
    //marker点击事件
    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        Object obj = marker.getObject();
        String className = obj.getClass().getSimpleName();
        switch (className){
            case "PickingLocation":{
                PickingLocation p = (PickingLocation)obj;
                LatLng point1 = new LatLng(p.getLatitude(),p.getLongtitude());
                LatLng point2 = new LatLng(mLocationLatitude,mLocationLongtitude);
                int distance = (int)AMapUtils.calculateLineDistance(point1,point2);
                locationWindow.setDistance(distance);
                locationWindow.setPickingProfile(p);
                locationWindow.setVisibility(View.VISIBLE);
                break;
            }
            case "VendorLocationProfile":{
                VendorLocationProfile p = (VendorLocationProfile)obj;
                LatLng point1 = new LatLng(p.getLatitude(),p.getLongtitude());
                LatLng point2 = new LatLng(mLocationLatitude,mLocationLongtitude);
                int distance = (int)AMapUtils.calculateLineDistance(point1,point2);
                locationWindow.setDistance(distance);
                locationWindow.setVendorProfile(p,getActivity());
                locationWindow.setVisibility(View.VISIBLE);
                break;
            }
            default:
                break;
        }
        return false;
    }
    /*
     * 得到收获点信息
     */
    public void getPickingList(){
        getList("pick");
    }
    /*
     * 得到农场信息
     */
    public void getVendorList(){
        getList("vendor");
    }
    /*
     * 异步网络操作得到商家信息
     */
    public void getList(String list){
        if (service==null)
            service = NetworkApi.retrofit.create(NetworkApi.class);
        if(list.equals("vendor")){
            int cityid = MyApplication.getCityid();
            Call<ArrayList<VendorLocationProfile>> call =
                  service.getVendorLocationProfile(cityid);
            call.enqueue(new Callback<ArrayList<VendorLocationProfile>>() {
                @Override
                public void onResponse(Response<ArrayList<VendorLocationProfile>> response, Retrofit retrofit) {
                    if(response.body()!=null)
                        vendors = response.body();
                    Log.d(TAG,"vendor success");
                    setVendorMarker(vendors);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(TAG,"vendor fail");
                }
            });
        }
        if(list.equals("pick")){
            Call<ArrayList<PickingLocation>> call =
                  service.getPickingLocation(MyApplication.getCityid());
            call.enqueue(new Callback<ArrayList<PickingLocation>>() {
                @Override
                public void onResponse(Response<ArrayList<PickingLocation>> response, Retrofit retrofit) {
                    if(response.body()!=null)
                        pickings = response.body();
                    Log.d(TAG,"picking success");
                    setPickingMarker(pickings);
                }
                @Override
                public void onFailure(Throwable t) {
                    Log.d(TAG,"picking fail");
                }
            });
        }

    }
}
