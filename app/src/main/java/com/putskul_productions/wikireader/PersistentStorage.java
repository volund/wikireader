package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersistentStorage {

    public void setArrayValue(Context context, String key, List<String> vals) {
        Set<String> setVal = new HashSet<String>();
        setVal.addAll(vals);
        SharedPreferences.Editor editor = context.getSharedPreferences("wikireader-preferences", context.MODE_PRIVATE).edit();
        editor.putStringSet(key, setVal);
        editor.apply();
    }

    public List<String> arrayValue(Context context, String key, List<String> defaultValues) {
        Set<String> defValues = new HashSet<String>();
        if (defaultValues != null) {
            defValues.addAll(defaultValues);
        }
        SharedPreferences prefs = context.getSharedPreferences("wikireader-preferences", context.MODE_PRIVATE);
        Set<String> valueSet = prefs.getStringSet(key, defValues);
        List<String> valueList = new ArrayList<String>();
        valueList.addAll(valueSet);
        return valueList;
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
