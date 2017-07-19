package com.jyt.enterpriseinspect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.adapter.AddressRcvAdapter;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.itemDecoration.RecycleViewDivider;
import com.jyt.enterpriseinspect.model.BaseModelImpl;
import com.jyt.enterpriseinspect.presenter.BasePresenterImpl;
import com.jyt.enterpriseinspect.ui.viewholder.AddressViewHolder;
import com.jyt.enterpriseinspect.uitl.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by v7 on 2016/12/24.
 */
@ActivityAnnotation(showBack = true, title = "搜索地点", showFunction = false)
public class SearchLocationActivity extends BaseActivity {



    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.centerMessage)
    TextView centerMessage;


    AddressRcvAdapter addressRcvAdapter;
    PoiSearch poiSearch;
    String currentCity;
    LatLng currentLatlng;
    String keyword;


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
        return R.layout.activity_search_location;
    }

    @Override
    public void onViewCreated() {
        addressRcvAdapter = new AddressRcvAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(addressRcvAdapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.dividerColor)));
        currentLatlng = getIntent().getParcelableExtra(IntentHelper.KEY_LATLNG);
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                centerMessage.setVisibility(poiResult==null?View.VISIBLE:View.GONE);
                if (poiResult!=null){
                    addressRcvAdapter.setAddress(poiResult.getAllPoi());
                    addressRcvAdapter.notifyDataSetChanged();
                }
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                if (s.length()!=0)
                searchPoi();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                searchPoi();
            }
        });

        addressRcvAdapter.setOnAddressClickListener(new AddressViewHolder.OnAddressClickListener() {
            @Override
            public void onClick(PoiInfo info, int index) {
                Intent intent = new Intent();
                intent.putExtra(IntentHelper.KEY_POIINFO,info);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void searchPoi(){
        poiSearch.searchNearby(new PoiNearbySearchOption().location(currentLatlng).keyword(keyword).pageCapacity(50).radius(20000).sortType(PoiSortType.comprehensive));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        poiSearch.destroy();
    }
}
