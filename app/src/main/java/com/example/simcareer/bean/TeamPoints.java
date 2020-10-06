package com.example.simcareer.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamPoints extends Team {
    private int points;


    public TeamPoints(String name, String car, int points) {
        super(name, car);
        this.points = points;
    }

    public TeamPoints(JSONObject obj) throws JSONException {
        super(obj);
        this.points = obj.getInt("punti");
    }

    @Override
    public JSONObject toJSON() throws JSONException{
        JSONObject obj = super.toJSON();
        obj.put("punti", this.points);
        return obj;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
