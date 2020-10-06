package com.example.simcareer.bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CupLeaderboard extends Cup{
    private List<PilotPoints> pilotsPoints;
    private List<TeamPoints> teamsPoints;

    public CupLeaderboard(int id, String name, String logo, List<PilotPoints> pilotsPoints, List<TeamPoints> teamsPoints) {
        super(id, name, logo);
        this.pilotsPoints = pilotsPoints;
        this.teamsPoints = teamsPoints;
    }

    public CupLeaderboard(JSONObject obj) throws JSONException {
        super(obj);
        JSONArray pilots = obj.getJSONArray("classifica-piloti");
        this.pilotsPoints = new ArrayList<>();
        this.teamsPoints = new ArrayList<>();
        for(int i = 0; i < pilots.length(); i++)
            this.pilotsPoints.add(new PilotPoints(pilots.getJSONObject(i)));
        JSONArray teams = obj.getJSONArray("classifica-team");
        for(int i = 0; i < teams.length(); i++)
            this.teamsPoints.add(new TeamPoints(teams.getJSONObject(i)));
    }

    public List<PilotPoints> getPilotsPoints() {
        return pilotsPoints;
    }

    public void setPilotsPoints(List<PilotPoints> pilotsPoints) {
        this.pilotsPoints = pilotsPoints;
    }

    public List<TeamPoints> getTeamsPoints() {
        return teamsPoints;
    }

    public void setTeamsPoints(List<TeamPoints> teamsPoints) {
        this.teamsPoints = teamsPoints;
    }

    public int getPilotPoints(String name){
        for(int i = 0; i < pilotsPoints.size(); i++){
            if(pilotsPoints.get(i).getName().equals(name))
                return pilotsPoints.get(i).getPoints();
        }
        return -1;
    }
}
