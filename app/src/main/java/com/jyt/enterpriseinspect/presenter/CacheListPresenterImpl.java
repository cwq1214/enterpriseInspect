package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.contract.CacheListContract;
import com.jyt.enterpriseinspect.model.CacheListModelImpl;

/**
 * Created by v7 on 2016/12/27.
 */

public class CacheListPresenterImpl extends BasePresenterImpl implements CacheListContract.CachePresenter {
    @Override
    public BaseContract.BaseModel createModel() {
        return new CacheListModelImpl();
    }
}
