package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.util.Log;

import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObjectSug;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;

import java.util.List;

/**
 * Created by lxs on 17-8-10.
 */

public class CacheUtil {
    private static final String UGO_KEY = "HasUGO", UGO_SUG_KEY = "HasUGOSug";

    private static ACache m_cache = null;

    private static ACache instance() {
        if(m_cache == null) {
            m_cache = ACache.get(MyApplication.getContext());
            return m_cache;
        }
        else {
            return m_cache;
        }
    }

    public static boolean hasUGOs() {
        return SPUtils.getBool(UGO_KEY, false);
    }

    public static void putUGOs(List<GreenObject> objs) {
        instance().put(UGO_KEY, objs.toArray());
        SPUtils.put(UGO_KEY, true);
    }

    public static List<GreenObject> getUGOs() {
        if(!hasUGOs())
            return null;
        Log.d("test2",instance().<GreenObject>getAsObjectList(UGO_KEY)+"");
        return instance().<GreenObject>getAsObjectList(UGO_KEY);
    }

    public static void removeUGOS() {
        instance().remove(UGO_KEY);
        SPUtils.put(UGO_KEY, false);
    }

    public static boolean hasUGOSug() {
        return SPUtils.getBool(UGO_SUG_KEY, false);
    }

    public static void putUGOSug(List<GreenObjectSug> objs) {
        String IDs[] = new String[objs.size()];
        String Addresses[] = new String[objs.size()];
        int i = 0;
        for(GreenObjectSug obj : objs) {
            IDs[i] = obj.UGO_ID;
            Addresses[i] = obj.UGO_Address;
            i++;
        }
        instance().put(UGO_SUG_KEY + "UGO_ID", IDs);
        instance().put(UGO_SUG_KEY + "UGO_Address", Addresses);
        SPUtils.put(UGO_SUG_KEY, true);
    }

    public static String[] getUGOSug(String column) {
        if(!hasUGOSug()) {
            return null;
        }
        switch (column) {
            case "UGO_ID":
                return (String[]) instance().<String>getAsObjectList(UGO_SUG_KEY + "UGO_ID").toArray();
            case "UGO_Address":
                return (String[]) instance().<String>getAsObjectList(UGO_SUG_KEY + "UGO_Address").toArray();
            default:
                return null;
        }
    }

    public static void removeUGOSug() {
        instance().remove(UGO_SUG_KEY + "UGO_ID");
        instance().remove(UGO_SUG_KEY + "UGO_Address");
        SPUtils.put(UGO_SUG_KEY, false);
    }

    public static String getFileLocalPath(String fileID) {
        return instance().getAsString(fileID);
    }

    public static void putFileLocalPath(String fileID, String localPath) {
        instance().put(fileID, localPath);
    }

    public static void removeFileLocalPath(String fileID) {
        instance().remove(fileID);
    }

}
