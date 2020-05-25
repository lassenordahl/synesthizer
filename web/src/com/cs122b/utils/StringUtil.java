package com.cs122b.utils;

import java.util.ArrayList;
import java.util.StringJoiner;

public class StringUtil {
    public static String formatFullTextSearch(String search) {
        String[] words = search.split(" ");

        ArrayList<String> filteredWords = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("") == false) {
                filteredWords.add("+" + words[i] + "*");
            }
        }

        StringJoiner sj = new StringJoiner(" ");
        for (String filteredWord : filteredWords) {
            sj.add(filteredWord);
        }

        String formatted = sj.toString();
        return formatted;
    }
}