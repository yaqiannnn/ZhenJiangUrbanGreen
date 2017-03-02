package com.nju.urbangreen.zhenjiangurbangreen.events;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Liwei on 2016/11/25.
 */
//事件记录类
public class OneEvent implements Serializable{

    private String code;
    private String name;
    private String type;
    private String location;
    private Date date_time;
    private String damageDegree;
    private String lostFee;//损失总价
    private String compensation;//实际赔偿
    private String relevantPerson;
    private String relevantLicensePlate;
    private String relevantContact;
    private String relevantCompany;
    private String relevantAddress;
    private String relevantDescription;
    private String registrar;
    private String description;



    private String reason;
    private int state;//0 :未提交；1 :已提交



    public OneEvent(String name, String registrar, String location, Date date_time){
        this.name = name;
        this.registrar = registrar;
        this.location = location;
        this.date_time = date_time;

    }

    public OneEvent(){}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public void setDate_time(String date_time_str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            this.date_time = sdf.parse(date_time_str);
        }catch (ParseException e){
            this.date_time = new Date();
        }

    }

    public String getDamageDegree() {
        return damageDegree;
    }

    public void setDamageDegree(String damageDegree) {
        this.damageDegree = damageDegree;
    }

    public String getLostFee() {
        return lostFee;
    }

    public void setLostFee(String lostFee) {
        this.lostFee = lostFee;
    }

    public String getCompensation() {
        return compensation;
    }

    public void setCompensation(String compensation) {
        this.compensation = compensation;
    }

    public String getRelevantPerson() {
        return relevantPerson;
    }

    public void setRelevantPerson(String relevantPerson) {
        this.relevantPerson = relevantPerson;
    }

    public String getRelevantLicensePlate() {
        return relevantLicensePlate;
    }

    public void setRelevantLicensePlate(String relevantLicensePlate) {
        this.relevantLicensePlate = relevantLicensePlate;
    }

    public String getRelevantContact() {
        return relevantContact;
    }

    public void setRelevantContact(String relevantContact) {
        this.relevantContact = relevantContact;
    }

    public String getRelevantCompany() {
        return relevantCompany;
    }

    public void setRelevantCompany(String relevantCompany) {
        this.relevantCompany = relevantCompany;
    }

    public String getRelevantAddress() {
        return relevantAddress;
    }

    public void setRelevantAddress(String relevantAddress) {
        this.relevantAddress = relevantAddress;
    }

    public String getRelevantDescription() {
        return relevantDescription;
    }

    public void setRelevantDescription(String relevantDescription) {
        this.relevantDescription = relevantDescription;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
