/*
Copyright 2021 Amos JOSHUA

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
    
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
