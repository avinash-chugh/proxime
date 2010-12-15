package com.proxime.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomDBHelper extends SQLiteOpenHelper implements ColumnNames {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "events.db";
    public static final String LOCATIONS_TABLE = "locations";

    private static final String CREATE_LOCATIONS_TABLE =
            "CREATE TABLE " + LOCATIONS_TABLE + " ("
            + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT NOT NULL, "
            + "latitude INTEGER NOT NULL, "
            + "longitude INTEGER NOT NULL, "
            + "span INTEGER NOT NULL "
            + ")";

    public static final String EVENTS_TABLE = "events";
    private static final String CREATE_EVENTS_TABLE =
            "CREATE TABLE " + EVENTS_TABLE + " ("
            + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT NOT NULL, "
            + MESSAGE + " TEXT, "
            + LOCATION_ID + " INTEGER REFERENCES " + LOCATIONS_TABLE + " , "
            + CONTACT_ID + " INTEGER "
            + ")";


    public CustomDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOCATIONS_TABLE);            
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE);
        onCreate(db);
    }
}
