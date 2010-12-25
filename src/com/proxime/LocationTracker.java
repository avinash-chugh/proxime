package com.proxime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;


public class LocationTracker extends Service {
    private LocationManager locationManager;
    private final int DOES_NOT_EXPIRE = -1;
    private static final int NOTIFY_LOCATION_PROXIMITY = 1;


    @Override
    public void onCreate() {
        log("onCreate");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //load all events and start tracking them
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Bundle extras = intent.getExtras();
        final Event event = (Event) extras.get("event");

        final String action = extras.getString("action");
        if(action != null)
        {
            RegisterForNotification(event, action);
        }
        else
        {
            boolean approaching = extras.getBoolean(LocationManager.KEY_PROXIMITY_ENTERING);
            NotifyEvent(event,approaching);
        }


        log("onStartCommand");
        return START_STICKY;
    }

    private void NotifyEvent(Event event, boolean approaching) {
//        debugNotify();
        SmsManager.getDefault().sendTextMessage("9972027222", "9902043962", event.getMessage(), null, null);
    }

    private void debugNotify() {
        Notification notification = new Notification(R.drawable.create_pressed, "Sent message", System.currentTimeMillis());
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }

    private void RegisterForNotification(final Event event, final String action) {
        if (!event.isValidForNotification()) return ;
        new Thread(new Runnable() {
            public void run() {
                if (action.equals("add")) {
                    Location location = event.getLocation();
                    Intent intent = new Intent(getApplicationContext(), LocationTracker.class);
                    PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), NOTIFY_LOCATION_PROXIMITY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    locationManager.addProximityAlert(location.getLatitude(), location.getLongitude(), location.getSpan(), DOES_NOT_EXPIRE, pendingIntent);
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void log(String message) {
//        Log.e("LocationTracker", message);
    }

}
