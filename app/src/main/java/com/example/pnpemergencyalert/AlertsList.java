package com.example.pnpemergencyalert;

public class AlertsList {

    public String name_uid;
    public String police_uid;
    public String lat;
    public String lng;
    public String datetime;
    public String status;
    public String imageUrl;

    public AlertsList(){

    }

    public AlertsList(String name_uid, String police_uid, String lat, String lng, String datetime, String status, String imageUrl) {
        this.name_uid = name_uid;
        this.police_uid = police_uid;
        this.lat = lat;
        this.lng = lng;
        this.datetime = datetime;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public String getName_uid() {
        return name_uid;
    }

    public void setName_uid(String name_uid) {
        this.name_uid = name_uid;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
