package com.putskul_productions.wikireader;

public class StringUtils {

    public static String capitalize(String str) {
        if ((str == null) || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}