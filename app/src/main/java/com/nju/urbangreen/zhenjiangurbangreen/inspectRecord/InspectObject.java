package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lxs on 2016/11/20.
 */
public class InspectObject implements Serializable{

    private String ID;
    private String Code;
    private String InspectType;
    private Date InspectDate;
    private String CompanyID;
    private String Inspector;
    private String Score;
    private String Content;
    private String InspectOpinion;
    private String LoggerPID;
    private String LogTime;
    private String LastEditorPID;

    public InspectObject(String id,String code)
    {
        this.ID=id;
        this.Code=code;
        this.InspectType="";
        this.InspectDate=new Date();
        this.CompanyID="";
        this.Inspector="";
        this.Score="";
        this.Content="";
        this.InspectOpinion="";
        this.LoggerPID="";
        this.LogTime="";
        this.LastEditorPID="";
    }
    public InspectObject(String id,String code,String inspectType,Date inspectDate,String companyID,
                         String inspector,String score,String content,String inspectOpinion)
    {
        this.ID=id;
        this.Code=code;
        this.InspectType=inspectType;
        this.InspectDate=inspectDate;
        this.CompanyID=companyID;
        this.Inspector=inspector;
        this.Score=score;
        this.Content=content;
        this.InspectOpinion=inspectOpinion;
    }

    public String getID() {
        return ID;
    }
    public String getCode() {
        return Code;
    }
    public Date getInspectDate() {
        return InspectDate;
    }
    public String getInspectType() {
        return InspectType;
    }
    public String getCompanyID() {
        return CompanyID;
    }
    public String getInspector() {
        return Inspector;
    }
    public String getScore() {
        return Score;
    }
    public String getContent() {
        return Content;
    }
    public String getInspectOpinion() {
        return InspectOpinion;
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

    public void setID(String ID) {
        this.ID = ID;
    }
    public void setCode(String code) {
        Code = code;
    }
    public void setInspectDate(Date inspectDate) {
        InspectDate = inspectDate;
    }
    public void setInspector(String inspector) {
        Inspector = inspector;
    }
    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }
    public void setInspectType(String inspectType) {
        InspectType = inspectType;
    }
    public void setContent(String content) {
        Content = content;
    }
    public void setInspectOpinion(String inspectOpinion) {
        InspectOpinion = inspectOpinion;
    }
    public void setScore(String score) {
        Score = score;
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
}
