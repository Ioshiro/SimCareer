package com.example.simcareer.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Pilot {
    private String name;
    private String team;
    private String car;

    public Pilot(String name, String team, String car) {
        this.name = name;
        this.team = team;
        this.car = car;
    }

    public Pilot(String name, String car){
        this.name = name;
        this.car = car;
        this.team = "Indipendente";
    }

    public Pilot(JSONObject obj) throws JSONException {
        this.name = obj.getString("nome");
        this.team = obj.getString("team");
        this.car = obj.getString("auto");
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("nome", this.name);
        obj.put("team", this.team);
        obj.put("auto", this.car);
        return obj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }
}
