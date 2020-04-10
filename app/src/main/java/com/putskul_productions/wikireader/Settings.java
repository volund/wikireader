package com.putskul_productions.wikireader;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {
    static final Settings shared = new Settings();
    private StorageBackend storage = new StorageBackend();
    WordreferenceDictionaries wordpress = new WordreferenceDictionaries();

    public String lastVisitedURL(Context context) {
        /*
        String lastURL = storage.stringValue(context, "last-visited-url-" + sourceLanguage(context), null);
        if (lastURL == null) {
            lastURL = defaultURLForLanguage(sourceLanguage(context));
        }
        return lastURL;*/
        return null;
    }

    public void setLastVisitedURL(Context context, String url) {
        //storage.setStringValue(context, "last-visited-url-" + sourceLanguage(context), url);
    }

    private String defaultURLForLanguage(String language) {
        return "https://" + language.toLowerCase() + ".wikipedia.org/";
    }

    public Language getCurrentLanguage(Context context) {
        String currentStr = storage.stringValue(context, "current-language", "");
        List<Language> languages = Storage.shared.getLanguages(context);
        for (Language language : languages) {
            if (language.label.equals(currentStr)) {
                return language;
            }
        }
        return languages.size() > 0 ? languages.get(0) : Language.NoLanguage;
    }

    public void setCurrentLanguage(Context context, Language language) {
        storage.setStringValue(context, "current-language", language.label);
    }


    public void setExpandedSections(Context context, String expandedSections) {
        storage.setStringValue(context, "expanded-sections", expandedSections);
    }

    public String getExpandedSections(Context context) {
        return storage.stringValue(context, "expanded-sections", "[]");
    }

    public List<Language> defaultLanguages() {

        Language french = new Language("Français");
        Language italian = new Language("Italiano");
        Language portuguese = new Language("Português");
        Language english = new Language("English");
        Language hebrew = new Language("עברית");

        italian.sites.add(new Site("Ascoltando le cicale", "https://valerianeglia.wordpress.com/"));
        italian.sites.add(new Site("Notizie", "https://news.google.it"));
        italian.sites.add(new Site("Wikipedia", "https://www.wikipedia.it"));
        french.sites.add(new Site("En écoutant les cigales", "https://valerianeglia.wordpress.com/en-ecoutant-les-cigales/"));
        french.sites.add(new Site("Infos", "https://news.google.fr"));
        french.sites.add(new Site("Wikipedia", "https://fr.wikipedia.org"));
        portuguese.sites.add(new Site("Notícias","https://news.google.com.br"));
        portuguese.sites.add(new Site("Wikipedia","https://pt.wikipedia.org"));
        english.sites.add(new Site("Wikipedia","https://wikipedia.org"));
        hebrew.sites.add(new Site("וויקיפידיה", "https://wikipedia.co.il"));


        italian.dictionaries.addAll(wordpress.dictionariesForLocale("IT"));
        french.dictionaries.addAll(wordpress.dictionariesForLocale("FR"));
        portuguese.dictionaries.addAll(wordpress.dictionariesForLocale("PT"));

        english.dictionaries.addAll(wordpress.dictionariesForLocale("EN"));
        english.dictionaries.add(new Dictionary("Morfix (EN-HE)", "עברית", "https://www.morfix.co.il/en/%s"));

        hebrew.dictionaries.add(new Dictionary("Milog (HE-HE)", "עברית", "https://milog.co.il/%s"));
        hebrew.dictionaries.add(new Dictionary("Morfix (HE-EN)", "English", "https://www.morfix.co.il/en/%s"));

        List<Language> languages = new ArrayList<>();
        languages.add(english);
        languages.add(french);
        languages.add(italian);
        languages.add(portuguese);
        languages.add(hebrew);

        return languages;
    }
}
