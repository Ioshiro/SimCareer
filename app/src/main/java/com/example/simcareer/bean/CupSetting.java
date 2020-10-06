package com.example.simcareer.bean;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CupSetting extends Cup {
    private List<RaceDate> raceDates;
    private List<RaceSetting> raceSettings;
    private String carList;
    private List<Pilot> pilotsSubscribed;

    public CupSetting(int id, String name, String logo, List<RaceDate> raceDates, List<RaceSetting> raceSettings, String carList, List<Pilot> pilotsSubscribed) {
        super(id, name, logo);
        this.raceDates = raceDates;
        this.raceSettings = raceSettings;
        this.carList = carList;
        this.pilotsSubscribed = pilotsSubscribed;
    }

    public CupSetting(JSONObject obj) throws JSONException {
        super(obj);
        this.raceDates = new ArrayList<>();
        this.raceSettings = new ArrayList<>();
        this.pilotsSubscribed = new ArrayList<>();
        JSONArray dates = obj.getJSONArray("calendario");
        Log.d("CupSetting constructor", "calendario size: "+dates.length());
        for(int i = 0; i < dates.length(); i++)
            this.raceDates.add(new RaceDate(dates.getJSONObject(i)));
        JSONArray settings = obj.getJSONArray("impostazioni-gioco");
        for(int i = 0; i < settings.length(); i++)
            this.raceSettings.add(new RaceSetting(settings.getJSONObject(i)));
        this.carList = obj.getString("lista-auto");
        JSONArray pilots = obj.getJSONArray("piloti-iscritti");
        for(int i = 0; i < pilots.length(); i++)
            this.pilotsSubscribed.add(new Pilot(pilots.getJSONObject(i)));
    }


    @Override
    public JSONObject toJSON() throws JSONException{
        JSONObject obj = super.toJSON();
        JSONArray dates = new JSONArray();
        for(int i = 0; i < this.raceDates.size(); i++)
            dates.put(raceDates.get(i).toJSON());
        obj.put("calendario", dates);
        JSONArray settings = new JSONArray();
        for(int i = 0; i < this.raceSettings.size(); i++)
            settings.put(raceSettings.get(i).toJSON());
        obj.put("impostazioni-gioco", this.raceSettings);
        obj.put("lista-auto", this.carList);
        JSONArray pilots = new JSONArray();
        for(int i = 0; i < this.pilotsSubscribed.size(); i++)
            pilots.put(pilotsSubscribed.get(i).toJSON());
        obj.put("piloti-iscritti", pilots);
        return obj;
    }

    public Bundle toBundle(){
        Bundle obj = super.toBundle();
        obj.putString("lista-auto", this.carList);
        return obj;
    }

    public List<RaceDate> getRaceDates() {
        return raceDates;
    }

    public void setRaceDates(List<RaceDate> raceDates) {
        this.raceDates = raceDates;
    }

    public List<RaceSetting> getRaceSettings() {
        return raceSettings;
    }

    public void setRaceSettings(List<RaceSetting> raceSettings) {
        this.raceSettings = raceSettings;
    }

    public String getCarList() {
        return carList;
    }

    public void setCarList(String carList) {
        this.carList = carList;
    }

    public List<Pilot> getPilotsSubscribed() {
        return pilotsSubscribed;
    }

    public void setPilotsSubscribed(List<Pilot> pilotsSubscribed) {
        this.pilotsSubscribed = pilotsSubscribed;
    }

    public int countPastDates(){
        int res = 0;
        for(RaceDate date : this.raceDates){
            if(date.isDatePast())
                res++;
        }
        return res;
    }
}
