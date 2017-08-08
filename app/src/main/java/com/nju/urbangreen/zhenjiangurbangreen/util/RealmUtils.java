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
        realm.beginTransaction();
        for(GreenObjects obj : objs) {
            realm.copyToRealm(obj);
        }
        realm.commitTransaction();
        SPUtils.put("HasUGO", true);
    }

    public static List<GreenObjects> getUGOs() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GreenObjects> objs = realm.where(GreenObjects.class).findAll();
        return realm.copyFromRealm(objs);
    }

    public static void removeUGOs() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GreenObjects> objs = realm.where(GreenObjects.class).findAll();
        realm.beginTransaction();
        objs.deleteAllFromRealm();
        realm.commitTransaction();
        SPUtils.put("HasUGO", false);
    }
}
