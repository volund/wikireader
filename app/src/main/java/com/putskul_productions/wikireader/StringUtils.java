package com.putskul_productions.wikireader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;

public class StringUtils {

    public static String capitalize(String str) {
        if ((str == null) || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static List<String> splitToList(String str, String delimeter) {
        String[] arrValues = str.split(delimeter);
        return new ArrayList<String>(Arrays.asList(arrValues));
    }

    public static String[] splitToArrayAndAdd(String str, String delimiter, String addition) {
        List<String> list = splitToList(str, delimiter);
        list.add(addition);
        return list.toArray(new String[list.size()]);
    }

    public static String extractTextFromHtml(String html) {
        return Jsoup.parse(html).text();
    }
}
