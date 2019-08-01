package com.example.pnpemergencyalert;

public class Alerts {

    public String c_uid;
    public String c_name;
    public String c_imgUrl;
    public String c_lat;
    public String c_lng;
    public String c_datecreated;
    public String p_uid;
    public String p_name;
    public String p_status;
    public boolean c_read;

    public Alerts(){

    }

    public Alerts(String c_uid, String c_name, String c_imgUrl, String c_lat, String c_lng, String c_datecreated, String p_uid, String p_name, String p_status, boolean c_read) {
        this.c_uid = c_uid;
        this.c_name = c_name;
        this.c_imgUrl = c_imgUrl;
        this.c_lat = c_lat;
        this.c_lng = c_lng;
        this.c_datecreated = c_datecreated;
        this.p_uid = p_uid;
        this.p_name = p_name;
        this.p_status = p_status;
        this.c_read = c_read;
    }

    public String getC_uid() {
        return c_uid;
    }

    public void setC_uid(String c_uid) {
        this.c_uid = c_uid;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_imgUrl() {
        return c_imgUrl;
    }

    public void setC_imgUrl(String c_imgUrl) {
        this.c_imgUrl = c_imgUrl;
    }

    public String getC_lat() {
        return c_lat;
    }

    public void setC_lat(String c_lat) {
        this.c_lat = c_lat;
    }

    public String getC_lng() {
        return c_lng;
    }

    public void setC_lng(String c_lng) {
        this.c_lng = c_lng;
    }

    public String getC_datecreated() {
        return c_datecreated;
    }

    public void setC_datecreated(String c_datecreated) {
        this.c_datecreated = c_datecreated;
    }

    public String getP_uid() {
        return p_uid;
    }

    public void setP_uid(String p_uid) {
        this.p_uid = p_uid;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_status() {
        return p_status;
    }

    public void setP_status(String p_status) {
        this.p_status = p_status;
    }

    public boolean isC_read() {
        return c_read;
    }

    public void setC_read(boolean c_read) {
        this.c_read = c_read;
    }
}