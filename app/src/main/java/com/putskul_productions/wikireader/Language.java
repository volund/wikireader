package com.putskul_productions.wikireader;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Language implements Comparable {
    public boolean enabled;
    public String label;
    public List<Site> sites;
    public List<Dictionary> dictionaries;
    public Dictionary currentDictionary;

    public Language(String pLabel) {
        enabled = false;
        label = pLabel;
        sites = new ArrayList<Site>();
        dictionaries = new ArrayList<>();
        currentDictionary = Dictionary.BlankDictionary;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Language == false) {
            return false;
        }
        Language language = (Language)obj;
        return label.equals(language.label);
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        if (obj instanceof Language == false) {
            return -1;
        }
        Language lang = (Language)obj;
        return label.compareTo(lang.label);
    }

    final static Language NoLanguage = new Language("None");
}
