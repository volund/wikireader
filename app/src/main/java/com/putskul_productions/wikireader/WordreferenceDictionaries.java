package com.putskul_productions.wikireader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class WordreferenceDictionaries {
     String[] sourceLocales() {
         Set<String> keys = localeMap().keySet();
         return keys.toArray(new String[keys.size()]);
     }

     Map<String, String[]> localeMap() {
         Map<String, String[]> map = new HashMap<>();
         map.put("ES", new String[] {"EN", "FR", "PT", "IT", "DE", "ES"});
         map.put("FR", new String[] {"EN", "ES"});
         map.put("IT", new String[] {"EN", "ES"});
         map.put("CA", new String[] {"CA"});
         map.put("DE", new String[] {"EN", "ES"});
         map.put("NL", new String[] {"EN"});
         map.put("SV", new String[] {"EN"});
         map.put("RU", new String[] {"EN"});
         map.put("PT", new String[] {"EN", "ES"});
         map.put("PL", new String[] {"EN"});
         map.put("RO", new String[] {"EN"});
         map.put("CZ", new String[] {"EN"});
         map.put("GR", new String[] {"EN"});
         map.put("TR", new String[] {"EN"});
         map.put("ZH", new String[] {"EN"});
         map.put("JA", new String[] {"EN"});
         map.put("KO", new String[] {"EN"});
         map.put("AR", new String[] {"EN"});
         map.put("EN", new String[] {"EN", "AR", "KO", "JA", "ZH", "TR", "GR", "CZ", "RO", "PL", "PT", "RU", "SV", "NL", "DE", "CA", "IT", "FR", "ES"});
         return map;
     }

     Dictionary dictionary(String srcLocale, String dstLocale) {
        Locale srcLc = new Locale(srcLocale);
        Locale dstLc = new Locale(dstLocale);
        String srcName = srcLc.getDisplayName(dstLc);
        String dstName = dstLc.getDisplayName(dstLc);
        String name = srcName + "->" + dstName + " (Wordreference)";
        String url = "https://www.wordreference.com/" + srcLocale.toLowerCase() + dstLocale.toLowerCase() + "/%s";
        String dstLanguage = dstLc.getDisplayLanguage(dstLc);
        return new Dictionary(name, dstLanguage, url);
     }

     List<Dictionary> dictionariesForLocale(String srcLocale) {
        List<Dictionary> dictionaries = new ArrayList<>();
        if (!localeMap().containsKey(srcLocale)) {
            throw new RuntimeException("Unknown locale [" + srcLocale + "]");
        }
        String[] destLocales = localeMap().get(srcLocale);
        for (String destLocale : destLocales) {
            dictionaries.add(dictionary(srcLocale, destLocale));
        }
        return dictionaries;
     }

}
