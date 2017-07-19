package com.jyt.enterpriseinspect.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by v7 on 2016/12/23.
 */

public class MFragmentPageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

    public MFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList();
    }

    public void addFragment(Fragment fragment){
        fragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


}
