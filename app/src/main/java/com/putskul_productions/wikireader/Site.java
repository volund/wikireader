package com.putskul_productions.wikireader;

import android.support.annotation.NonNull;

public class Site implements Comparable {
    public String language;
    public String label;
    public String address;

    Site(String pLanguage, String pLabel, String pAddress) {
        language = pLanguage.trim();
        label = pLabel.trim();
        address = pAddress.trim();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Site == false) {
            return false;
        }
        Site site = (Site)obj;
        return language.equals(site.language) && label.equals(site.label) && address.equals(site.address);
    }

    String serialize() {
        return language + "|" + label + "|" + address;
    }

    static Site parse(String serialized) {
        String[] components = serialized.split("\\|");
        if (components.length < 3) {
            return null;
        }
        return new Site(components[0], components[1], components[2]);
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        if (obj instanceof Site == false) {
            return -1;
        }
        Site site = (Site)obj;
        return site.label.compareTo(label);
    }
}
