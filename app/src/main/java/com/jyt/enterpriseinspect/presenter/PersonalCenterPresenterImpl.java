package com.jyt.enterpriseinspect.presenter;

import android.content.Context;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.PersonalCenterContract;
import com.jyt.enterpriseinspect.model.PersonalCenterModelImpl;
import com.jyt.enterpriseinspect.presenter.BasePresenterImpl;

/**
 * Created by v7 on 2016/12/28.
 */

public class PersonalCenterPresenterImpl extends BasePresenterImpl<PersonalCenterContract.PersonalCenterModel,PersonalCenterContract.PersonalCenterView> implements PersonalCenterContract.PersonalCenterPresenter{

    @Override
    public PersonalCenterContract.PersonalCenterModel createModel() {
        return new PersonalCenterModelImpl();
    }
}
