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
            + "description text,"
            + "reason text,"
            + "registrar text,"
            + "logger_pid text,"
            + "log_time text,"
            + "lasteditor_pid text,"
            + "lastedit_time text,"
            + "state integer)";
    /**
     * Inspect建表语句
     */
    public static final String CREATE_INSPECT = "create table Inspect ("
            + "id integer primary key autoincrement,"
            + "code text,"
            + "inspect_type text,"
            + "inspect_date text"
            + "company_id text,"
            + "inspector text,"
            + "score text,"
            + "content text,"
            + "inspect_opinion text,"
            + "logger_pid text,"
            + "log_time text,"
            + "lasteditor_pid text,"
            + "lastedit_time text)";
    /**
     * Maintain建表语句
     */
    public static final String CREATE_MAINTAIN = "create table Maintain ("
            + "id integer primary key autoincrement,"
            + "code text,"
            + "company_id text,"
            + "maintain_type text,"
            + "maintain_staff text,"
            + "maintain_date text,"
            + "content text,"
            + "logger_pid text,"
            + "log_time text,"
            + "lasteditor_pid text,"
            + "lastedit_time text)";

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
        sqLiteDatabase.execSQL(CREATE_MAINTAIN);
        sqLiteDatabase.execSQL(CREATE_INSPECT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
