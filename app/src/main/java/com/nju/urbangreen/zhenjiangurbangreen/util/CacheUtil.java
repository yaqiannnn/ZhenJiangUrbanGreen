package com.nju.urbangreen.zhenjiangurbangreen.util;

import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObjects;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lxs on 17-8-10.
 */

public class CacheUtil {
    private static final String UGO_KEY = "HasUGO";

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

    public static void putUGOs(List<GreenObjects> objs) {
        instance().put(UGO_KEY, objs.toArray());
        SPUtils.put(UGO_KEY, true);
    }

    public static List<GreenObjects> getUGOs() {
        if(!hasUGOs())
            return null;
        return instance().<GreenObjects>getAsObjectList(UGO_KEY);
    }

    public static void removeUGOS() {
        instance().remove(UGO_KEY);
        SPUtils.put(UGO_KEY, false);
    }

}
