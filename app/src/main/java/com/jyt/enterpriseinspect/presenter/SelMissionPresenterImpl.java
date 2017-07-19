package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.SelMissionTypeContract;
import com.jyt.enterpriseinspect.contract.SelPeopleContract;
import com.jyt.enterpriseinspect.model.SelMissionTypeModelImpl;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class SelMissionPresenterImpl extends BasePresenterImpl<SelMissionTypeContract.SelMissionModel,SelMissionTypeContract.SelMissionView> implements SelMissionTypeContract.SelMissionPresenter{
    @Override
    public SelMissionTypeContract.SelMissionModel createModel() {
        return new SelMissionTypeModelImpl();
    }
}
