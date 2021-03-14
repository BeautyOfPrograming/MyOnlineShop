package com.example.myonlineshop.model;

public class AdminOrders {
    private  String name, phone ,address,date,time,city,totalamount,state;


    public AdminOrders() {
    }

    public AdminOrders(String name, String phone, String address, String date, String time, String city, String totalamount, String state) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.date = date;
        this.time = time;
        this.city = city;
        this.totalamount = totalamount;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }



}
