package com.jyt.enterpriseinspect.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.adapter.MissionListAdapter;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseFragment;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.MissionListFragmentContract;
import com.jyt.enterpriseinspect.entity.Mission;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.MissionHelper;
import com.jyt.enterpriseinspect.itemDecoration.RecycleViewDivider;
import com.jyt.enterpriseinspect.presenter.MissionListFragmentPresenterImpl;
import com.jyt.enterpriseinspect.ui.viewholder.MissionItemViewHolder;
import com.jyt.enterpriseinspect.uitl.LogUtil;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by v7 on 2016/12/23.
 */

public class MissionListFragment extends BaseFragment implements MissionListFragmentContract.MissionListFragmentView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    TwinklingRefreshLayout swipeRefreshLayout;
    @BindView(R.id.centerMessage)
    TextView cecnterMessage;

    public static final String TYPE_TODAY = "today";
    public static final String TYPE_WEEK = "week";
    public static final String TYPE_MONTH = "month";


    public String TYPE;

    MissionListAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_mission_list;
    }

    @Override
    public BaseContract.BasePresenter createPresenter() {
        return new MissionListFragmentPresenterImpl();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TYPE  = getArguments().getString("TYPE");
        LogUtil.e("Type", TYPE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter = new MissionListAdapter());
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL,1, getResources().getColor(R.color.dividerColor)));

//        List<Mission> missions = new ArrayList<>();
//        for (int i=0;i<10;i++){
//            missions.add(new Mission());
//        }
//        adapter.setMissionList(missions);
//        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener(new MissionItemViewHolder.OnItemClickListener() {
            @Override
            public void onClick(Mission mission, int position) {
                MissionHelper.currentMission = mission;
                IntentHelper.openMissionDetailActivity(getContext(),mission);
            }
        });



        swipeRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                refreshLayout.finishLoadmore();
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getMissionList();
            }
        });
        cecnterMessage.setVisibility(View.GONE);


//        getMissionList();
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.startRefresh();

    }

    private void getMissionList(){
        Http.getMissionList(getContext(), TYPE, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                if (isSuccess) {
                    List<Mission> missions = new Gson().fromJson(data.toString(), new TypeToken<List<Mission>>() {
                    }.getType());
                    boolean emptyList = missions == null || missions.size() == 0;
                    cecnterMessage.setVisibility(emptyList ? View.VISIBLE : View.GONE);
                    adapter.setMissionList(missions);
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.finishRefreshing();
            }
        });
    }

}
