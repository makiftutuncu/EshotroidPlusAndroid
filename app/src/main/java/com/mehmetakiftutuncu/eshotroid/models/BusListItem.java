package com.mehmetakiftutuncu.eshotroid.models;

import com.google.gson.Gson;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;

import java.io.Serializable;

public class BusListItem implements Serializable {
    public String id;
    public String start;
    public String end;
    public boolean starred;

    public static BusListItem fromJson(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, BusListItem.class);
        } catch (Exception e) {
            Log.error(BusListItem.class, "Failed to generate BusListItem from json! json: " + json, e);
            return null;
        }
    }

    public String toJson() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this, BusListItem.class);
        } catch (Exception e) {
            Log.error(this, "Failed to convert BusListItem to json! id: " + id + ", start: " + start + ", end: " + end + ", starred: " + starred, e);
            return null;
        }
    }

    @Override
    public String toString() {
        return toJson();
    }
}
