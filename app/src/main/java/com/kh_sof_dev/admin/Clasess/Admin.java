package com.kh_sof_dev.admin.Clasess;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Admin {
    private String name,email,password,uid;
    private List<permissin> Permissin=new ArrayList<>();
    public static Admin getAdmin(Context mcx) {
        SharedPreferences sp=mcx.getSharedPreferences("user_info", MODE_PRIVATE);
        Admin admin=new Admin();
        admin.name=sp.getString("name"," ");
        admin.email=sp.getString("email"," ");
        admin.password=sp.getString("password"," ");

        return admin;
    }
    public List<permissin> getPermissin() {
        return Permissin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPermissin(List<permissin> permissin) {
        Permissin = permissin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Admin() {
    }
}
