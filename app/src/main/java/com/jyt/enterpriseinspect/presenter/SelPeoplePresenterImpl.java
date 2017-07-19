package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.SelPeopleContract;
import com.jyt.enterpriseinspect.model.SelPeopleModelImpl;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class SelPeoplePresenterImpl extends BasePresenterImpl<SelPeopleContract.SelPeopleModel,SelPeopleContract.SelPeopleView> implements SelPeopleContract.SelPeoplePresenter {
    @Override
    public SelPeopleContract.SelPeopleModel createModel() {
        return new SelPeopleModelImpl();
    }
}
