package com.example.simcareer.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Team {
    private String name;
    private String car;

    public Team(String name, String car) {
        this.name = name;
        this.car = car;
    }


    public Team(JSONObject obj) throws JSONException {
        this.name = obj.getString("team");
        this.car = obj.getString("auto");
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("team", this.name);
        obj.put("auto", this.car);
        return obj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
