package com.example.diarioescolar;

public class Serial {
    private static int serial=0;
    private Serial(){}
    public static synchronized int proximo(){
        return ++serial;
    }
}
