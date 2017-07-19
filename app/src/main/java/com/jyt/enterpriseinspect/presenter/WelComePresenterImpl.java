package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.WelComeContract;
import com.jyt.enterpriseinspect.model.WelComeModelImpl;

/**
 * Created by v7 on 2016/12/22.
 */

public class WelComePresenterImpl extends BasePresenterImpl implements WelComeContract.WelComePresenter {
    @Override
    public BaseContract.BaseModel createModel() {
        return new WelComeModelImpl();
    }
}
