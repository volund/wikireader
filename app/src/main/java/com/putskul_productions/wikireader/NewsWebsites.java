package com.putskul_productions.wikireader;

import java.util.HashMap;
import java.util.Map;

public class NewsWebsites {

    Site websiteForLocale(String localeStr) {
        Map<String, Site> map = new HashMap<>();
        map.put("ES", new Site("Noticias", "https://elpais.com"));
        map.put("FR", new Site("Actualités", "https://news.google.fr"));
        map.put("IT", new Site("Notizie", "https://news.google.it"));
        map.put("CA", new Site("Notícies", "https://www.ccma.cat/"));
        map.put("DE", new Site("Nachrichten", "https://news.google.de"));
        map.put("NL", new Site("Nieuws", "https://news.google.nl"));
        map.put("SV", new Site("Nyheter", "https://news.google.se"));
        map.put("RU", new Site("Новости", "https://news.google.ru"));
        map.put("PT", new Site("Notícias", "https://news.google.pt"));
        map.put("PL", new Site("Aktualności", "https://news.google.pl"));
        map.put("RO", new Site("Știri", "https://news.google.ro"));
        map.put("CZ", new Site("Zprávy", "https://news.google.cz"));
        map.put("GR", new Site("Νέα", "https://news.google.gr"));
        map.put("TR", new Site("Haberler", "https://news.google.com.tr"));
        map.put("ZH", new Site("Nyheter", "https://news.google.com/?hl=zh-CN&gl=CN"));
        map.put("JA", new Site("新闻", "https://news.google.co.jp"));
        map.put("KO", new Site("소식", "https://news.google.co.kr"));
        map.put("AR", new Site("أخبار", "https://news.google.com/?hl=ar&gl=EG"));
        map.put("EN", new Site("News", "https://news.google.com"));
        map.put("HE", new Site("חדשות", "https://news.google.co.il"));

        return map.get(localeStr);
    }
}
