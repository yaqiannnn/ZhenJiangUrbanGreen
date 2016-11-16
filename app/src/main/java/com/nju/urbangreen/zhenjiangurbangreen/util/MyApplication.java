package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by HCQIN on 2016/11/6.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
