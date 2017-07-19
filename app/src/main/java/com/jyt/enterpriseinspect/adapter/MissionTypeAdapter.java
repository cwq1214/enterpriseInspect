package com.jyt.enterpriseinspect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.ui.viewholder.MissionTypeViewHolder;

import java.util.List;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionTypeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    List datas ;
    MissionTypeViewHolder.OnMissionTypeClickListener onMissionTypeClickListener;
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MissionTypeViewHolder holder = new MissionTypeViewHolder(parent);
        holder.setOnMissionTypeClickListener(onMissionTypeClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(datas.get(position),position);
    }

    @Override
    public int getItemCount() {
        if (datas!=null)
            return datas.size();
        return 0;
    }

    public void setDatas(List datas) {
        this.datas = datas;
    }

    public void setOnMissionTypeClickListener(MissionTypeViewHolder.OnMissionTypeClickListener onMissionTypeClickListener) {
        this.onMissionTypeClickListener = onMissionTypeClickListener;
    }
}
