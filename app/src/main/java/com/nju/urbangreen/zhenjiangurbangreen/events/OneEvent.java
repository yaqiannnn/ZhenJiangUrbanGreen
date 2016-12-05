package com.nju.urbangreen.zhenjiangurbangreen.events;

/**
 * Created by Liwei on 2016/11/25.
 */
//事件记录类
public class OneEvent {

    private String code;
    private String name;
    private String type;
    private String location;
    private String date_time;
    private String damageDegree;
    private float lostFee;//损失总价
    private float compensation;//实际赔偿
    private String relevantPerson;
    private String relevantLicensePlate;
    private String relevantContact;
    private String relevantCompany;
    private String relevantAddress;
    private String relevantDescription;
    private String registrar;
    private String reason;
    private int state;//0 :未提交；1 :已提交



    public OneEvent(String name, String registrar, String location, String date_time){
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

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDamageDegree() {
        return damageDegree;
    }

    public void setDamageDegree(String damageDegree) {
        this.damageDegree = damageDegree;
    }

    public float getLostFee() {
        return lostFee;
    }

    public void setLostFee(float lostFee) {
        this.lostFee = lostFee;
    }

    public float getCompensation() {
        return compensation;
    }

    public void setCompensation(float compensation) {
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
