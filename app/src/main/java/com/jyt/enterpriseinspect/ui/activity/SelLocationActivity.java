package com.jyt.enterpriseinspect.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.jyt.enterpriseinspect.App;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.model.BaseModelImpl;
import com.jyt.enterpriseinspect.presenter.BasePresenterImpl;
import com.jyt.enterpriseinspect.uitl.LogUtil;
import com.jyt.enterpriseinspect.uitl.ToastUtil;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/23.
 */
@ActivityAnnotation(showBack = true,showFunction = true,title = "地址",functionText = "确定")
public class SelLocationActivity extends BaseActivity {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.search_content)
    LinearLayout searchContent;
    @BindView(R.id.search_mask)
    LinearLayout searchMask;
    @BindView(R.id.toCurrentLocation)
    ImageView toCurrentLocation;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.ding)
    ImageView ding;
    @BindView(R.id.layout_search)
    FrameLayout layoutSearch;

    LocationClient mLocationClient = null;
    boolean zoomOnce = false;
    LatLng currentLatlng;
    String currentCity;
    String currentAddress;
    List<Overlay> marksList=new ArrayList();
    String lat;
    String lon;

    boolean canEditAddress = false;

    GeoCoder geoCoder ;

    PoiSearch poiSearch;
    @Override
    public BaseContract.BasePresenter createPresenter() {
        return new BasePresenterImpl() {
            @Override
            public BaseContract.BaseModel createModel() {
                return new BaseModelImpl();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sel_location;
    }


    @Override
    public void onViewCreated() {

        canEditAddress = getIntent().getBooleanExtra(IntentHelper.KEY_EDIT_ADDRESS,false);
        lat = getIntent().getStringExtra(IntentHelper.KEY_LATITUDE);
        lon = getIntent().getStringExtra(IntentHelper.KEY_LONGITUDE);


        geoCoder = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
//                    Toast.makeText(MainActivity.this, "抱歉，未能找到结果",
//                            Toast.LENGTH_LONG).show();
                    ToastUtil.showShort(getContext(),"抱歉，未能找到结果");
                    return;
                }
                List<PoiInfo> poiList = result.getPoiList();
                if (poiList!=null&&poiList.size()!=0){

                    address.setText(poiList.get(0).address);
                    address.setTag(poiList.get(0).location);
                }
//                poiInfos = (ArrayList<PoiInfo>) result.getPoiList();
//                adapter = new PoiListAdapter(MainActivity.this, poiInfos);
//                iv_item.setAdapter(adapter);
//                adapter.notifyDataSetChanged();

            }

            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null
                        || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    // 没有检测到结果
                }
            }
        };
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);

        if (!canEditAddress) {
            layoutSearch.setVisibility(View.GONE);
            functionBtn.setVisibility(View.GONE);
            ding.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lon)) {
            if (!canEditAddress){
                //定义Maker坐标点
                LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.ding);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mapView.getMap().addOverlay(option);
                mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point,17));
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.valueOf(lat),Double.valueOf(lon))));
//            }
            }else {
                LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
                mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point,17));
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.valueOf(lat),Double.valueOf(lon))));
            }
        }else {
//            onToCurrentLocationClick();
        }





        mapView.getMap().setMyLocationEnabled(true);
        mapView.showZoomControls(false);




        CloudManager.getInstance().init(new CloudListener() {
            @Override
            public void onGetSearchResult(CloudSearchResult cloudSearchResult, int i){
                if (cloudSearchResult==null)
                    return;
                List<CloudPoiInfo> poiList = cloudSearchResult.poiList;
                LogUtil.e("i",i);
                for (int j=0;j<poiList.size();j++){
                    LogUtil.e("poi",poiList.get(j).address);
                }
//                LogUtil.e();
            }


            @Override
            public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {
                LogUtil.e("i",i);
            }

            @Override
            public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {
                List customPois = cloudRgcResult.customPois;
                LogUtil.e("i",i);
                for (int j=0;j<customPois.size();j++){
                    LogUtil.e("poi",customPois.get(j).toString());
                }
            }
        });

        setupSearchView();
        setupLocation();
    }


    private void setupSearchView(){

        searchMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentHelper.openSearchLocationActivityForResult(getActivity(),currentLatlng);
            }
        });
    }

    private void setupLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();


        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                MyLocationData locationData = new MyLocationData.Builder().latitude(location.getLatitude()).longitude(location.getLongitude()).build();
                mapView.getMap().setMyLocationData(locationData);
                currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
                currentCity = location.getCity();
                currentAddress = location.getAddrStr();

                if (TextUtils.isEmpty(lat)||TextUtils.isEmpty(lon))
                if (!zoomOnce) {
                    onToCurrentLocationClick();
                    zoomOnce = true;
                }
