package com.proxime.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import com.proxime.repositories.EventRepository;

public class LocationTracker extends Service {

    private EventRepository eventRepository;

    @Override
    public void onCreate() {
        eventRepository = new EventRepository(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getCommand(intent).Execute();
        return START_STICKY;
    }

    private Command getCommand(Intent intent) {
        final Bundle extras = intent.getExtras();
        final String action = extras.getString("action");
        if(action == null) return new NotifyProximityCommand(extras,eventRepository,getApplicationContext());
        if(action.equals("add")) return new RegisterForNotificationCommand(extras,getApplicationContext());
        throw new RuntimeException();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
