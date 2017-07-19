package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.MainContract;
import com.jyt.enterpriseinspect.model.MainModelImpl;

/**
 * Created by v7 on 2016/12/22.
 */

public class MainPresenterImpl extends BasePresenterImpl implements MainContract.MainPresenter{
    @Override
    public BaseContract.BaseModel createModel() {
        return new MainModelImpl();
    }
}
