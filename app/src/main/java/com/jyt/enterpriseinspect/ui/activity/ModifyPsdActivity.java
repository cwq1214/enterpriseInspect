package com.jyt.enterpriseinspect.ui.activity;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.ModifyPsdContract;
import com.jyt.enterpriseinspect.presenter.ModifyPsdPresenterImpl;

/**
 * Created by v7 on 2016/12/23.
 */
@ActivityAnnotation(showBack = true,showFunction = false,title = "修改密码")
public class ModifyPsdActivity extends BaseActivity<ModifyPsdContract.ModifyPsdPresenter> implements ModifyPsdContract.ModifyPsdView {
    @Override
    public ModifyPsdContract.ModifyPsdPresenter createPresenter() {
        return new ModifyPsdPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_psd;
    }

    @Override
    public void onViewCreated() {

    }
}
