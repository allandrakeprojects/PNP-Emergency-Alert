package com.example.pnpemergencyalert;

public class Information {
    public String name;
    public String address;
    public String email;
    public String gender;
    public String imageUrl;
    public String type;

    public Information(){

    }

    public Information(String name, String address, String email, String gender, String imageUrl, String type) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
