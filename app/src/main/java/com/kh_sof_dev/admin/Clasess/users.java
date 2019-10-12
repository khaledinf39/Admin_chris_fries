package com.kh_sof_dev.admin.Clasess;

import android.content.Context;
import android.content.SharedPreferences;

import com.kh_sof_dev.admin.Activities.Odrer_activity;

import static android.content.Context.MODE_PRIVATE;

public class users {
    private  String name,phone,id,address;
    private  String nb,token,pointDay="الكل";
    private int request_wail_nb;
private Double wallet=0.0;

    public static boolean getpermissions(Context mcx, String checkout) {
        SharedPreferences sp=mcx.getSharedPreferences("user_info", MODE_PRIVATE);
       String mpermissions=sp.getString("permissions"," ");
       Boolean res=false;
        if (mpermissions.contains(checkout)){
            res=true;
        }
        return res;
    }

    public String getPointDay() {
        return pointDay;
    }

    public void setPointDay(String pointDay) {
        this.pointDay = pointDay;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }

    public int getRequest_wail_nb() {
        return request_wail_nb;
    }

    public void setRequest_wail_nb(int request_wail_nb) {
        this.request_wail_nb = request_wail_nb;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNb() {
        return nb;
    }

    public void setNb(String nb) {
        this.nb = nb;
    }

    public users(String name, String phone, String nb) {
        this.name = name;
        this.phone = phone;
        this.nb = nb;
    }

    private Location_ Location;

    public Location_ getLocation() {
        return Location;
    }

    public void setLocation(Location_ location) {
        Location = location;
    }

    public users() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
