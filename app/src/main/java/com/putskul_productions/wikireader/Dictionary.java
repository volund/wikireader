package com.putskul_productions.wikireader;

import android.util.Log;

import java.net.URLEncoder;

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
        String url = urlTemplate.replace("%s", word); // URLEncoder.encode(word));
        Log.e("WIKIREADER", "DBG returning url [" + url + "]");
        return url;
    }

    final static Dictionary BlankDictionary = new Dictionary("None", "", "about:blank");
}
