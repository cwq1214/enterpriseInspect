package com.jyt.enterpriseinspect;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.orhanobut.hawk.Hawk;

/**
 * Created by v7 on 2016/12/22.
 */

public class App extends Application {

    public static final String baidu_ak="Ke6q1Q0KKEFX0QWNRXdAmVVKZRYZwS9D";
    public static final int baidu_appId = 9118014;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());

        Hawk.init(getApplicationContext()).build();
    }
}
