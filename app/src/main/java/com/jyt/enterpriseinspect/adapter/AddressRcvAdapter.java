package com.jyt.enterpriseinspect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.ui.viewholder.AddressViewHolder;

import java.util.List;

/**
 * Created by v7 on 2016/12/24.
 */

public class AddressRcvAdapter extends RecyclerView.Adapter {
    List address;

    AddressViewHolder.OnAddressClickListener onAddressClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AddressViewHolder viewHolder = new AddressViewHolder(parent);
        viewHolder.setOnAddressClickListener(onAddressClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).setData(address.get(position),position);
    }

    @Override
    public int getItemCount() {
        if (address!=null)
            return address.size();
        return 0;
    }

    public void setAddress(List address) {
        this.address = address;
    }

    public void setOnAddressClickListener(AddressViewHolder.OnAddressClickListener onAddressClickListener) {
        this.onAddressClickListener = onAddressClickListener;
    }
}
