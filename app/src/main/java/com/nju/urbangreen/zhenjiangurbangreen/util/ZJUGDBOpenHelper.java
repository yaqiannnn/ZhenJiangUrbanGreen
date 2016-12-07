package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Liwei on 2016/12/5.
 */
public class ZJUGDBOpenHelper extends SQLiteOpenHelper {

    /**
     * Event建表语句
     */
    public static final String CREATE_EVENT = "create table Event ("
            + "id integer primary key autoincrement,"
            + "code text,"
            + "name text,"
            + "type text,"
            + "location text,"
            + "date_time text,"
            + "damageDegree text,"
            + "lostFee real,"
            + "compensation real,"
            + "relevantPerson text,"
            + "relevantLicensePlate text,"
            + "relevantContact text,"
            + "relevantCompany text,"
            + "relevantAddress text,"
            + "relevantDescription text,"
            + "reason text,"
            + "state integer)";

    /**
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public ZJUGDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
