package com.proxime.services;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import com.proxime.entities.Event;
import com.proxime.entities.Location;

public class RegisterForNotificationCommand implements Command {
    private static final int NOTIFY_LOCATION_PROXIMITY = 1;
    private static final int DOES_NOT_EXPIRE = -1;

    private Event event;
    private Context context;
    private LocationManager locationManager;

    public RegisterForNotificationCommand(Bundle extras, Context context) {
        this.context = context;
        this.locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        event = (Event)extras.get("event");
    }

    public void Execute() {
        if (!event.isValidForNotification()) return ;
        PendingIntent pendingIntent = createPendingEvent();
        addProximityAlert(pendingIntent);
    }

    private void addProximityAlert(PendingIntent pendingIntent) {
        Location location = event.getLocation();
        locationManager.addProximityAlert(location.getLatitude(), location.getLongitude(), location.getSpan(), DOES_NOT_EXPIRE, pendingIntent);
    }

    private PendingIntent createPendingEvent() {
        Intent intent = new Intent(context, LocationTracker.class);
        return PendingIntent.getService(context, NOTIFY_LOCATION_PROXIMITY, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
