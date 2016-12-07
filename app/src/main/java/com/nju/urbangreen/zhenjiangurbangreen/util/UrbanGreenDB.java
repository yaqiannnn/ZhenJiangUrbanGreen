package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nju.urbangreen.zhenjiangurbangreen.events.OneEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/12/5.
 */
public class UrbanGreenDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "urban_green";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private  static UrbanGreenDB urbanGreenDB;

    private SQLiteDatabase db;

    /**
     *将构造方法私有化
     */
    private UrbanGreenDB(Context context){
        ZJUGDBOpenHelper dbOpenHelper = new ZJUGDBOpenHelper(context,DB_NAME,null,VERSION);
        db = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 获取UrbanGreenDB的实例
     */
    public synchronized static UrbanGreenDB getInstance(Context context){
        if(urbanGreenDB == null){
            urbanGreenDB = new UrbanGreenDB(context);
        }
        return urbanGreenDB;
    }

    /**
     * 将OneEvent实例存储到数据库
     */
    public void saveEvent(OneEvent oneEvent){
        if(oneEvent != null){
            Log.i("数据库","saveEvent");
            ContentValues values = new ContentValues();
            values.put("code",oneEvent.getCode());
            values.put("name",oneEvent.getName());
            values.put("type",oneEvent.getType());
            values.put("location",oneEvent.getLocation());
            values.put("date_time",oneEvent.getDate_time());
            values.put("damageDegree",oneEvent.getDamageDegree());
            values.put("lostFee",oneEvent.getLostFee());
            values.put("compensation",oneEvent.getCompensation());
            values.put("relevantLicensePlate",oneEvent.getRelevantLicensePlate());
            values.put("relevantContact",oneEvent.getRelevantContact());
            values.put("relevantCompany",oneEvent.getRelevantCompany());
            values.put("relevantAddress",oneEvent.getRelevantAddress());
            values.put("relevantDescription",oneEvent.getRelevantDescription());
            values.put("reason",oneEvent.getReason());
            values.put("state",oneEvent.getState());
            db.insert("Event",null,values);
        }
    }

    /**
     * 从数据库读取不同状态的Event记录
     * @param state 待上传(State = 0)或已上传(State = 1)
     * @return 返回相应状态的Event列表
     */
    public List<OneEvent> loadEventsWithDiffState(int state){
        Log.i("数据库","loadEventsWithDiffState");
        List<OneEvent> list = new ArrayList<OneEvent>();
        Cursor cursor = db.query("Event",null,"state = ?",new String[]{String.valueOf(state)},null,null,null);
        if(cursor.moveToFirst()){
            do {
                OneEvent oneEvent = new OneEvent();
                oneEvent.setCode(cursor.getString(cursor.getColumnIndex("code")));
                oneEvent.setName(cursor.getString(cursor.getColumnIndex("name")));
                oneEvent.setType(cursor.getString(cursor.getColumnIndex("type")));
                oneEvent.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                oneEvent.setDate_time(cursor.getString(cursor.getColumnIndex("date_time")));
                oneEvent.setDamageDegree(cursor.getString(cursor.getColumnIndex("damageDegree")));
                oneEvent.setLostFee(cursor.getFloat(cursor.getColumnIndex("lostFee")));
                oneEvent.setCompensation(cursor.getFloat(cursor.getColumnIndex("compensation")));
                oneEvent.setRelevantPerson(cursor.getString(cursor.getColumnIndex("relevantPerson")));
                oneEvent.setRelevantLicensePlate(cursor.getString(cursor.getColumnIndex("relevantLicensePlate")));
                oneEvent.setRelevantContact(cursor.getString(cursor.getColumnIndex("relevantContact")));
                oneEvent.setRelevantCompany(cursor.getString(cursor.getColumnIndex("relevantCompany")));
                oneEvent.setRelevantAddress(cursor.getString(cursor.getColumnIndex("relevantAddress")));
                oneEvent.setRelevantDescription(cursor.getString(cursor.getColumnIndex("relevantDescription")));
                oneEvent.setReason(cursor.getString(cursor.getColumnIndex("reason")));
                oneEvent.setState(state);
                list.add(oneEvent);
            }while (cursor.moveToNext());
        }
        return list;
    }
}
