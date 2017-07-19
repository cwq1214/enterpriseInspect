package com.jyt.enterpriseinspect.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.adapter.CompanyListAdapter;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.entity.Company;
import com.jyt.enterpriseinspect.model.BaseModelImpl;
import com.jyt.enterpriseinspect.presenter.BasePresenterImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chenweiqi on 2017/3/18.
 */
@ActivityAnnotation(title = "企业查询", showBack = true)
public class SearchMissionActivity extends BaseActivity {
    @BindView(R.id.inputSearchContent)
    EditText inputSearchContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    List<Company> companies;

    CompanyListAdapter adapter;
    @Override
    public BaseContract.BasePresenter createPresenter() {
        return new BasePresenterImpl() {
            @Override
            public BaseContract.BaseModel createModel() {
                return new BaseModelImpl();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_mission;
    }

    @Override
    public void onViewCreated() {
        inputSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchMission(s.toString());
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter = new CompanyListAdapter());



    }


    private void searchMission(final String keywords){
        Http.searchCompany(getContext(), "company_code", keywords, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                if (isSuccess){
                    companies = new ArrayList<Company>();
                    companies.addAll((Collection<? extends Company>) data);
                }

                Http.searchCompany(getContext(), "companyname", keywords, new Http.HttpResultCallback() {
                    @Override
                    public void result(boolean isSuccess, Object data, String message) {
                        if (isSuccess){
                            companies.addAll((Collection<? extends Company>) data);
                        }

                        Http.searchCompany(getContext(), "legal_person", keywords, new Http.HttpResultCallback() {
                            @Override
                            public void result(boolean isSuccess, Object data, String message) {
                                if (isSuccess){
                                    companies.addAll((Collection<? extends Company>) data);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                });
            }
        });
    }



}
