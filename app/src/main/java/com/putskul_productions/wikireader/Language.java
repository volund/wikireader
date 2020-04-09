package com.putskul_productions.wikireader;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Language implements Comparable {
    public String label;
    public List<Site> sites;

    public Language(String pLabel) {
        label = pLabel;
        sites = new ArrayList<Site>();
    }

    public String serialize() {
        return label;
    }

    public static Language parse(String str) {
        return new Language(str);
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
}
