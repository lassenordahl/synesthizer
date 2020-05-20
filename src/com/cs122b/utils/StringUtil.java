package com.cs122b.utils;

import java.util.StringJoiner;

public class StringUtil {
    public static String formatFullTextSearch(String search) {
        String[] words = search.split(" ");

        for (int i = 0; i < words.length; i++) {
            words[i] = "+" + words[i] + "*";
        }

        StringJoiner sj = new StringJoiner(" ");
        for (String word : words) {
            sj.add(word);
        }

        String formatted = sj.toString();
        return formatted;
    }
}