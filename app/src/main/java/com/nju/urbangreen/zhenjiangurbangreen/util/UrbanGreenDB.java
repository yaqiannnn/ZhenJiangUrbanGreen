package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.nju.urbangreen.zhenjiangurbangreen.events.OneEvent;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.Inspect;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.Maintain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Liwei on 2016/12/5.
 */
public class UrbanGreenDB {

    //数据库名
    public static final String DB_NAME = Environment.getExternalStorageDirectory() + File.separator + "NARUTO/" + "urban_green";

    //数据库版本
    public static final int VERSION = 1;

    private static UrbanGreenDB urbanGreenDB;

    private SQLiteDatabase db;

    //将构造方法私有化
    private UrbanGreenDB(Context context) {

        ZJUGDBOpenHelper dbOpenHelper = new ZJUGDBOpenHelper(context, DB_NAME, null, VERSION);
        db = dbOpenHelper.getWritableDatabase();
    }

    //获取UrbanGreenDB的实例
    public synchronized static UrbanGreenDB getInstance(Context context) {
        if (urbanGreenDB == null) {
            urbanGreenDB = new UrbanGreenDB(context);
        }
        return urbanGreenDB;
    }

    //更新养护对象基本信息,行道树除外
    public boolean updateUGOInfoExceptST(Map<String, Object> ugos) {
        db.beginTransaction();
        try {
            emptyTable("UGOInfo");
            Map<String, Object> oneUGO;
            for (Map.Entry<String, Object> entry : ugos.entrySet()) {
                oneUGO = (Map<String, Object>) entry.getValue();
                saveOneUGOInfo(oneUGO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }

        return true;
    }

    //将一个养护对象的基本信息保存到数据库中
    public boolean saveOneUGOInfo(Map<String, Object> ugo) {
        if (ugo != null) {
            ContentValues values = new ContentValues();
            values.put("code", ugo.get("UGO_Ucode").toString());
            values.put("parent_id", ugo.get("UGO_ParentID").toString());
            values.put("type_id", ugo.get("UGO_ClassType_ID").toString());
            values.put("name", ugo.get("UGO_Name").toString());
            values.put("location", ugo.get("UGO_Geo_Location").toString());
            values.put("address", ugo.get("UGO_Address").toString());
            values.put("spatial_description", ugo.get("UGO_SpatialDescription").toString());
            values.put("description", ugo.get("UGO_Description").toString());
            values.put("current_area", Float.parseFloat(ugo.get("UGO_CurrentArea").toString()));
            values.put("current_owner", ugo.get("UGO_CurrentOwner").toString());
            values.put("current_owner_type", ugo.get("UGO_CurrentOwnerType").toString());
            values.put("is_destroyed", ugo.get("UGO_Destroyed").toString());
            values.put("date_destroyed", ugo.get("UGO_DateOfDestroyed").toString());
            values.put("date_register_destroyed", ugo.get("UGO_DateOfDestroyRecord").toString());
            values.put("logger_pid_destroyed", ugo.get("UGO_LoggerPIDOfDestroyRecord").toString());
            values.put("logger_pid", ugo.get("UGO_LoggerPID").toString());
            values.put("log_time", ugo.get("UGO_LogTime").toString());
            values.put("lasteditor_pid", ugo.get("UGO_LastEditorPID").toString());
            values.put("lastedit_time", ugo.get("UGO_LastEditTime").toString());
            db.insert("UGOInfo", null, values);
            return true;
        }
        return false;
    }

    /**
     * 从数据库读取不同状态的Event记录
     * @param state 待上传(State = 0)或已上传(State = 1)
     * @return 返回相应状态的Event列表
     */
    public List<OneEvent> loadEventsWithDiffState(int state) {
        Log.i("数据库", "loadEventsWithDiffState");
        List<OneEvent> list = new ArrayList<OneEvent>();
        Cursor cursor = db.query("Event", null, "state = ?", new String[]{String.valueOf(state)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                OneEvent oneEvent = new OneEvent();
                oneEvent.setUGE_Code(cursor.getString(cursor.getColumnIndex("code")));
                oneEvent.setUGE_Name(cursor.getString(cursor.getColumnIndex("name")));
                oneEvent.setUGE_Time(cursor.getString(cursor.getColumnIndex("type")));
                oneEvent.setUGE_Location(cursor.getString(cursor.getColumnIndex("location")));
                oneEvent.setUGE_Time(cursor.getString(cursor.getColumnIndex("date_time")));
                oneEvent.setUGE_DamageDegree(cursor.getString(cursor.getColumnIndex("damageDegree")));
                oneEvent.setUGE_LostFee(String.valueOf(cursor.getFloat(cursor.getColumnIndex("lostFee"))));
                oneEvent.setUGE_Compensation(String.valueOf(cursor.getFloat(cursor.getColumnIndex("compensation"))));
                oneEvent.setUGE_RelevantPerson(cursor.getString(cursor.getColumnIndex("relevantPerson")));
                oneEvent.setUGE_RelevantLicensePlate(cursor.getString(cursor.getColumnIndex("relevantLicensePlate")));
                oneEvent.setUGE_RelevantContact(cursor.getString(cursor.getColumnIndex("relevantContact")));
                oneEvent.setUGE_RelevantCompany(cursor.getString(cursor.getColumnIndex("relevantCompany")));
                oneEvent.setUGE_RelevantAddress(cursor.getString(cursor.getColumnIndex("relevantAddress")));
                oneEvent.setUGE_RelevantDescription(cursor.getString(cursor.getColumnIndex("relevantDescription")));
                oneEvent.setUGE_Description(cursor.getString(cursor.getColumnIndex("description")));
                oneEvent.setUGE_Reason(cursor.getString(cursor.getColumnIndex("reason")));
                oneEvent.setRegistrar(cursor.getString(cursor.getColumnIndex("registrar")));
                oneEvent.setState(state);
                list.add(oneEvent);
            } while (cursor.moveToNext());
        }
        return list;
    }

    //将一条事件记录存储到数据库
    public boolean saveEvent(OneEvent oneEvent) {
        if (oneEvent != null) {
            Log.i("数据库", "saveEvent");
            ContentValues values = new ContentValues();
            values.put("code", oneEvent.getUGE_Code());
            values.put("name", oneEvent.getUGE_Name());
            values.put("type", oneEvent.getUGE_Type());
            values.put("location", oneEvent.getUGE_Location());
            values.put("date_time", oneEvent.getUGE_Time().toString());
            values.put("damageDegree", oneEvent.getUGE_DamageDegree());
            values.put("lostFee", oneEvent.getUGE_LostFee());
            values.put("compensation", oneEvent.getUGE_Compensation());
            values.put("relevantPerson", oneEvent.getUGE_RelevantPerson());
            values.put("relevantLicensePlate", oneEvent.getUGE_RelevantLicensePlate());
            values.put("relevantContact", oneEvent.getUGE_RelevantContact());
            values.put("relevantCompany", oneEvent.getUGE_RelevantCompany());
            values.put("relevantAddress", oneEvent.getUGE_RelevantAddress());
            values.put("relevantDescription", oneEvent.getUGE_RelevantDescription());
            values.put("description", oneEvent.getUGE_Description());
            values.put("reason", oneEvent.getUGE_Reason());
            values.put("registrar", SPUtils.get("username", "xk").toString());
            values.put("state", oneEvent.getState());
            db.insert("Event", null, values);
            return true;
        }
        return false;
    }

    //将一条养护记录存储到数据库
//    public boolean saveMaintain(Maintain oneMaintain) {
//        if (oneMaintain != null) {
//            ContentValues values = new ContentValues();
//            values.put("code", oneMaintain.MR_Code);
//            values.put("company_id", oneMaintain.MR_CompanyID);
//            values.put("maintain_type", oneMaintain.getMaintainType());
//            values.put("maintain_staff", oneMaintain.getMaintainStaff());
//            values.put("maintain_date", oneMaintain.getMaintainDate().toString());
//            values.put("content", oneMaintain.getContent());
//            values.put("logger_pid", oneMaintain.getLoggerPID());
//            values.put("log_time", oneMaintain.getLogTime());
//            values.put("lasteditor_pid", oneMaintain.getLastEditorPID());
//            values.put("lastedit_time", "");
//            db.insert("Maintain", null, values);
//            return true;
//        }
//        return false;
//    }

    //将一条巡查记录存储到数据库
    public boolean saveInspect(Inspect oneInspect) {
        if (oneInspect != null) {
            ContentValues values = new ContentValues();
            values.put("code", oneInspect.getCode());
            values.put("inspect_type", oneInspect.getInspectType());
            values.put("inspect_date", oneInspect.getInspectDate().toString());
            values.put("company_id", oneInspect.getCompanyID());
            values.put("inspector", oneInspect.getInspector());
            values.put("score", oneInspect.getScore());
            values.put("content", oneInspect.getContent());
            values.put("inspect_opinion", oneInspect.getInspectOpinion());
            values.put("logger_pid", oneInspect.getLoggerPID());
            values.put("log_time", oneInspect.getLogTime());
            values.put("lasteditor_pid", oneInspect.getLastEditorPID());
            values.put("lastedit_time", "");
            db.insert("Inspect", null, values);
            return true;
        }
        return false;
    }

    //将一条事件记录从数据库中删除,根据事件编号来删除
    public void deleteEvent(int code) {

    }

    //清空某个表中的数据
    public void emptyTable(String tableName) {
        String sql = "delete from " + tableName + ";";
        db.execSQL(sql);
    }
}
