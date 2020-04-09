package com.putskul_productions.wikireader;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Storage {
    private StorageBackend backend = new StorageBackend();
    final static Storage shared = new Storage();

    public boolean isFreshInstall(Context context) {
        return backend.stringValue(context, "is-fresh-install", "true").equals("true");
    }

    public void setIsFreshInstall(Context context, boolean val) {
        backend.setStringValue(context, "is-fresh-install", val ? "true" : "false");
    }

    public void addLanguage(Context context, Language language) {
        List<Language> languages = getLanguages(context);
        languages.add(language);
        setLanguages(context, languages);
    }

    public void removeLanguage(Context context, Language language) {
        List<Language> languages = getLanguages(context);
        languages.remove(language);
        setLanguages(context, languages);
    }

    public void updateLanguage(Context context, Language language) {
        List<Language> languages = getLanguages(context);
        int index = languages.indexOf(language);
        if (index == -1) {
            Log.e("WIKIREADER", "Cannot update Language [" + language.label + "], index not found");
            return;
        }
        languages.remove(index);
        languages.add(index, language);
        setLanguages(context, languages);
    }

    public void setLanguages(Context context, List<Language> languages) {
        backend.setListValue(context, "languages", languages);
    }

    public List<Language> getLanguages(Context context) {
        List<Language> languages = backend.getListValue(context, "languages", new TypeToken<ArrayList<Language>>(){});
        Collections.sort(languages);
        return languages;
    }
}
