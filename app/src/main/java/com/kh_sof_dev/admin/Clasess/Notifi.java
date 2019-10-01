package com.kh_sof_dev.admin.Clasess;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Notifi {

    long type;
    String firstName;
    String secondName,body,title,token;
public JSONObject Notifi(){


    JSONObject jsonObject = new JSONObject();
    JSONObject notif = new JSONObject();
    try {
        jsonObject.put("title", title);
        jsonObject.put("body", body);

        try {
            notif.put("notification", jsonObject);
            notif.put("to",token);
    } catch (JSONException e) {
        e.printStackTrace();
    }

    } catch (JSONException e) {
        e.printStackTrace();
    }
return  notif;
}
    public Notifi() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Notifi(long type , String body, String title) {
        this.body = body;
        this.title = title;
//        this.type = type;
    }

    public Notifi(Map<String,Object> map){
        this.secondName = (String)map.get("title");
        this.secondName = (String) map.get("body");
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
