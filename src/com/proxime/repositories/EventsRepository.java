package com.proxime.repositories;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.proxime.Contact;
import com.proxime.ContactRepository;
import com.proxime.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsRepository  implements ColumnNames
{

    private Activity activity;
    private CustomDBHelper dbHelper;
    private ContactRepository contactRepository;

    public EventsRepository(Activity activity) {
        this.activity = activity;
        dbHelper = new CustomDBHelper(activity.getApplicationContext());
        contactRepository = new ContactRepository(activity);
    }

    public long save(Event event) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, event.getName());
        values.put(MESSAGE, event.getMessage());
//        values.put(LOCATION_ID, event.getLocation().getId());
        values.put(CONTACT_ID, event.getContact().getUri().toString());

        long id = db.insertOrThrow(CustomDBHelper.EVENTS_TABLE, null, values);
        db.close();
        return id;
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
        String[] columns = {NAME, MESSAGE, CONTACT_ID};
        String selection = ID + " = ?";
        String[] selectionArgs = {new Long(id).toString()};
        Cursor cursor = db.query(CustomDBHelper.EVENTS_TABLE, columns, selection, selectionArgs, null, null, null, null);
        cursor.moveToNext();
        result.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
        result.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(MESSAGE)));
        String contactUri = cursor.getString(cursor.getColumnIndexOrThrow(CONTACT_ID));
        Contact contact = contactRepository.getContact(Uri.parse(contactUri));
        result.setContact(contact);
        cursor.close();
        db.close();
        return result;
    }
}
