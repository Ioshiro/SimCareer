package com.example.simcareer.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class RaceSetting {
    private String type;
    private String value;

    public RaceSetting(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public RaceSetting(JSONObject obj) throws JSONException {
        this.type = obj.getString("tipo");
        this.value = obj.getString("valore");
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("tipo", this.type);
        obj.put("valore", this.value);
        return obj;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
