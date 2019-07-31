package com.example.pnpemergencyalert;

public class Alerts {

    public String police_uid;
    public String lat;
    public String lng;
    public Long datetime;
    public String status;
    public boolean read_ontheway;

    public Alerts(){

    }

    public Alerts(String police_uid, String lat, String lng, Long datetime, String status, boolean read_ontheway) {
        this.police_uid = police_uid;
        this.lat = lat;
        this.lng = lng;
        this.datetime = datetime;
        this.status = status;
        this.read_ontheway = read_ontheway;
    }

    public String getPolice_uid() {
        return police_uid;
    }

    public void setPolice_uid(String police_uid) {
        this.police_uid = police_uid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getRead_ontheway() {
        return read_ontheway;
    }

    public void setRead_ontheway(boolean read_ontheway) {
        this.read_ontheway = read_ontheway;
    }
}
