package com.example.socialapp;

import android.util.Log;

public class ServerConnectionThread implements Runnable {
    private Thread thread;
    private String threadName;

    ServerConnectionThread(String name) {
        threadName = name;
        Log.i(getClass().getSimpleName(), "create new thread " + threadName);
    }

    public void start() {
        Log.i(getClass().getSimpleName(), "start thread " + threadName);
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
