package com.example.projectVT.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Jay on 4/14/14.
 */
public class mainService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){

        return null;
    }
}
