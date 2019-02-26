package com.example.rrks.myapplication.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public class MyUtils {
    public static void close(Closeable closeable){
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
