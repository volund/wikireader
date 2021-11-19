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

import java.util.HashMap;
import java.util.Map;

public class NewsWebsites {

    Site websiteForLocale(String localeStr) {
        Map<String, Site> map = new HashMap<>();
        map.put("ES", new Site("Noticias", "https://elpais.com"));
        map.put("FR", new Site("Actualités", "https://news.google.fr/?hl=fr&gl=FR"));
        map.put("IT", new Site("Notizie", "https://news.google.it/?hl=it&gl=IT"));
        map.put("CA", new Site("Notícies", "https://www.ccma.cat/"));
        map.put("DE", new Site("Nachrichten", "https://news.google.de/?hl=de&gl=DE"));
        map.put("NL", new Site("Nieuws", "https://news.google.nl/?hl=nl&gl=NL"));
        map.put("SV", new Site("Nyheter", "https://news.google.se?hl=sv&gl=SE"));
        map.put("RU", new Site("Новости", "https://news.google.ru?hl=ru&gl=RU"));
        map.put("PT", new Site("Notícias", "https://news.google.com/?hl=pt-PT&gl=PT"));
        map.put("PL", new Site("Aktualności", "https://news.google.pl?hl=pl&gl=PL"));
        map.put("RO", new Site("Știri", "https://news.google.ro?hl=ro&gl=RO"));
        map.put("CZ", new Site("Zprávy", "https://news.google.cz?hl=cs&gl=CZ"));
        map.put("GR", new Site("Νέα", "https://news.google.gr?hl=el&gl=GR"));
        map.put("TR", new Site("Haberler", "https://news.google.com.tr?hl=tr&gl=TR"));
        map.put("ZH", new Site("Nyheter", "https://news.google.com/?hl=zh-CN&gl=CN"));
        map.put("JA", new Site("新闻", "https://news.google.co.jp?hl=ja&gl=JP"));
        map.put("KO", new Site("소식", "https://news.google.co.kr?hl=ko&gl=KR"));
        map.put("AR", new Site("أخبار", "https://news.google.com/?hl=ar&gl=EG"));
        map.put("EN", new Site("News", "https://news.google.com"));
        map.put("HE", new Site("חדשות", "https://news.google.com/?hl=he-IL&gl=IL"));

        return map.get(localeStr);
    }
}
