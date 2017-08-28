package com.nju.urbangreen.zhenjiangurbangreen.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lxs on 17-8-11.
 */

public class TimeFormatUtil {

    public static final String OutputTimeFormat = "yyyy-MM-dd HH:mm";
    public static final String DBTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";

    public static String format(String timeStr) {
        if(timeStr == null || timeStr.equals("")) {
            return "";
        }
        if(timeStr.contains("GMT")) {
            return new SimpleDateFormat(OutputTimeFormat).format(new Date(timeStr));
        } else {
            try {
                Date time = new SimpleDateFormat(DBTimeFormat).parse(timeStr);
                return new SimpleDateFormat(OutputTimeFormat).format(time);
            } catch (ParseException e) {
                return timeStr;
            }
        }
    }
}
