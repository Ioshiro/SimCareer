package com.example.simcareer.bean;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Cup {
    private int id;
    private String name;
    private String logo;



    public Cup(int id, String name, String logo) {
        this.id = id;
        this.name = name;
        this.logo = logo;

    }

    public Cup(JSONObject obj) throws JSONException {
        this.id = obj.getInt("id");
        this.name = obj.getString("nome");
        this.logo = obj.getString("logo");

    }

    public JSONObject toJSON() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("nome", this.name);
        obj.put("logo", this.logo);
        return obj;
    }

    public Bundle toBundle(){
        Bundle obj = new Bundle();
        obj.putInt("id", this.id);
        obj.putString("nome", this.name);
        obj.putString("logo", this.logo);
        return obj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
