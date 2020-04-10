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

    URL urlForWord(String word) throws MalformedURLException {
        String urlStr = urlTemplate.replace("%s", word);
        return new URL(urlStr);
    }

    final static Dictionary BlankDictionary = new Dictionary("Blank dictionary", "", "about:blank");
}
