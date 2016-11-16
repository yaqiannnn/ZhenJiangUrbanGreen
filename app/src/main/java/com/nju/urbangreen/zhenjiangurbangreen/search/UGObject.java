package com.nju.urbangreen.zhenjiangurbangreen.search;

/**
 * Created by lxs on 2016/9/30.
 */
public class UGObject {
    private String ID;
    private String Ucode;
    private String classType_ID;
    private String name;
    private String Geo_location;
    private String address;
    private float area;
    private String spatialDescription;

    public UGObject(String ID,String Ucode,String classType_ID,String name,String Geo_location,
                    String address,float area,String spatialDescription) {
        this.ID=ID;
        this.Ucode=Ucode;
        this.classType_ID=classType_ID;
        this.name=name;
        this.Geo_location=Geo_location;
        this.address=address;
        this.area=area;
        this.spatialDescription=spatialDescription;
    }

    public float getArea() {
        return area;
    }

    public String getAddress() {
        return address;
    }

    public String getClassType_ID() {
        return classType_ID;
    }

    public String getGeo_location() {
        return Geo_location;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSpatialDescription() {
        return spatialDescription;
    }

    public String getUcode() {
        return Ucode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public void setClassType_ID(String classType_ID) {
        this.classType_ID = classType_ID;
    }

    public void setGeo_location(String geo_location) {
        this.Geo_location = geo_location;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpatialDescription(String spatialDescription) {
        this.spatialDescription = spatialDescription;
    }

    public void setUcode(String ucode) {
        this.Ucode = ucode;
    }
}
