package com.jyt.enterpriseinspect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.entity.Mission;
import com.jyt.enterpriseinspect.ui.viewholder.MissionItemViewHolder;

import java.util.List;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    List<Mission> missionList;
    MissionItemViewHolder.OnItemClickListener onItemClickListener;
    View.OnLongClickListener onLongClickListener ;
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MissionItemViewHolder holder = new MissionItemViewHolder(parent);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(missionList.get(position),position);
    }

    @Override
    public int getItemCount() {
        if (missionList!=null)
            return missionList.size();
        return 0;
    }

    public void setMissionList(List<Mission> missionList) {
        this.missionList = missionList;
    }

    public void setOnItemClickListener(MissionItemViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


}
