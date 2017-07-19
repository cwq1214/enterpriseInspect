package com.jyt.enterpriseinspect.presenter;

import com.jyt.enterpriseinspect.contract.PersonalCenterContract;
import com.jyt.enterpriseinspect.contract.PrintAskOrderContract;
import com.jyt.enterpriseinspect.model.PrintAskOrderModelImpl;

/**
 * Created by v7 on 2016/12/28.
 */

public class PrintAskOrderPresenterImpl extends BasePresenterImpl<PrintAskOrderContract.PrintAskOrderModel,PersonalCenterContract.PersonalCenterView> implements PrintAskOrderContract.PrintAskOrderPresenter{
    @Override
    public PrintAskOrderContract.PrintAskOrderModel createModel() {
        return new PrintAskOrderModelImpl();
    }
}
