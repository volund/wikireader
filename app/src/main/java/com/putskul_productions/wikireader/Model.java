/*
Copyright 2021 Amos JOSHUA

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
    
package com.putskul_productions.wikireader;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Model {
    WordreferenceDictionaries wordreference = new WordreferenceDictionaries();
    NewsWebsites newsWebsites = new NewsWebsites();

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
        App.shared.storage.setListValue(context, "languages", languages);
    }

    public List<Language> getLanguages(Context context) {
        List<Language> languages = App.shared.storage.getListValue(context, "languages", new TypeToken<ArrayList<Language>>(){});
        if (languages == null) {
            return new ArrayList<Language>();
        }
        for (Language lang : languages) {
            Collections.sort(lang.sites);
        }
        return languages;
    }

    public List<Language> enabledLanguages(Context context) {
        List<Language> languages = getLanguages(context);
        List<Language> enabled = new ArrayList<>();
        for (Language language : languages) {
            if (language.enabled) {
                enabled.add(language);
            }
        }
        return enabled;
    }

    public boolean hasEnabledLanguages(Context context) {
        return enabledLanguages(context).size() > 0;
    }

    public List<Language> defaultLanguages() {
        Map<String, Language> languageMap = new HashMap<>();
        for (String key : wordreference.sourceLocales()) {
            String adjustedKey = key.equals("GR") ? "EL" : key.equals("CZ") ? "CS" : key;
            Locale locale = new Locale(adjustedKey);//Locale(key.equals("GR") ? "EL" : key);
            Language language = new Language(locale.getDisplayLanguage(locale));
            language.dictionaries.addAll(wordreference.dictionariesForLocale(key));
            language.sites.add(new Site(key.equals("AR") ? "ويكيبيديا" : "Wikipedia", "https://" + adjustedKey.toLowerCase() + ".wikipedia.org"));
            language.sites.add(newsWebsites.websiteForLocale(key));
            languageMap.put(key, language);
        }

        Language hebrew = new Language("עברית");
        hebrew.sites.add(new Site("וויקיפידיה", "https://wikipedia.co.il"));
        hebrew.sites.add(newsWebsites.websiteForLocale("HE"));

        Language amharic = new Language("አማርኛ");
        amharic.sites.add(new Site("ወሬ፣ ዜና", "https://www.bbc.com/amharic"));
        amharic.sites.add(new Site("ውክፔዲያ", "https://am.wikipedia.org/"));


        languageMap.get("IT").sites.add(new Site("Ascoltando le cicale", "https://valerianeglia.wordpress.com/"));
        languageMap.get("FR").sites.add(new Site("En écoutant les cigales", "https://valerianeglia.wordpress.com/en-ecoutant-les-cigales/"));

        // Note: milog won't load in the app for some reason
        //hebrew.dictionaries.add(new Dictionary("עברית - עברית (מילוג)", "עברית", "https://milog.co.il/%s"));
        hebrew.dictionaries.add(new Dictionary("עברית - עברית (מורפיקס)", "עברית", "https://www.morfix.co.il/he/%s"));
        hebrew.dictionaries.add(new Dictionary("Hebrew - English (Morfix)", "English", "https://www.morfix.co.il/en/%s"));
        amharic.dictionaries.add(new Dictionary("አማርኛ - እንግሊዝኛ (Abyssinica)", "English", "https://dictionary.abyssinica.com/%s"));
        languageMap.get("EN").dictionaries.add(new Dictionary("אנגלית - עברית (מורפיקס)", "עברית", "https://www.morfix.co.il/en/%s"));
        languageMap.get("EN").dictionaries.add(new Dictionary("English - Amharic (Abyssinica)", "አማርኛ", "https://dictionary.abyssinica.com/%s"));


        List<Language> languages = new ArrayList<Language>();
        languages.add(languageMap.get("EN"));
        languages.add(languageMap.get("ES"));
        languages.add(languageMap.get("FR"));
        languages.add(languageMap.get("IT"));
        languages.add(languageMap.get("PT"));
        languages.add(hebrew);
        languages.add(languageMap.get("AR"));
        languages.add(amharic);
        languages.add(languageMap.get("CA"));
        languages.add(languageMap.get("DE"));
        languages.add(languageMap.get("NL"));
        languages.add(languageMap.get("SV"));
        languages.add(languageMap.get("RU"));
        languages.add(languageMap.get("PL"));
        languages.add(languageMap.get("RO"));
        languages.add(languageMap.get("CZ"));
        languages.add(languageMap.get("GR"));
        languages.add(languageMap.get("TR"));
        languages.add(languageMap.get("ZH"));
        languages.add(languageMap.get("JA"));
        languages.add(languageMap.get("KO"));

        return languages;
    }
}
