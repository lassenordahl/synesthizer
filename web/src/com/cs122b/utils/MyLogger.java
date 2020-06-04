package com.cs122b.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MyLogger {

    private static boolean initiated = false;
    private static Logger logger = Logger.getLogger(Logger.class.getName());
    
    private static void initLogger() {
        try {
            LogManager.getLogManager().readConfiguration(new FileInputStream("/logging.properties"));
        } catch (SecurityException | IOException e1) {
            e1.printStackTrace();
        }
        logger.setLevel(Level.FINE);
        logger.addHandler(new ConsoleHandler());
        try {
            //FileHandler file name with max size and number of log files limit
            Handler fileHandler = new FileHandler("/logger.log", 6000, 5);
            logger.addHandler(fileHandler);
            logger.log(Level.CONFIG, "Config data");
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        if (initiated == false) {
            initiated = true;
            initLogger();
        }
        logger.log(Level.FINE, message);
    }
}