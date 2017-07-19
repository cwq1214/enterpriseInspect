package com.jyt.enterpriseinspect.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by v7 on 2016/12/24.
 */

public abstract class BaseViewHolder<T extends Object> extends RecyclerView.ViewHolder {
    protected T data;
    protected int index;
    public BaseViewHolder(View view) {
        super(view);
        ButterKnife.bind(this,itemView);
    }

    public void setData(T data,int index){
        this.data = data;
        this.index = index;

    }


    protected static View inflateView(ViewGroup parent,int layoutId){
        return LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
    }

}
