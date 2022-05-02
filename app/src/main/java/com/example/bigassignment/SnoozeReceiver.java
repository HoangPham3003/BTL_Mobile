package com.example.bigassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SnoozeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase("com.akash.SnoozeReceiver")) {
            System.out.println("Hello from snooze receiver");
            MusicControl.getInstance(context).stopMusic();
        }
    }
}
