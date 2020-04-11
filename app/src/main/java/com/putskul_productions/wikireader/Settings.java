package com.putskul_productions.wikireader;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    static final Settings shared = new Settings();
    private StorageBackend storage = new StorageBackend();
    WordreferenceDictionaries wordreference = new WordreferenceDictionaries();

    private String lastVisitedKey(Language language, Site site) {
        return language.label + "-" + site.label;
    }

    public String lastVisitedURL(Context context, Language language, Site site) {
        String key = lastVisitedKey(language, site);
        return storage.stringValue(context, key, site.address);
    }

    public void setLastVisitedURL(Context context, Language language, Site site, String url) {
        String key = lastVisitedKey(language, site);
        storage.setStringValue(context, key, url);
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

    public Site getCurrentSite(Context context) {
        String currentStr = storage.stringValue(context, "current-site", "");
        Language currentLanguage = getCurrentLanguage(context);
        for (Site site : currentLanguage.sites) {
            if (site.address.equals(currentStr)) {
                return site;
            }
        }
        return currentLanguage.sites.size() > 0 ? currentLanguage.sites.get(0) : Site.BlankSite;
    }

    public void setCurrentSite(Context context, Site site) {
        storage.setStringValue(context, "current-site", site.address);
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
        english.sites.add(new Site("Wikipedia","https://en.wikipedia.org"));
        hebrew.sites.add(new Site("וויקיפידיה", "https://wikipedia.co.il"));

        italian.dictionaries.addAll(wordreference.dictionariesForLocale("IT"));
        french.dictionaries.addAll(wordreference.dictionariesForLocale("FR"));
        portuguese.dictionaries.addAll(wordreference.dictionariesForLocale("PT"));

        english.dictionaries.add(new Dictionary("אנגלית - עברית (מורפיקס)", "עברית", "https://www.morfix.co.il/en/%s"));
        english.dictionaries.addAll(wordreference.dictionariesForLocale("EN"));

        hebrew.dictionaries.add(new Dictionary("עברית - עברית (מילוג)", "עברית", "https://milog.co.il/%s"));
        hebrew.dictionaries.add(new Dictionary("Hebrew - English (Morfix)", "English", "https://www.morfix.co.il/en/%s"));

        List<Language> languages = new ArrayList<>();
        languages.add(english);
        languages.add(french);
        languages.add(italian);
        languages.add(portuguese);
        languages.add(hebrew);

        return languages;
    }
}
