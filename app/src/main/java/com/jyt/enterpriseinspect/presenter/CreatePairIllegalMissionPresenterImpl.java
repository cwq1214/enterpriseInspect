package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.CreatePairIllegalMissionContract;
import com.jyt.enterpriseinspect.model.CreatePairIllegalMissionModelImpl;

import static com.jyt.enterpriseinspect.contract.CreatePairIllegalMissionContract.*;

/**
 * Created by v7 on 2016/12/27.
 */

public class CreatePairIllegalMissionPresenterImpl  extends BasePresenterImpl<CreatePairIllegalMissionModel,CreatePairIllegalMissionView> implements CreatePairIllegalMissionPresenter {


    @Override
    public CreatePairIllegalMissionModel createModel() {
        return new CreatePairIllegalMissionModelImpl();
    }
}
