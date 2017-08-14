package com.nju.urbangreen.zhenjiangurbangreen.util;

import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;

import java.util.List;

/**
 * Created by tommy on 2017/8/14.
 */

public class ListUtil {
    public static List<GreenObject> trim(List<GreenObject> list1,List<GreenObject> list2){
        for(GreenObject o : list1){
            for(GreenObject o2 : list2){
                if(o.UGO_ID.equals(o2.UGO_ID))
                    list1.remove(o);
            }
        }
        return list1;
    }
}
