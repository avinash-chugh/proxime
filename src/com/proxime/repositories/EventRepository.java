package com.proxime.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.proxime.Contact;
import com.proxime.ContactRepository;
import com.proxime.Event;
import com.proxime.Location;

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
        Cursor cursor = db.query(CustomDBHelper.EVENTS_TABLE, new String[]{ID, NAME}, null, null, null, null, null);
        List<Event> result = new ArrayList<Event>();
        while(cursor.moveToNext())
        {
            Event event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
            event.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
            result.add(event);
        }
        cursor.close();
        db.close();
        return result;
    }

    public Event load(long id) {
        Event result = new Event();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {NAME, MESSAGE, CONTACT_ID, LOCATION_ID};
        String selection = ID + " = ?";
        String[] selectionArgs = {new Long(id).toString()};
        Cursor cursor = db.query(CustomDBHelper.EVENTS_TABLE, columns, selection, selectionArgs, null, null, null, null);
        cursor.moveToNext();
        result.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        result.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE)));
        String contactUri = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_ID));
        Contact contact = contactRepository.getContact(Uri.parse(contactUri));
        result.setContact(contact);
        Location location = locationRepository.load(cursor.getLong(cursor.getColumnIndexOrThrow(LOCATION_ID)));
        result.setLocation(location);
        cursor.close();
        db.close();
        return result;
    }
}
