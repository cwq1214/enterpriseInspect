package com.jyt.enterpriseinspect.ui.activity;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.model.BaseModelImpl;
import com.jyt.enterpriseinspect.presenter.BasePresenterImpl;

/**
 * Created by chenweiqi on 2017/3/18.
 */
@ActivityAnnotation(title = "任务类型")
public class TodoMissionActivity extends BaseActivity {
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
        return R.layout.activity_sel_missiontype;
    }

    @Override
    public void onViewCreated() {

    }
}
