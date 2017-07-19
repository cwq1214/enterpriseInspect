package com.jyt.enterpriseinspect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.presenter.WelComePresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/22.
 */

public class WelComeActivity extends BaseActivity {
    @BindView(R.id.img)
    ImageView img;

    Handler handler;

    int delayedTime = 5000;
    final int WHAT_TO_LOGIN=1;
    final int WHAT_TO_MAIN=2;

    @Override
    public BaseContract.BasePresenter createPresenter() {
        return new WelComePresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void onViewCreated() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (WHAT_TO_LOGIN==msg.what){
                    IntentHelper.openLoginActivity(getContext());
                }else if (WHAT_TO_MAIN == msg.what){
                    IntentHelper.openMainActivity(getContext());
                }
            }
        };

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WHAT_TO_LOGIN);
            }
        },delayedTime);

    }

    @OnClick(R.id.img)
    public void onImgClick(){
        IntentHelper.openLoginActivity(getContext());
        finish();
    }

}
