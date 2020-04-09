package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StorageBackend {
    public <T> void setListValue(Context context, String key, List<T> vals) {
        Gson gson = new Gson();
        String json = gson.toJson(vals);
        setStringValue(context, key, json);
    }

    public <T> List<T> getListValue(Context context, String key, TypeToken type) {
        String json = stringValue(context, key, "");
        Gson gson = new Gson();
        //Log.e("WIKIREADER", "before parse [" + json + "]");
        //Type listType = new TypeToken<ArrayList<Language>>() {}.getType();
        List<T> list = gson.fromJson(json, type.getType());
        //Log.e("WIKIREADER", "parsed: [" + list);
        return list;
    }

    public String stringValue(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences("wikireader-preferences", context.MODE_PRIVATE);
        String val = prefs.getString(key, defaultValue);
        return val;
    }

    public void setStringValue(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("wikireader-preferences", context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
