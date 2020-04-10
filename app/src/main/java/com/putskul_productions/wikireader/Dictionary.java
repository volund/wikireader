package com.putskul_productions.wikireader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class Dictionary {
    String urlTemplate;
    String name;
    String destinationLanguage;

    Dictionary(String pName, String pDestinationLanguage, String pUrlTemplate) {
        name = pName;
        destinationLanguage = pDestinationLanguage;
        urlTemplate = pUrlTemplate;
    }

    String urlForWord(String word) {
        return urlTemplate.replace("%s", word);
    }

    final static Dictionary BlankDictionary = new Dictionary("None", "", "about:blank");
}
