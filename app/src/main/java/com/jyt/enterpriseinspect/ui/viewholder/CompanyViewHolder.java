package com.jyt.enterpriseinspect.ui.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.entity.Company;

/**
 * Created by chenweiqi on 2017/3/18.
 */

public class CompanyViewHolder extends BaseViewHolder {
    public CompanyViewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_company_info,parent,false));
    }

    @Override
    public void setData(Object data, int index) {
        super.setData(data, index);
        Company company = (Company) data;
    }
}
