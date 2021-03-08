package com.example.myonlineshop.model;

public class Users {

    private  String pass, phone,user,image,address ;

    public Users() {
    }


    public Users(String pass, String phone, String user, String image, String address) {
        this.pass = pass;
        this.phone = phone;
        this.user = user;
        this.image = image;
        this.address = address;
    }


    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
