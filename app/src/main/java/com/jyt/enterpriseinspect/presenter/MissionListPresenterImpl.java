package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.MissionListContract;
import com.jyt.enterpriseinspect.model.MissionListModelImpl;

/**
 * Created by v7 on 2016/12/23.
 */

public class MissionListPresenterImpl extends BasePresenterImpl implements MissionListContract.MissionListPresenter {
    @Override
    public BaseContract.BaseModel createModel() {
        return new MissionListModelImpl();
    }
}
