package com.putskul_productions.wikireader;

public class Site {
    public String language;
    public String label;
    public String address;

    Site(String pLanguage, String pLabel, String pAddress) {
        language = pLanguage.trim();
        label = pLabel.trim();
        address = pAddress.trim();
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
}
