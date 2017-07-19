package com.jyt.enterpriseinspect.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.adapter.MissionListAdapter;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.CacheListContract;
import com.jyt.enterpriseinspect.entity.Mission;
import com.jyt.enterpriseinspect.helper.Cache;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.MissionHelper;
import com.jyt.enterpriseinspect.presenter.CacheListPresenterImpl;
import com.jyt.enterpriseinspect.ui.viewholder.MissionItemViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by v7 on 2016/12/27.
 */

@ActivityAnnotation(title = "暂存任务", showBack = true)
public class CacheListActivity extends BaseActivity implements CacheListContract.CacheListView {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.centerMessage)
    TextView centerMessage;


    List<Mission> cacheMission;
    MissionListAdapter adapter;

    @Override
    public BaseContract.BasePresenter createPresenter() {
        return new CacheListPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cache_list;
    }

    @Override
    public void onViewCreated() {
        adapter = new MissionListAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        adapter.setOnItemClickListener(new MissionItemViewHolder.OnItemClickListener() {
            @Override
            public void onClick(Mission mission, int position) {
                MissionHelper.currentMission = mission;
                IntentHelper.openMissionDetailActivity(getContext(),mission);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cacheMission = Cache.getMissionList();
        adapter.setMissionList(cacheMission);
        adapter.notifyDataSetChanged();

        boolean showMessage = cacheMission==null||cacheMission.size()==0;
        centerMessage.setVisibility(showMessage? View.VISIBLE:View.GONE);


    }
}
