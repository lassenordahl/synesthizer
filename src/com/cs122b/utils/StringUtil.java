package com.cs122b.utils;

public class StringUtil {
    public static String formatFullTextSearch(String search) {
        String formatted = search.replace(" ", "* ") + "*";
        return formatted;
    }
}