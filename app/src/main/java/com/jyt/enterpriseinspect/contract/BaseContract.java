package com.jyt.enterpriseinspect.contract;

import android.content.Context;

/**
 * Created by v7 on 2016/12/22.
 */

public class BaseContract {
    public interface BaseModel {
        abstract void setContext(Context context);
    }

    public interface BaseView{
        abstract Context getContext();
    }
    public interface BasePresenter{
        abstract void setView(BaseView view);
        abstract BaseModel createModel();
        abstract void setContext(Context context);
    }
}
