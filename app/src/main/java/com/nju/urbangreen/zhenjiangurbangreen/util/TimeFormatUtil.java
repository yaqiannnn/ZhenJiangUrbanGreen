package com.nju.urbangreen.zhenjiangurbangreen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lxs on 17-8-11.
 */

public class TimeFormatUtil {

    public static final String TimeFormat = "yyyy-MM-dd HH:mm";

    public static String format(String timeStr) {
        if(timeStr == null || timeStr.equals("")) {
            return "";
        }
        if(timeStr.contains("GMT")) {
            return new SimpleDateFormat(TimeFormat).format(new Date(timeStr));
        } else {
            try {
                return new SimpleDateFormat(TimeFormat).parse(timeStr).toString();
            } catch (ParseException e) {
                return timeStr;
            }
        }
    }
}
