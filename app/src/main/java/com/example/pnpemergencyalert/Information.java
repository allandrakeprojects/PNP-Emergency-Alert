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
}
