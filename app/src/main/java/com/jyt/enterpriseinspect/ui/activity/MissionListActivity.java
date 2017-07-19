package com.jyt.enterpriseinspect.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.adapter.MFragmentPageAdapter;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.MissionListContract;
import com.jyt.enterpriseinspect.presenter.MissionListPresenterImpl;
import com.jyt.enterpriseinspect.ui.fragment.MissionListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by v7 on 2016/12/23.
 */
@ActivityAnnotation(title = "任务列表", showBack = true)
public class MissionListActivity extends BaseActivity<MissionListContract.MissionListPresenter> implements MissionListContract.MissionListView {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    MFragmentPageAdapter adapter;
    MissionListFragment todayMissionListFragment;
    MissionListFragment weekMissionListFragment;
    MissionListFragment monthMissionListFragment;

    @Override
    public MissionListContract.MissionListPresenter createPresenter() {
        return new MissionListPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mission_list;
    }

    @Override
    public void onViewCreated() {
        todayMissionListFragment = new MissionListFragment();
        weekMissionListFragment = new MissionListFragment();
        monthMissionListFragment = new MissionListFragment();

        Bundle bundle1 = new Bundle();
        bundle1.putString("TYPE",MissionListFragment.TYPE_TODAY);
        todayMissionListFragment.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString("TYPE",MissionListFragment.TYPE_WEEK);
        weekMissionListFragment.setArguments(bundle2);

        Bundle bundle3 = new Bundle();
        bundle3.putString("TYPE",MissionListFragment.TYPE_MONTH);
        monthMissionListFragment.setArguments(bundle3);

        adapter = new MFragmentPageAdapter(getSupportFragmentManager());
        adapter.addFragment(todayMissionListFragment);
        adapter.addFragment(weekMissionListFragment);
        adapter.addFragment(monthMissionListFragment);
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("今天");
        tabLayout.getTabAt(1).setText("本周");
        tabLayout.getTabAt(2).setText("本月");


    }


}
