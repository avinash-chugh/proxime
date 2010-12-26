package com.proxime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import com.proxime.repositories.EventRepository;


public class LocationTracker extends Service {
    private LocationManager locationManager;
    private final int DOES_NOT_EXPIRE = -1;
    private static final int NOTIFY_LOCATION_PROXIMITY = 1;
    private EventRepository eventRepository;
    private NotificationManager notificationManager;


    @Override
    public void onCreate() {
//        log("Created");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        eventRepository = new EventRepository(this);
        //load all events and start tracking them
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Bundle extras = intent.getExtras();
        if(extras.getBoolean("log") == true) return START_STICKY;

        final String action = extras.getString("action");
        if(action != null)
        {
            RegisterForNotification((Event) extras.get("event"), action);
        }
        else
        {
            boolean approaching = extras.getBoolean(LocationManager.KEY_PROXIMITY_ENTERING);
            NotifyEvent((Long)extras.get("event_id"),approaching);
        }


//        log("Started");
        return START_STICKY;
    }

    private void NotifyEvent(long eventId, boolean approaching) {
        Event event = eventRepository.load(eventId);

        String whichWay = approaching ? "approaching" : "leaving";
        log("you are " + whichWay + " " + event.getLocation().getName());
//        SmsManager.getDefault().sendTextMessage("destination", "source", event.getMessage(), null, null);
    }

    private void RegisterForNotification(final Event event, final String action) {
        if (!event.isValidForNotification()) return ;
        new Thread(new Runnable() {
            public void run() {
                if (action.equals("add")) {
                    Location location = event.getLocation();
                    Intent intent = new Intent(getApplicationContext(), LocationTracker.class).putExtra("event_id",event.getId());
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

    public void log(String message) {
        log(getApplicationContext(), message,notificationManager);
    }

    public static void log(Context context, String message, NotificationManager notificationManager) {
        Notification notification = new Notification(R.drawable.alert,"Proxime", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getService(context,23,new Intent(context,LocationTracker.class).putExtra("log",true),0);
        notification.setLatestEventInfo(context,"Proxime",message, pendingIntent);
//        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

    }

}
