package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.app.Application;
import android.content.Context;

import com.nju.urbangreen.zhenjiangurbangreen.BuildConfig;

import net.gotev.uploadservice.UploadService;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by HCQIN on 2016/11/6.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // Init Real
        Realm.init(this);
        RealmConfiguration config = new  RealmConfiguration.Builder()
                .name("myRealm.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
        // Init Upload Service
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
