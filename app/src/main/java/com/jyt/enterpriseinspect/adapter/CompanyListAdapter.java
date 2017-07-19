package com.jyt.enterpriseinspect.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.entity.Company;
import com.jyt.enterpriseinspect.ui.viewholder.CompanyViewHolder;

import java.util.List;

/**
 * Created by chenweiqi on 2017/3/18.
 */

public class CompanyListAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    List<Company> companies;
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CompanyViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(companies.get(position),position);
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public int getItemCount() {
        if (companies!=null)
            return companies.size();
        return 0;
    }
}
