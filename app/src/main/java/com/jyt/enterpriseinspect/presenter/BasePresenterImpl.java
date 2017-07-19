package com.jyt.enterpriseinspect.presenter;

import android.content.Context;

import com.jyt.enterpriseinspect.contract.BaseContract;

import butterknife.BindView;

/**
 * Created by v7 on 2016/12/22.
 */

public abstract class BasePresenterImpl<M extends BaseContract.BaseModel,V extends BaseContract.BaseView> implements BaseContract.BasePresenter{
    M model;
    V view;

    public BasePresenterImpl() {
        this.model = createModel();
    }


    public void setView(BaseContract.BaseView view) {
        this.view = (V) view;
    }

    @Override
    abstract public M createModel();

    @Override
    public void setContext(Context context) {
        model.setContext(context);
    }
}
