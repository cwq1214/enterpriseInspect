package com.jyt.enterpriseinspect.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.entity.Content;
import com.jyt.enterpriseinspect.entity.ViewType;
import com.jyt.enterpriseinspect.ui.activity.BrowImageActivity;
import com.jyt.enterpriseinspect.ui.viewholder.MissionCheckedEditPanelViewHolder;
import com.jyt.enterpriseinspect.ui.viewholder.MissionCheckedItemViewHolder;

import java.util.List;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionCheckedAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    List<ViewType> viewTypes;

    Activity activity;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), BrowImageActivity.class);
            intent.putExtra("imageUrl", (String) v.getTag());
            v.getContext().startActivity(intent);
        }
    };
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ViewType.MISSION_CHECKED_EDIT_PANEL:
                MissionCheckedEditPanelViewHolder editPanelViewHolder = new MissionCheckedEditPanelViewHolder(parent);
                editPanelViewHolder.setActivity(activity);
                return editPanelViewHolder;
            case ViewType.MISSION_CHECKED_ITEM:
                MissionCheckedItemViewHolder itemViewHolder = new MissionCheckedItemViewHolder(parent);
                itemViewHolder.setOnClickListener(onClickListener);
                return itemViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(viewTypes.get(position).data,position);
    }

    @Override
    public int getItemCount() {
        if (viewTypes!=null)
            return viewTypes.size();
        return 0;
    }

    public Content getContent(){
        for (int i=0;i<viewTypes.size();i++){
            if (viewTypes.get(i).type==ViewType.MISSION_CHECKED_EDIT_PANEL){
                return (Content) viewTypes.get(i).data;
            }
        }
        return null;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypes.get(position).type;
    }

    public void setViewTypes(List<ViewType> viewTypes) {
        this.viewTypes = viewTypes;
    }
}
