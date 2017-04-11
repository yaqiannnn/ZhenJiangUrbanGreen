package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lxs on 2016/11/28.
 */
public class MaintainObject implements Serializable{
    private String ID;
    private String Code;
    private String CompanyID;
    private String MaintainType;
    private String MaintainStaff;
    private Date MaintainDate;
    private String Content;
    private String LoggerPID;
    private String LogTime;
    private String LastEditorPID;

    private int state;//0 :未提交；1 :已提交

    public MaintainObject(String id,String code)
    {
        this.ID=id;
        this.Code=code;
        this.MaintainType="";
        this.MaintainDate=new Date();
        this.CompanyID="";
        this.MaintainStaff="";
        this.Content="";
        this.LoggerPID="";
        this.LogTime="";
        this.LastEditorPID="";
    }

    public MaintainObject(String id,String code,String companyID,String maintainType
                          ,String maintainStaff,Date maintainDate,String content)
    {
        this.ID=id;
        this.Code=code;
        this.MaintainType=maintainType;
        this.MaintainDate=maintainDate;
        this.CompanyID=companyID;
        this.MaintainStaff=maintainStaff;
        this.Content=content;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public void setCode(String code) {
        Code = code;
    }
    public void setContent(String content) {
        Content = content;
    }
    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }
    public void setMaintainDate(Date maintainDate) {
        MaintainDate = maintainDate;
    }
    public void setMaintainStaff(String maintainStaff) {
        MaintainStaff = maintainStaff;
    }
    public void setMaintainType(String maintainType) {
        MaintainType = maintainType;
    }
    public void setLoggerPID(String loggerPID) {
        LoggerPID = loggerPID;
    }
    public void setLogTime(String logTime) {
        LogTime = logTime;
    }
    public void setLastEditorPID(String lastEditorPID) {
        LastEditorPID = lastEditorPID;
    }
    public void setState(int state) {
        this.state = state;
    }

    public String getID() {
        return ID;
    }
    public String getCode() {
        return Code;
    }
    public Date getMaintainDate() {
        return MaintainDate;
    }
    public String getCompanyID() {
        return CompanyID;
    }
    public String getContent() {
        return Content;
    }
    public String getMaintainStaff() {
        return MaintainStaff;
    }
    public String getMaintainType() {
        return MaintainType;
    }
    public String getLoggerPID() {
        return LoggerPID;
    }
    public String getLogTime() {
        return LogTime;
    }
    public String getLastEditorPID() {
        return LastEditorPID;
    }
    public int getState() {
        return state;
    }
}
