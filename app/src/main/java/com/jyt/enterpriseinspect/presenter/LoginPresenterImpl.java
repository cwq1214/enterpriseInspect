package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.LoginContract;
import com.jyt.enterpriseinspect.model.LoginModelImpl;

/**
 * Created by v7 on 2016/12/22.
 */

public class LoginPresenterImpl extends BasePresenterImpl implements LoginContract.LoginPresenter {
    @Override
    public BaseContract.BaseModel createModel() {
        return new LoginModelImpl();
    }

}
