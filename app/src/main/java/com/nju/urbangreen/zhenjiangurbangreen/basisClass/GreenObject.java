package com.nju.urbangreen.zhenjiangurbangreen.basisClass;

/**
 * Created by Liwei on 2017/4/13.
 */
import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Liwei on 2017/4/13.
 */
public class GreenObject implements Serializable {
    private static final long serialVersionUID = 21455356667888L;
    public String UGO_ID;
    public String UGO_Ucode;
    public String UGO_ParentID;
    public String UGO_ClassType_ID;
    public String UGO_Name;
    public String UGO_Geo_Location;
    public String UGO_Address;
    public float UGO_CurrentArea;
    public String UGO_CurrentOwner;
    public String UGO_CurrentOwnerType;
    public String UGO_SpatialDescription;
    public String UGO_Description;
    public String UGO_LoggerPID;
    public String UGO_LogTime;
    public String UGO_LastEditorPID;
    public String UGO_LastEditTime;
    public boolean UGO_Destroyed;
    public String UGO_DateOfDestroyed;
    public String UGO_DateOfDestroyRecord;
    public String UGO_LoggerPIDOfDestroyRecord;

}
