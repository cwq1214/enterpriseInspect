package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.MissionListFragmentContract;
import com.jyt.enterpriseinspect.model.MissionListFragmentModelImpl;

/**
 * Created by v7 on 2016/12/23.
 */

public class MissionListFragmentPresenterImpl extends BasePresenterImpl implements MissionListFragmentContract.MissionListFragmentPresenter {
    @Override
    public BaseContract.BaseModel createModel() {
        return new MissionListFragmentModelImpl();
    }
}
