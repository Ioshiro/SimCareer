package com.example.simcareer.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.core.content.FileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class DbManager {

    /**
     * adds a username and password to the partial db for faster id research
     *
     * @param context of the app
     * @param user    UserBean class to be added
     * @throws JSONException if file not found
     */
    public static void addUserShort(Context context, User user) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "usersShort.json"));
        JSONObject newUser = new JSONObject();
        newUser.put("id", user.getId());
        newUser.put("username", user.getUsername());
        newUser.put("password", user.getPassword());
        obj.getJSONArray("users").put(newUser);
        saveJSONToFiles(context, obj.toString(), "usersShort.json");
    }


    /**
     * adds full user data to db given a UserBean object
     * creates empty default passiveTask array
     *
     * @param context of the app
     * @param user    Bundle class to be added
     * @throws JSONException if file not found
     */
    public static int addUser(Context context, Bundle user) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "users.json"));
        JSONArray users = obj.getJSONArray("users");
        int id = users.getJSONObject(users.length() - 1).getInt("id") + 1;
        user.putInt("id", id);
        User usr = new User(user);
        usr.setPicName(usr.getUsername() + id + ".png");
        addUserShort(context, usr);
        JSONObject newUser = usr.toJSON();
        obj.getJSONArray("users").put(newUser);
        saveJSONToFiles(context, obj.toString(), "users.json");
        return id;
    }

    /**
     * finds user ID by username and password combination
     *
     * @param context  of the app
     * @param username of user
     * @param password of user
     * @return -1 if no user was found, user ID if it is found
     * @throws JSONException if file not found
     */
    public static int getUserId(Context context, String username, String password) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "usersShort.json"));
        JSONArray usrs = obj.getJSONArray("users");
        int res = -1;
        for (int i = 0; i < usrs.length(); i++) {
            JSONObject usr = usrs.getJSONObject(i);
            String name = usr.getString("username");
            String psw = usr.getString("password");
            if (username.equals(name) && psw.equals(password)) {
                res = usr.getInt("id");
                return res;
            }
        }
        return res;
    }

    /**
     * finds complete user data by id, converting it in UserBean class
     *
     * @param context of the app
     * @param id      of the user
     * @return UserBean object, default "error" object if id is wrong, full user data if id is correct
     * @throws JSONException if file not found
     */
    public static User getFullUserById(Context context, int id) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "users.json"));
        User res = null;
        if (obj.has("users")) {
            JSONArray users = obj.getJSONArray("users");
            for (int i = 0; i < users.length(); i++) {
                JSONObject usr = users.getJSONObject(i);
                int checkId = usr.getInt("id");
                if (checkId == id) {
                    res = new User(usr);
                    return res;
                }
            }
        }
        return res;
    }

    public static boolean removeUser(Context context, int id) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "users.json"));
        JSONArray users = obj.getJSONArray("users");
        obj = new JSONObject(loadJSONFromFiles(context, "usersShort.json"));
        JSONArray usersShort = obj.getJSONArray("users");
        boolean done = false;
        boolean doneShort = false;
        for (int i = 0; i < users.length(); i++) {
            if (!done && users.getJSONObject(i).getInt("id") == id) {
                users.remove(i);
                saveJSONToFiles(context, users.toString(), "users.json");
                done = true;
            }
            if (!doneShort && usersShort.getJSONObject(i).getInt("id") == id) {
                usersShort.remove(i);
                saveJSONToFiles(context, users.toString(), "usersShort.json");
                doneShort = true;
            }
            if (done && doneShort)
                return true;
        }
        return false;
    }

    /**
     * updates the JSON db overwriting the existing user by comparing their IDs
     *
     * @param context of the app
     * @param usr     UserBean class to be updated
     * @throws JSONException if file not found
     */
    public static boolean updateFullUser(Context context, User usr) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "users.json"));
        JSONArray usrs = obj.getJSONArray("users");
        for (int i = 0; i < usrs.length(); i++) {
            if (usrs.getJSONObject(i).getInt("id") == usr.getId()) {
                updateCupSettingsPilot(context, usrs.getJSONObject(i).getString("name"), usr.getName());
                updateCupLeaderboardPilot(context, usrs.getJSONObject(i).getString("name"), usr.getName());
                usrs.put(i, usr.toJSON());
                obj.put("users", usrs);
                saveJSONToFiles(context, obj.toString(), "users.json");
                return true;
            }
        }
        return false;
    }

    /**
     * updates password of a user compared by given user id and writes it to both user db json files
     *
     * @param context of the app
     * @param usr     User class to bo updated
     * @return true if password was updated correctly, false if not
     * @throws JSONException if file not found
     */
    public static boolean updateUserPassword(Context context, User usr) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "users.json"));
        JSONArray users = obj.getJSONArray("users");
        obj = new JSONObject(loadJSONFromFiles(context, "usersShort.json"));
        JSONArray usersShort = obj.getJSONArray("users");
        boolean done = false;
        boolean doneShort = false;
        for (int i = 0; i < users.length(); i++) { //length() may be wrong, try size() or whatever
            if (!done && users.getJSONObject(i).getInt("id") == usr.getId()) {
                users.put(i, usr.toJSON());
                obj = new JSONObject();
                obj.put("users", users);
                saveJSONToFiles(context, obj.toString(), "users.json");
                done = true;
            }
            if (!doneShort && usersShort.getJSONObject(i).getInt("id") == usr.getId()) {
                usersShort.getJSONObject(i).put("password", usr.getPassword());
                obj = new JSONObject();
                obj.put("users", usersShort);
                saveJSONToFiles(context, obj.toString(), "usersShort.json");
                doneShort = true;
            }
            if (done && doneShort)
                return true;
        }
        return false;
    }


    public static List<Cup> getCups(Context context) throws JSONException {
        List<Cup> res = new ArrayList<>();
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            res.add(new Cup(cups.getJSONObject(i)));
        }
        return res;
    }

    public static Cup getCup(Context context, int id) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            if (cups.getJSONObject(i).getInt("id") == id)
                return new Cup(cups.getJSONObject(i));
        }
        return null;
    }

    public static boolean updateCupSettingsPilot(Context context, String pilotName, String newPilotName) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        boolean res = false;
        for (int i = 0; i < cups.length(); i++) {
            JSONArray pilots = cups.getJSONObject(i).getJSONArray("piloti-iscritti");
            for (int j = 0; j < pilots.length(); j++) {
                String nome = pilots.getJSONObject(j).getString("nome");
                if (nome.equals(pilotName)) {
                    pilots.getJSONObject(j).put("nome", newPilotName);
                    cups.getJSONObject(i).put("piloti-iscritti", pilots);
                    obj.put("campionati", cups);
                    saveJSONToFiles(context, obj.toString(), "campionati.json");
                    res = true;
                }
            }
        }
        return res;
    }

    public static boolean updateCupLeaderboardPilot(Context context, String pilotName, String newPilotName) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "classifiche.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        boolean res = false;
        for (int i = 0; i < cups.length(); i++) {
            JSONArray pilots = cups.getJSONObject(i).getJSONArray("classifica-piloti");
            for (int j = 0; j < pilots.length(); j++) {
                String nome = pilots.getJSONObject(j).getString("nome");
                if (nome.equals(pilotName)) {
                    pilots.getJSONObject(j).put("nome", newPilotName);
                    cups.getJSONObject(i).put("classifica-piloti", pilots);
                    obj.put("campionati", cups);
                    saveJSONToFiles(context, obj.toString(), "classifiche.json");
                    res = true;
                }
            }
        }
        return res;
    }

    public static boolean addCupPilot(Context context, Pilot pilot, int cupId) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONObject obj2 = new JSONObject(loadJSONFromFiles(context, "classifiche.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        JSONArray leads = obj2.getJSONArray("campionati");
        boolean doneCup = false;
        boolean doneLead = false;
        for (int i = 0; i < cups.length(); i++) {
            if (cups.getJSONObject(i).getInt("id") == cupId) {
                JSONArray newPilots = cups.getJSONObject(i).getJSONArray("piloti-iscritti");
                newPilots.put(pilot.toJSON());
                cups.getJSONObject(i).put("piloti-iscritti", newPilots);
                obj.put("campionati", cups);
                DbManager.saveJSONToFiles(context, obj.toString(), "campionati.json");
                doneCup = true;
            }
            if (leads.getJSONObject(i).getInt("id") == cupId) {
                JSONArray newPilots = leads.getJSONObject(i).getJSONArray("classifica-piloti");
                newPilots.put(new PilotPoints(pilot.getName(), pilot.getTeam(), pilot.getCar(), 0).toJSON());
                leads.getJSONObject(i).put("classifica-piloti", newPilots);
                obj2.put("campionati", leads);
                DbManager.saveJSONToFiles(context, obj2.toString(), "classifiche.json");
                doneLead = true;
            }
            if (doneCup && doneLead)
                return true;
        }
        return false;
    }

    public static boolean removeCupPilot(Context context, String pilot, int cupId) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        JSONObject obj2 = new JSONObject(loadJSONFromFiles(context, "classifiche.json"));
        JSONArray leads = obj2.getJSONArray("campionati");
        //Log.d("lead received: ", leads.toString());
        //Log.d("id received: ", "" + cupId);
        boolean doneCup = false;
        boolean doneLead = false;
        for (int i = 0; i < cups.length(); i++) {
            if (cups.getJSONObject(i).getInt("id") == cupId) {
                JSONArray newPilots = cups.getJSONObject(i).getJSONArray("piloti-iscritti");
                for (int j = 0; j < newPilots.length(); j++) {
                    if (newPilots.getJSONObject(j).getString("nome").equals(pilot)) {
                        newPilots.remove(j);
                        cups.getJSONObject(i).put("piloti-iscritti", newPilots);
                        obj.put("campionati", cups);
                        DbManager.saveJSONToFiles(context, obj.toString(), "campionati.json");
                        doneCup = true;
                    }

                }
            }
            if (leads.getJSONObject(i).getInt("id") == cupId) {
                JSONArray newPilots = leads.getJSONObject(i).getJSONArray("classifica-piloti");
                for (int j = 0; j < newPilots.length(); j++) {
                    if (newPilots.getJSONObject(j).getString("nome").equals(pilot)) {
                        newPilots.remove(j);
                        leads.getJSONObject(i).put("classifica-piloti", newPilots);
                        obj2.put("campionati", leads);
                        DbManager.saveJSONToFiles(context, obj2.toString(), "classifiche.json");
                        doneLead = true;
                    }
                }
            }
            if (doneCup && doneLead)
                return true;
        }
        return false;
    }

    public static boolean isPilotSubscribedToCup(Context context, String pilot, int cupId) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            if (cups.getJSONObject(i).getInt("id") == cupId) {
                JSONArray pilots = cups.getJSONObject(i).getJSONArray("piloti-iscritti");
                for (int j = 0; j < pilots.length(); j++) {
                    if (pilots.getJSONObject(j).getString("nome").equals(pilot)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<CupSetting> getSubscribedCups(Context context, String pilot) throws JSONException {
        List<CupSetting> res = new ArrayList<>();
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            if (isPilotSubscribedToCup(context, pilot, i)) {
                res.add(new CupSetting(cups.getJSONObject(i)));
            }
        }
        return res;
    }

    public static List<CupSetting> getCupsSettings(Context context) throws JSONException {
        List<CupSetting> res = new ArrayList<>();
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            res.add(new CupSetting(cups.getJSONObject(i)));
        }
        return res;
    }

    public static CupSetting getCupSetting(Context context, int id) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "campionati.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            if (cups.getJSONObject(i).getInt("id") == id)
                return new CupSetting(cups.getJSONObject(i));
        }
        return null;
    }

    public static List<CupLeaderboard> getCupsLeaderboards(Context context) throws JSONException {
        List<CupLeaderboard> res = new ArrayList<>();
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "classifiche.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            res.add(new CupLeaderboard(cups.getJSONObject(i)));
        }
        return res;
    }


    public static CupLeaderboard getCupLeaderboard(Context context, int id) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromFiles(context, "classifiche.json"));
        JSONArray cups = obj.getJSONArray("campionati");
        for (int i = 0; i < cups.length(); i++) {
            if (cups.getJSONObject(i).getInt("id") == id) {
                return new CupLeaderboard(cups.getJSONObject(i));
            }
        }
        return null;
    }

    public static List<String> getCircuits(Context context) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromAsset(context, "circuiti.json"));
        JSONArray circuits = obj.getJSONArray("circuiti");
        List<String> res = new ArrayList<>();
        for (int i = 0; i < circuits.length(); i++) {
            res.add(circuits.getString(i));
        }
        return res;
    }

    public static List<String> getCars(Context context) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromAsset(context, "auto.json"));
        JSONArray cars = obj.getJSONArray("auto");
        List<String> res = new ArrayList<>();
        for (int i = 0; i < cars.length(); i++) {
            res.add(cars.getString(i));
        }
        return res;
    }


    public static List<String> getTeams(Context context) throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromAsset(context, "teams.json"));
        JSONArray teams = obj.getJSONArray("teams");
        List<String> res = new ArrayList<>();
        for (int i = 0; i < teams.length(); i++) {
            res.add(teams.getString(i));
        }
        return res;
    }

    /**
     * read file from the internal asset folder given a filename and returns a string
     *
     * @param context  of the app
     * @param filename to load
     * @return string containing JSON db info
     */
    public static String loadJSONFromAsset(Context context, String filename) {
        String json = "";
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    public static Drawable loadDrawableFromAsset(Context context, String filename) throws IOException {
        return Drawable.createFromResourceStream(context.getResources(), new TypedValue(), context.getResources().getAssets().open(filename), null);
    }

    /**
     * read file from "public" files given a filename and returns a string
     *
     * @param context  of the app
     * @param filename to load
     * @return string containing JSON db info
     */
    public static String loadJSONFromFiles(Context context, String filename) {
        String json = "";

        try {
            File file = new File(context.getFilesDir(), filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String thisLine;
            while ((thisLine = br.readLine()) != null) {
                json = json.concat(thisLine);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

    /**
     * write to file in "public" files given a string and a filename
     *
     * @param context    of the app
     * @param JSONString used to write
     * @param filename   used to save
     */
    public static void saveJSONToFiles(Context context, String JSONString, String filename) {
        try {
            File file = new File(context.getFilesDir(), filename);
            Writer output = new BufferedWriter(new FileWriter(file));
            output.write(JSONString);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * writes all the important files(quiz questions, short userList and full userList)
     * from asset directory to "public" directory
     *
     * @param context of the app
     */
    public static void saveAllJSONToFiles(Context context) {
        String json;
        json = loadJSONFromAsset(context, "campionati.json");
        saveJSONToFiles(context, json, "campionati.json");
        json = loadJSONFromAsset(context, "classifiche.json");
        saveJSONToFiles(context, json, "classifiche.json");
        json = loadJSONFromAsset(context, "usersShort.json");
        saveJSONToFiles(context, json, "usersShort.json");
        json = loadJSONFromAsset(context, "users.json");
        saveJSONToFiles(context, json, "users.json");
    }

    public static void saveBitmapToFiles(Context context, Bitmap bitmap, String filename) throws IOException {
        File file = new File(context.getFilesDir(), filename);
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.close();
    }

    public static Bitmap loadBitmapFromFiles(Context context, String filename) {
        File image = new File(context.getFilesDir(), filename);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
    }

    public static Uri saveDrawableToFiles(Context context, int drawableId, String filename) throws IOException {
        File file = new File(context.getExternalCacheDir(), filename);
        OutputStream outputStream = null;
        outputStream = new FileOutputStream(file);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.close();
        return FileProvider.getUriForFile(context, "com.example.simcareer.fileprovider", file);
    }
}
