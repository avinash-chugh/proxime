package com.proxime.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import com.proxime.R;
import com.proxime.entities.Event;
import com.proxime.repositories.EventRepository;

public class NotifyProximityCommand implements Command {
    private Bundle extras;
    private EventRepository eventRepository;
    private Context context;
    private NotificationManager notificationManager;

    public NotifyProximityCommand(Bundle extras, EventRepository eventRepository, Context context) {
        this.extras = extras;
        this.eventRepository = eventRepository;
        this.context = context;
        this.notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void NotifyEvent(long eventId, boolean approaching) {
        if(!approaching) return;
        Event event = eventRepository.load(eventId);
        if(event.isNotifySelf()){
            NotifySelf(event);
        }
        else
        {
            SendSMS(event);
        }

    }

    private void NotifySelf(Event event) {
       log(event.getMessage());
    }

    private void SendSMS(Event event) {
        SmsManager.getDefault().sendTextMessage(event.getContact().getPhoneNumber(), null,event.getMessage(),null,null);
        log(event.getContact().getName() + " is messaged that " + event.getMessage());
    }

    public void log(String message) {
        Notification notification = new Notification(R.drawable.alert, "Proxime", System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getService(context, 23, new Intent(context, LocationTracker.class).putExtra("action", "log"), 0);
        notification.setLatestEventInfo(context, "Proxime", message, pendingIntent);
        notification.defaults |= Notification.DEFAULT_ALL;
        notificationManager.notify(1, notification);
    }

    public void Execute() {
        boolean approaching = extras.getBoolean(LocationManager.KEY_PROXIMITY_ENTERING);
        NotifyEvent((Long) extras.get("event_id"), approaching);
    }
}
