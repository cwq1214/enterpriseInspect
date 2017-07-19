package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.CreateMissionContract;
import com.jyt.enterpriseinspect.model.CreateMissionModelImpl;

/**
 * Created by v7 on 2016/12/23.
 */

public class CreateMissionPresenterImpl extends BasePresenterImpl implements CreateMissionContract.CreateMissionPresenter {
    @Override
    public BaseContract.BaseModel createModel() {
        return new CreateMissionModelImpl();
    }
}
