package com.cs122b.utils;

import java.io.FileInputStream;
import java.io.*;
import java.io.IOException;
import java.io.FileWriter; 
import java.io.File;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class MyLogger {

    private static boolean initiated = false;
    private static File loggerFile;
    private static String fileName;
    
    private static void initLogger() {
        
        fileName = "tstjlogs.log";
        try {
            loggerFile = new File(fileName);

            loggerFile.createNewFile();
    
            System.out.println(loggerFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error Creating the Log File");
        }
    }

    public static void log(String message){
        if (initiated == false) {
            initiated = true;
            initLogger();
        }

//        try {
//            logger.write(message + "\n");
//            System.out.println("writing to file");
//            logger.close();
//        } catch (IOException e) {
//            System.out.println("Error writing to the file");
//        }
        try {
            RandomAccessFile stream = new RandomAccessFile(fileName, "rw");
            FileChannel channel = stream.getChannel();

            FileLock lock = null;
            try {
                lock = channel.tryLock();
            } catch (final OverlappingFileLockException e) {
                stream.close();
                channel.close();
                System.out.println("could not get lock");
            }
            stream.seek(stream.length());
            stream.writeChars(message + "\n");
            lock.release();

            stream.close();
            channel.close();

        } catch(IOException e) {
            System.out.println("Error Writing to File");
        }
    }
}