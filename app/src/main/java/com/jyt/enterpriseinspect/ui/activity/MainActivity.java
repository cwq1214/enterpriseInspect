package com.jyt.enterpriseinspect.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.MainContract;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.MainPresenterImpl;
import com.jyt.enterpriseinspect.uitl.DensityUtils;
import com.jyt.enterpriseinspect.uitl.ScreenUtils;
import com.jyt.enterpriseinspect.uitl.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/22.
 */

@ActivityAnnotation(showBack = false, title = "首页", showFunction = false)
public class MainActivity extends BaseActivity<MainPresenterImpl> implements MainContract.MainView {
    @BindView(R.id.createMission)
    FrameLayout createMission;
    @BindView(R.id.missionList)
    FrameLayout missionList;
    @BindView(R.id.saveMission)
    FrameLayout saveMission;
    @BindView(R.id.government)
    FrameLayout government;
    @BindView(R.id.print)
    FrameLayout print;
    @BindView(R.id.setting)
    FrameLayout setting;
    @BindView(R.id.gridLayout)
    GridLayout gridLayout;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    Handler handler;
    final int delayedTime = 2000;
    boolean closeActivity = false;


    @Override
    public MainPresenterImpl createPresenter() {
        return new MainPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewCreated() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(3);

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(scrollView.getWidth(),scrollView.getHeight());
                gridLayout.setLayoutParams(params);
            }
        });

    }




    @OnClick({R.id.createMission, R.id.missionList, R.id.saveMission, R.id.government, R.id.print, R.id.setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createMission:
                onCreateMissionClick();
                break;
            case R.id.missionList:
                onMissionListClick();
                break;
            case R.id.saveMission:
                onSaveMissionClick();
                break;
            case R.id.government:
                onGovernmentClick();
                break;
            case R.id.print:
                onPrintClick();
                break;
            case R.id.setting:
                onSettingClick();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(UserInfo.getAccount())||TextUtils.isEmpty(UserInfo.getPassword())){
            finish();
            return;
        }
        getMyInfo();

    }

    public void onCreateMissionClick() {
        IntentHelper.openCreateMissionActivity(getContext());
    }

    public void onMissionListClick() {
        IntentHelper.openMissionListActivity(getContext());
    }

    public void onSaveMissionClick() {
        IntentHelper.openCacheListActivity(getContext());
    }

    public void onGovernmentClick() {
        IntentHelper.openCreatePairIllegalMissionActivity(getContext());
    }

    public void onPrintClick() {
        IntentHelper.openPrintAskOrderActivity(getContext());
    }

    public void onSettingClick() {
        IntentHelper.openPersonalCenterActivity(getContext());
    }


    @Override
    public void onBackPressed() {

        if (closeActivity) {
//            super.onBackPressed();
//            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);

        } else {
            closeActivity = true;
            ToastUtil.showShort(getContext(), "再按一次退出");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    closeActivity = false;
                }
            }, delayedTime);
        }
    }


    private void getMyInfo(){
        Http.getMyInfo(getContext(), UserInfo.getAccount(), new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                if (isSuccess){
                    try {
                        JSONObject jsonObject = new JSONObject(data.toString());
                        String userid = jsonObject.getString("userid");
                        String realname = jsonObject.getString("realname");

                        UserInfo.setID(userid);
                        UserInfo.setREALNAME(realname);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
