package com.nju.urbangreen.zhenjiangurbangreen.util;

/**
 * Created by lxs on 17-8-11.
 */

public class TimeFormatUtil {

    public static String format(String timeStr) {
        if(timeStr == null || timeStr.equals("")) {
            return "";
        }
        else {
            String dates[] = timeStr.split("T");
            if(dates.length <= 1)
                return timeStr;
            String times[] = dates[1].split(":");
            return dates[0] + " " + times[0] + ":" + times[1];
        }
    }
}
