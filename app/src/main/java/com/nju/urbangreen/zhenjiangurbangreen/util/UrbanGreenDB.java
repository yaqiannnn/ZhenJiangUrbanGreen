package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.nju.urbangreen.zhenjiangurbangreen.events.OneEvent;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectObject;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/12/5.
 */
public class UrbanGreenDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = Environment.getExternalStorageDirectory() + File.separator + "NARUTO/" + "urban_green";

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
     * 将一条事件记录存储到数据库
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
            values.put("relevantPerson",oneEvent.getRelevantPerson());
            values.put("relevantLicensePlate",oneEvent.getRelevantLicensePlate());
            values.put("relevantContact",oneEvent.getRelevantContact());
            values.put("relevantCompany",oneEvent.getRelevantCompany());
            values.put("relevantAddress",oneEvent.getRelevantAddress());
            values.put("relevantDescription",oneEvent.getRelevantDescription());
            values.put("description",oneEvent.getDescription());
            values.put("reason",oneEvent.getReason());
            values.put("registrar",SPUtils.get(MyApplication.getContext(),"username","zhangliwei").toString());
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
                oneEvent.setLostFee(String.valueOf(cursor.getFloat(cursor.getColumnIndex("lostFee"))));
                oneEvent.setCompensation(String.valueOf(cursor.getFloat(cursor.getColumnIndex("compensation"))));
                oneEvent.setRelevantPerson(cursor.getString(cursor.getColumnIndex("relevantPerson")));
                oneEvent.setRelevantLicensePlate(cursor.getString(cursor.getColumnIndex("relevantLicensePlate")));
                oneEvent.setRelevantContact(cursor.getString(cursor.getColumnIndex("relevantContact")));
                oneEvent.setRelevantCompany(cursor.getString(cursor.getColumnIndex("relevantCompany")));
                oneEvent.setRelevantAddress(cursor.getString(cursor.getColumnIndex("relevantAddress")));
                oneEvent.setRelevantDescription(cursor.getString(cursor.getColumnIndex("relevantDescription")));
                oneEvent.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                oneEvent.setReason(cursor.getString(cursor.getColumnIndex("reason")));
                oneEvent.setRegistrar(cursor.getString(cursor.getColumnIndex("registrar")));
                oneEvent.setState(state);
                list.add(oneEvent);
            }while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 将一条事件记录从数据库中删除
     * @param code 根据事件编号来删除
     */
    public void deleteEvent(int code){

    }

    /**
     * 将一条养护记录存储到数据库
     */
    public void saveMaintain(MaintainObject oneMaintain){
        if(oneMaintain != null){
            ContentValues values = new ContentValues();
            values.put("code",oneMaintain.getCode());
            values.put("company_id",oneMaintain.getCompanyID());
            values.put("maintain_type",oneMaintain.getMaintainType());
            values.put("maintain_staff",oneMaintain.getMaintainStaff());
            values.put("maintain_date",oneMaintain.getMaintainDate().toString());
            values.put("content",oneMaintain.getContent());
            values.put("logger_pid",oneMaintain.getLoggerPID());
            values.put("log_time",oneMaintain.getLogTime());
            values.put("lasteditor_pid",oneMaintain.getLastEditorPID());
            values.put("lastedit_time","");
        }
    }

    /**
     * 将一条巡查记录存储到数据库
     */
    public void saveInspect(InspectObject oneInspect){
        if(oneInspect != null){
            ContentValues values = new ContentValues();
            values.put("code",oneInspect.getCode());
            values.put("inspect_type",oneInspect.getInspectType());
            values.put("inspect_date",oneInspect.getInspectDate().toString());
            values.put("company_id",oneInspect.getCompanyID());
            values.put("inspector",oneInspect.getInspector());
            values.put("score",oneInspect.getScore());
            values.put("content",oneInspect.getContent());
            values.put("inspect_opinion",oneInspect.getInspectOpinion());
            values.put("logger_pid",oneInspect.getLoggerPID());
            values.put("log_time",oneInspect.getLogTime());
            values.put("lasteditor_pid",oneInspect.getLastEditorPID());
            values.put("lastedit_time","");
        }
    }
}
