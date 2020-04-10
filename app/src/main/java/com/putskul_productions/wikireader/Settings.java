package com.putskul_productions.wikireader;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Settings {
    static final Settings shared = new Settings();
    private StorageBackend storage = new StorageBackend();
    WordpressDictionaries wordpress = new WordpressDictionaries();

    public String lastVisitedURL(Context context) {
        String lastURL = storage.stringValue(context, "last-visited-url-" + sourceLanguage(context), null);
        if (lastURL == null) {
            lastURL = defaultURLForLanguage(sourceLanguage(context));
        }
        return lastURL;
    }

    public void setLastVisitedURL(Context context, String url) {
        storage.setStringValue(context, "last-visited-url-" + sourceLanguage(context), url);
    }

    private String defaultURLForLanguage(String language) {
        return "https://" + language.toLowerCase() + ".wikipedia.org/";
    }

    public String sourceLanguage(Context context) {
        return storage.stringValue(context, "source-language", "IT");
    }

    public void setSourceLanguage(Context context, String sourceLanguage) {
        storage.setStringValue(context, "source-language", sourceLanguage);
    }

    public String dictionaryLanguage(Context context) {
        return storage.stringValue(context, "dictionary-language", "EN");
    }

    public void setDictionaryLanguage(Context context, String dictionaryLanguage) {
        storage.setStringValue(context, "dictionary-language", dictionaryLanguage);
    }

    public String dictionaryURL(Context context) {
        String dictLang = dictionaryLanguage(context);
        String srcLang = sourceLanguage(context);
        if (srcLang.equals("Français")) {
            return "http://www.wordreference.com/fren/";
        }
        else if (srcLang.equals("Italiano")) {
            return "http://wordreference.com/iten/";
        }
        else if (srcLang.equals("Português")) {
            return "http://wordreference.com/pten/";
        }
        return "http://www.wordreference.com/" + srcLang.toLowerCase() + dictLang.toLowerCase() + "/";
    }



    public final Map<String, String[]> languageMap = new HashMap<String, String[]>() {{
        put("ES", new String[] {"EN", "FR", "PT", "IT", "DE", "ES"});
        put("Français", new String[] {"EN", "ES"});
        put("Italiano", new String[] {"EN", "ES"});
        put("CA", new String[] {"CA"});
        put("DE", new String[] {"EN", "ES"});
        put("NL", new String[] {"EN"});
        put("SV", new String[] {"EN"});
        put("RU", new String[] {"EN"});
        put("Português", new String[] {"EN", "ES"});
        put("PL", new String[] {"EN"});
        put("RO", new String[] {"EN"});
        put("CZ", new String[] {"EN"});
        put("GR", new String[] {"EN"});
        put("TR", new String[] {"EN"});
        put("ZH", new String[] {"EN"});
        put("JA", new String[] {"EN"});
        put("KO", new String[] {"EN"});
        put("AR", new String[] {"EN"});
        put("English", new String[] {"EN", "AR", "KO", "JA", "ZH", "TR", "GR", "CZ", "RO", "PL", "PT", "RU", "SV", "NL", "DE", "CA", "IT", "FR", "ES"});
    }};

    public String[] sourceLanguages() {
        return languageMap.keySet().toArray(new String[languageMap.size()]);
    }

    public void setExpandedSections(Context context, String expandedSections) {
        storage.setStringValue(context, "expanded-sections", expandedSections);
    }

    public String getExpandedSections(Context context) {
        return storage.stringValue(context, "expanded-sections", "[]");
    }

    public List<Language> defaultLanguages() {/*
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
        french.sites.add(new Site( "Wikipedia", "https://fr.wikipedia.org"));
        portuguese.sites.add(new Site("Notícias","https://news.google.com.br"));
        portuguese.sites.add(new Site( "Wikipedia","https://pt.wikipedia.org"));
        english.sites.add(new Site("Wikipedia","https://wikipedia.org"));
        hebrew.sites.add(new Site("וויקיפידיה", "https://wikipedia.co.il"));

        List<Language> languages = new ArrayList<>();
        languages.add(english);
        languages.add(french);
        languages.add(italian);
        languages.add(portuguese);
        languages.add(hebrew);

        return languages;
    }

    public List<Language> getLanguages() {*/

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

        /*
        italian.dictionaries.addAll(wordpress.dictionariesForLocale("IT"));
        french.dictionaries.addAll(wordpress.dictionariesForLocale("FR"));
        portuguese.dictionaries.addAll(wordpress.dictionariesForLocale("PT"));

        english.dictionaries.addAll(wordpress.dictionariesForLocale("EN"));
        english.dictionaries.add(new Dictionary("Morphix (EN-HE)", "עברית", "https://www.morphix.co.il/en/%s"));

        hebrew.dictionaries.add(new Dictionary("Milog (HE-HE)", "עברית", "https://milog.co.il/%s"));
        hebrew.dictionaries.add(new Dictionary("Morphix (HE-EN)", "English", "https://www.morfix.co.il/en/%s"));
*/
        List<Language> languages = new ArrayList<>();
        languages.add(english);
        languages.add(french);
        languages.add(italian);
        languages.add(portuguese);
        languages.add(hebrew);

        return languages;
    }
}
