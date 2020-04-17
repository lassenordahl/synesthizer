package com.cs122b.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;

public class JsonParse {
    public static JsonObject toJson(BufferedReader reader) {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            System.out.println("idk");
        }

        return new JsonParser().parse(jb.toString()).getAsJsonObject();
    }
}
