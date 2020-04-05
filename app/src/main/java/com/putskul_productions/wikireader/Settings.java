package com.putskul_productions.wikireader;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    static final Settings shared = new Settings();

    public String lastVisitedURL(Context context) {
        String lastURL = value(context, "last-visited-url-" + sourceLanguage(context), null);
        if (lastURL == null) {
            lastURL = defaultURLForLanguage(sourceLanguage(context));
        }
        return lastURL;
    }

    public void setLastVisitedURL(Context context, String url) {
        setValue(context, "last-visited-url-" + sourceLanguage(context), url);
    }

    private String defaultURLForLanguage(String language) {
        return "https://" + language.toLowerCase() + ".wikipedia.org/";
    }

    public String sourceLanguage(Context context) {
        return value(context, "source-language", "IT");
    }

    public void setSourceLanguage(Context context, String sourceLanguage) {
        setValue(context, "source-language", sourceLanguage);
    }

    public String dictionaryLanguage(Context context) {
        return value(context, "dictionary-language", "EN");
    }

    public void setDictionaryLanguage(Context context, String dictionaryLanguage) {
        setValue(context, "dictionary-language", dictionaryLanguage);
    }

    public String dictionaryURL(Context context) {
        String dictLang = dictionaryLanguage(context);
        String srcLang = sourceLanguage(context);
        return "http://www.wordreference.com/" + srcLang.toLowerCase() + dictLang.toLowerCase() + "/";
    }



    public final Map<String, String[]> languageMap = new HashMap<String, String[]>() {{
        put("ES", new String[] {"EN", "FR", "PT", "IT", "DE", "ES"});
        put("FR", new String[] {"EN", "ES"});
        put("IT", new String[] {"EN", "ES"});
        put("CA", new String[] {"CA"});
        put("DE", new String[] {"EN", "ES"});
        put("NL", new String[] {"EN"});
        put("SV", new String[] {"EN"});
        put("RU", new String[] {"EN"});
        put("PT", new String[] {"EN", "ES"});
        put("PL", new String[] {"EN"});
        put("RO", new String[] {"EN"});
        put("CZ", new String[] {"EN"});
        put("GR", new String[] {"EN"});
        put("TR", new String[] {"EN"});
        put("ZH", new String[] {"EN"});
        put("JA", new String[] {"EN"});
        put("KO", new String[] {"EN"});
        put("AR", new String[] {"EN"});
        put("EN", new String[] {"EN", "AR", "KO", "JA", "ZH", "TR", "GR", "CZ", "RO", "PL", "PT", "RU", "SV", "NL", "DE", "CA", "IT", "FR", "ES"});
    }};

    public String[] sourceLanguages() {
        return languageMap.keySet().toArray(new String[languageMap.size()]);
    }

    public void setExpandedSections(Context context, String expandedSections) {
        setValue(context, "expanded-sections", expandedSections);
    }

    public String getExpandedSections(Context context) {
        return value(context, "expanded-sections", "[]");
    }

    private String value(Context context, String key, String defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences("wikireader-preferences", context.MODE_PRIVATE);
        String val = prefs.getString(key, defaultValue);
        return val;
    }

    private void setValue(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("wikireader-preferences", context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
