package com.cs122b.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RecaptchaVerifyUtils {
    // private static String SECRET_KEY;

    public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public static void verify(String gRecaptchaResponse) throws Exception {

        String SECRET_KEY = null;

        try (InputStream input = RecaptchaVerifyUtils.class.getResourceAsStream("/config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            SECRET_KEY = prop.getProperty("recaptcha.secret");
            System.out.println(SECRET_KEY);

        } catch (IOException ex) {
            System.err.println("Insure that you have config file in src/resources/");
            ex.printStackTrace();
        }

        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            throw new Exception("recaptcha verification failed: gRecaptchaResponse is null or empty");
        }

        URL verifyUrl = new URL(SITE_VERIFY_URL);

        // Open Connection to URL
        HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

        // Add Request Header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Data will be sent to the server.
        String postParams = "secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;

        // Send Request
        conn.setDoOutput(true);

        // Get the output stream of Connection
        // Write data in this stream, which means to send data to Server.
        OutputStream outStream = conn.getOutputStream();
        outStream.write(postParams.getBytes());

        outStream.flush();
        outStream.close();

        // Response code return from server.
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode=" + responseCode);

        // Get the InputStream from Connection to read data sent from the server.
        InputStream inputStream = conn.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        JsonObject jsonObject = new Gson().fromJson(inputStreamReader, JsonObject.class);

        inputStreamReader.close();

        System.out.println("Response: " + jsonObject.toString());

        if (jsonObject.get("success").getAsBoolean()) {
            return;
        }

        throw new Exception("recaptcha verification failed: response is " + jsonObject.toString());
    }

}