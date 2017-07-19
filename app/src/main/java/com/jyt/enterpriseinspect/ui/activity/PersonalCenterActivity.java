package com.jyt.enterpriseinspect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.PersonalCenterContract;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.PersonalCenterPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/28.
 */
@ActivityAnnotation(title = "个人管理", showBack = true)
public class PersonalCenterActivity extends BaseActivity<PersonalCenterContract.PersonalCenterPresenter> implements PersonalCenterContract.PersonalCenterView {
    @BindView(R.id.modifyPsd)
    LinearLayout modifyPsd;
    @BindView(R.id.logout)
    TextView logout;

    @Override
    public PersonalCenterContract.PersonalCenterPresenter createPresenter() {
        return new PersonalCenterPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void onViewCreated() {

    }
    @OnClick(R.id.modifyPsd)
    public void onModifyPsdClick(){
        IntentHelper.openForgetPsdActivity(getContext());
    }

    @OnClick(R.id.logout)
    public void onLogoutClick(){
        UserInfo.setAccount(null);
        UserInfo.setPassword(null);
        UserInfo.setID(null);
        UserInfo.setREALNAME(null);

//        Intent i = getBaseContext().getPackageManager()
//                .getLaunchIntentForPackage(getBaseContext().getPackageName());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
        IntentHelper.openLoginActivity(getContext());
        finish();
    }


}
