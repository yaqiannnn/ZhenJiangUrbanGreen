package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.util.Log;

import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObjects;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by lxs on 17-8-7.
 */

public class RealmUtils {

    public static void insertUGOs(final List<GreenObjects> objs) {
        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                for (GreenObjects obj : objs) {
//                    realm.copyToRealm(obj);
//                }
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                SPUtils.put("HasUGO", true);
//                Log.i("Realm", "Insert UGOs Success.");
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Log.i("Realm", "Insert UGOs Error");
//            }
//        });
        realm.beginTransaction();
        for(GreenObjects obj : objs) {
            realm.copyToRealm(obj);
        }
        SPUtils.put("HasUGO", true);
        realm.commitTransaction();
    }

    public static List<GreenObjects> getUGOs() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GreenObjects> objs = realm.where(GreenObjects.class).findAll();
        return realm.copyFromRealm(objs);
    }
}
