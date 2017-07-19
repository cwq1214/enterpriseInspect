package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.ModifyPsdContract;
import com.jyt.enterpriseinspect.model.ModifyPsdModelImpl;

/**
 * Created by v7 on 2016/12/23.
 */

public class ModifyPsdPresenterImpl extends BasePresenterImpl implements ModifyPsdContract.ModifyPsdPresenter {
    @Override
    public BaseContract.BaseModel createModel() {
        return new ModifyPsdModelImpl();
    }
}
