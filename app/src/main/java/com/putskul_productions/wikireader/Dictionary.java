package com.putskul_productions.wikireader;

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
