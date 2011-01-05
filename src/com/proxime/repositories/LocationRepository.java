package com.proxime.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.proxime.entities.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository implements ColumnNames{
    private CustomDBHelper dbHelper;

    public LocationRepository(Context context) {
        dbHelper = new CustomDBHelper(context);
    }

    public void save(Location location) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, location.getName());
        values.put(SPAN, location.getSpan());
        values.put(LATITUDE, location.getLatitude());
        values.put(LONGITUDE, location.getLongitude());

        long id = location.getId();
        if (id > 0)
            db.update(CustomDBHelper.LOCATIONS_TABLE, values, ID + " = ?", new String[] {Long.toString(id)});
        else
        {
            id = db.insertOrThrow(CustomDBHelper.LOCATIONS_TABLE, null, values);
            location.setId(id);
        }

        db.close();
    }


    public List<Location> loadAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(CustomDBHelper.LOCATIONS_TABLE, new String[]{ID, NAME}, null, null, null, null, null);
        List<Location> result = new ArrayList<Location>();
        while(cursor.moveToNext())
        {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ID));
            result.add(new Location(id,name));
        }
        cursor.close();
        db.close();
        return result;
    }

    public Location load(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {NAME, SPAN, LATITUDE, LONGITUDE};
        String selection = ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        Cursor cursor = db.query(CustomDBHelper.LOCATIONS_TABLE, columns, selection, selectionArgs, null, null, null, null);
        cursor.moveToNext();
        String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
        int span = cursor.getInt(cursor.getColumnIndexOrThrow(SPAN));
        double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LATITUDE));
        double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LONGITUDE));
        cursor.close();
        db.close();
        return new Location(id,name,latitude,longitude,span);
    }

    public void delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String locationId = Long.toString(id);
        ContentValues values = new ContentValues();
        values.putNull(LOCATION_ID);

        db.update(CustomDBHelper.EVENTS_TABLE, values, LOCATION_ID + " = ?", new String[] {locationId});
        db.delete(CustomDBHelper.LOCATIONS_TABLE, ID + " = ?", new String[] {locationId});
        db.close();
    }
}
