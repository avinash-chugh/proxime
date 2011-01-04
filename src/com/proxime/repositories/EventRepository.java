package com.proxime.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.proxime.entities.Contact;
import com.proxime.entities.Event;
import com.proxime.entities.Location;

import java.util.ArrayList;
import java.util.List;

public class EventRepository implements ColumnNames
{

    private CustomDBHelper dbHelper;
    private ContactRepository contactRepository;
    private LocationRepository locationRepository;

    public EventRepository(Context context) {

        dbHelper = new CustomDBHelper(context);
        contactRepository = new ContactRepository(context.getContentResolver());
        locationRepository = new LocationRepository(context);
    }

    public void save(Event event) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, event.getName());
        values.put(MESSAGE, event.getMessage());
        if(event.hasLocation())values.put(LOCATION_ID, event.getLocation().getId());
        if(event.hasContact()) values.put(CONTACT_ID, event.getContact().getUri().toString());
        long id = db.insertOrThrow(CustomDBHelper.EVENTS_TABLE, null, values);
        db.close();
        event.setId(id);
    }

    public List<Event> loadAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CustomDBHelper.EVENTS_TABLE, new String[]{ID}, null, null, null, null, null);

        List<Event> result = new ArrayList<Event>();
        List<Long> eventIds = new ArrayList<Long>();
        while(cursor.moveToNext())
        {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(ID));
            eventIds.add(id);
        }
        cursor.close();
        db.close();
        for(long id : eventIds) result.add(load(id));
        return result;
    }

    public Event load(long id) {
        Event result = new Event();
        result.setId(id);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {NAME, MESSAGE, CONTACT_ID, LOCATION_ID};
        String selection = ID + " = ?";
        String[] selectionArgs = {new Long(id).toString()};
        Cursor cursor = db.query(CustomDBHelper.EVENTS_TABLE, columns, selection, selectionArgs, null, null, null, null);
        cursor.moveToNext();
        result.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        result.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE)));
        String contactUri = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_ID));
        if (contactUri != null)
        {
            Contact contact = contactRepository.getContact(Uri.parse(contactUri));
            result.setContact(contact);
        }
        long locationId = cursor.getLong(cursor.getColumnIndexOrThrow(LOCATION_ID));
        if (locationId > 0)
        {
            Location location = locationRepository.load(locationId);
            result.setLocation(location);
        }
        cursor.close();
        db.close();

        return result;
    }
}