////                mLocationClient.stop();
//            if (!TextUtils.isEmpty(lat)&&!TextUtils.isEmpty(lon)) {
//                LatLng point = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
//                mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(point,17));
//                geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(Double.valueOf(lat),Double.valueOf(lon))));
//
//            }


                //Receive Location
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\ncompany_latitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：公里每小时
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndirection : ");
                    sb.append(location.getDirection());// 单位度
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                sb.append("\nlocationdescribe : ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                List<Poi> list = location.getPoiList();// POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
                Log.i("BaiduLocationApiDem", sb.toString());
            }

        });

        mapView.getMap().setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (canEditAddress) {
                    playAnima();
                    LatLng center = mapStatus.bound.getCenter();
                    geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(center));
                }


            }
        });

        mLocationClient.start();


    }

    private void playAnima(){

        ObjectAnimator.ofFloat(ding,"translationY",0f,-150,0f).setDuration(500).start();
    }


    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @OnClick(R.id.toCurrentLocation)
    public void onToCurrentLocationClick(){
        mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(currentLatlng, 17));
        address.setText(currentAddress);
        address.setTag(currentLatlng);

    }

    @Override
    public void onFunctionClick() {
        super.onFunctionClick();
        if (TextUtils.isEmpty(address.getText().toString())){
            ToastUtil.showShort(getContext(),"未选定位置");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_LATLNG, (Parcelable) address.getTag());
        intent.putExtra(IntentHelper.KEY_ADDRESS,address.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IntentHelper.REQUIRE_CODE_SEARCH_LOCATION&&resultCode== Activity.RESULT_OK){
            locationResult(data);
        }
    }

    private void locationResult(Intent data){
//        ding.setVisibility(View.GONE);
        PoiInfo poiInfo = data.getParcelableExtra(IntentHelper.KEY_POIINFO);
        //定义Maker坐标点
                LatLng point =poiInfo.location;
        //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.ding);
        //构建MarkerOption，用于在地图上添加Marker
        Bundle bundle = new Bundle();
        bundle.putString("address",poiInfo.address);
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .title(poiInfo.name)
                .extraInfo(bundle);
        //删除其他所有Marker
        if (marksList.size()!=0){
            for (int i=marksList.size()-1;i>=0;i--){
                marksList.get(i).remove();
                marksList.remove(marksList.get(i));
            }

        }

        //在地图上添加Marker，并显示
//        marksList.add(mapView.getMap().addOverlay(option));

        mapView.getMap().setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                address.setText(marker.getExtraInfo().getString("address"));
                address.setTag(marker.getPosition());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(marker.getPosition());
                mapView.getMap().animateMapStatus(update);
                return true;
            }
        });

        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(poiInfo.location);
        mapView.getMap().animateMapStatus(update);
        address.setText(poiInfo.address+poiInfo.name);
        address.setTag(poiInfo.location);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (mLocationClient!=null&&mLocationClient.isStarted()){
            mLocationClient.stop();
        }
    }
}
