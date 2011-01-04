package com.proxime.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Entity {
    private long id;
    private String name;
    private double latitude;
    private double longitude;
    private int span;

    public Location(long id, String name, double latitude, double longitude, int span) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.span = span;
    }

    public Location(String name, double latitude, double longitude, int span) {
        this(0,name,latitude,longitude,span);
    }

    public Location(int id, String name) {
        this(id, name, 0.0, 0.0, 0);
    }

    public String getName() {
        return name;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public boolean isValid() {
        return latitude != 0.0 && longitude != 0.0 && span != 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getSpan() {
        return span;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(span);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public Location(Parcel in) {
        id = in.readLong();
        name = in.readString();
        span = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

}
