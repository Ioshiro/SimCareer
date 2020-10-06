package com.example.simcareer.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RaceDate {
    private int seq;
    private String date;
    private String circuit;

    public RaceDate(int seq, String date, String circuit) {
        this.seq = seq;
        this.date = date;
        this.circuit = circuit;
    }

    public RaceDate(JSONObject obj) throws JSONException {
        this.seq = obj.getInt("seq");
        this.date = obj.getString("data");
        this.circuit = obj.getString("circuito");
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("seq", this.seq);
        obj.put("data", this.date);
        obj.put("circuito", this.circuit);
        return obj;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public boolean isDatePast(){
        Calendar calendar = Calendar.getInstance();
        String[] splitDate = this.date.split("/");
        int[] splitNumbers = {0 ,0 ,0};
        splitNumbers[0] = Integer.parseInt(splitDate[0]);
        splitNumbers[1] = Integer.parseInt(splitDate[1]);
        splitNumbers[2] = Integer.parseInt(splitDate[2]);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (year > splitNumbers[2]) {
            return true;
        }else if(year == splitNumbers[2]){
            if(month > splitNumbers[1])
                return true;
            else if(month == splitNumbers[1]) {
                if (day > splitNumbers[0])
                    return true;
                else
                    return false;
            }
            else
                return false;
        }
        else
            return false;


    }
}
