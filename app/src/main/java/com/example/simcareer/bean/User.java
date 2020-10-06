package com.example.simcareer.bean;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private int id;
    private String username;
    private String name;
    private String password;
    private String email;
    private String dateOfBirth;
    private String localAddress;
    private int pilotNumber;
    private String favoriteCircuit;
    private String hatedCircuit;
    private String favoriteCar;
    private String picName;

    public User(int id, String username, String name, String password, String email, String dateOfBirth, String localAdress, int pilotNumber, String favoriteCircuit, String hatedCircuit, String favoriteCar) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.localAddress = localAdress;
        this.pilotNumber = pilotNumber;
        this.favoriteCircuit = favoriteCircuit;
        this.hatedCircuit = hatedCircuit;
        this.favoriteCar = favoriteCar;
    }

    public User(JSONObject obj) throws JSONException{
        this.id = obj.getInt("id");
        this.username = obj.getString("username");
        this.name = obj.getString("name");
        this.password = obj.getString("password");
        this.email = obj.getString("email");
        this.dateOfBirth = obj.getString("dateOfBirth");
        this.localAddress = obj.getString("localAddress");
        this.pilotNumber = obj.getInt("pilotNumber");
        this.favoriteCircuit = obj.getString("favoriteCircuit");
        this.hatedCircuit = obj.getString("hatedCircuit");
        this.favoriteCar = obj.getString("favoriteCar");
        this.picName = obj.getString("picName");

    }

    public User(Bundle obj){
        this.id = obj.getInt("id");
        this.username = obj.getString("username");
        this.name = obj.getString("name");
        this.password = obj.getString("password");
        this.email = obj.getString("email");
        this.dateOfBirth = obj.getString("dateOfBirth");
        this.localAddress = obj.getString("localAddress");
        this.pilotNumber = obj.getInt("pilotNumber");
        this.favoriteCircuit = obj.getString("favoriteCircuit");
        this.hatedCircuit = obj.getString("hatedCircuit");
        this.favoriteCar = obj.getString("favoriteCar");
        if(obj.containsKey("picName")){
            this.picName = obj.getString("picName");
        }
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("id", this.id);
        obj.put("username", this.username);
        obj.put("name", this.name);
        obj.put("password", this.password);
        obj.put("email", this.email);
        obj.put("dateOfBirth", this.dateOfBirth);
        obj.put("localAddress", this.localAddress);
        obj.put("pilotNumber", this.pilotNumber);
        obj.put("favoriteCircuit", this.favoriteCircuit);
        obj.put("hatedCircuit", this.hatedCircuit);
        obj.put("favoriteCar", this.favoriteCar);
        obj.put("picName", this.picName);
        return obj;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public int getPilotNumber() {
        return pilotNumber;
    }

    public void setPilotNumber(int pilotNumber) {
        this.pilotNumber = pilotNumber;
    }

    public String getFavoriteCircuit() {
        return favoriteCircuit;
    }

    public void setFavoriteCircuit(String favoriteCircuit) {
        this.favoriteCircuit = favoriteCircuit;
    }

    public String getHatedCircuit() {
        return hatedCircuit;
    }

    public void setHatedCircuit(String hatedCircuit) {
        this.hatedCircuit = hatedCircuit;
    }

    public String getFavoriteCar() {
        return favoriteCar;
    }

    public void setFavoriteCar(String favoriteCar) {
        this.favoriteCar = favoriteCar;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }
}

