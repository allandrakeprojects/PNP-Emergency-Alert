package com.example.pnpemergencyalert;

public class Alerts {

    public String police_uid;
    public String police_name;
    public String name;
    public String imageUrl;
    public String lat;
    public String lng;
    public String datetime;
    public String status;
    public boolean read_ontheway;

    public Alerts(){

    }

    public Alerts(String police_uid, String police_name, String name, String imageUrl, String lat, String lng, String datetime, String status, boolean read_ontheway) {
        this.police_uid = police_uid;
        this.police_name = police_name;
        this.name = name;
        this.imageUrl = imageUrl;
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

    public String getPolice_name() {
        return police_name;
    }

    public void setPolice_name(String police_name) {
        this.police_name = police_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
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