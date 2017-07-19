package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.MissionTypeDetailContract;
import com.jyt.enterpriseinspect.model.MissionTypeDetailModelImpl;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionTypeDetailPresenterImpl extends BasePresenterImpl<MissionTypeDetailContract.MissionTypeDetailModel,MissionTypeDetailContract.MissionTypeDetailView> implements MissionTypeDetailContract.MissionTypeDetailPresenter{
    @Override
    public MissionTypeDetailContract.MissionTypeDetailModel createModel() {
        return new MissionTypeDetailModelImpl();
    }
}
