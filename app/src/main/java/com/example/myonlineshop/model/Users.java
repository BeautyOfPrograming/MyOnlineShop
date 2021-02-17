package com.example.myonlineshop.model;

public class Users {

    private  String pass, phone,user;

    public Users() {
    }

    public Users(String pass, String phone, String user) {
        this.pass = pass;
        this.phone = phone;
        this.user = user;
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
}
