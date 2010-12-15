package com.proxime;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class LocationTracker extends Service
{
    private final LocalBinder localBinder = new LocalBinder();
    private NotificationManager notificationManager;

    public class LocalBinder extends Binder {
        LocationTracker getService() {
            return LocationTracker.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public void onCreate() {
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Service Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "LocationTracker Service Stopped", Toast.LENGTH_SHORT).show();
    }

    private void showNotification() {
        log("Started Service");
    }

    private void log(String message) {
        Log.i("LocationTracker", message);
    }


}
