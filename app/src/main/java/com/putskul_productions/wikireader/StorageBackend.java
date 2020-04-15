package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class StorageBackend {
    final String preferencesKey = "wikireader-preferences";

    public <T> void setListValue(Context context, String key, List<T> vals) {
        Gson gson = new Gson();
        String json = gson.toJson(vals);
        setStringValue(context, key, json);
    }

    public <T> List<T> getListValue(Context context, String key, TypeToken type) {
        String json = getStringValue(context, key, "");
        Gson gson = new Gson();
        List<T> list = gson.fromJson(json, type.getType());
        return list;
    }

    public String getStringValue(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(preferencesKey, context.MODE_PRIVATE);
        String val = prefs.getString(key, defaultValue);
        return val;
    }

    public void setStringValue(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(preferencesKey, context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
