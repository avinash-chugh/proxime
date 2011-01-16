package com.proxime.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Entity {
    private long id;
    private String name;
    private double latitude;
    private double longitude;
    private int span;
    private String address;
    private static final int DEFAULT_SPAN = 25;


    public Location(long id, String name) {
        setId(id);
        setName(name);
        setLatitude(0);
        setLongitude(0);
        setSpan(DEFAULT_SPAN);
        setAddress(new String(""));
    }

    public Location(long id, String name, double latitude, double longitude, int span, String address) {
        setId(id);
        setName(name);
        setLatitude(latitude);
        setLongitude(longitude);
        setSpan(span);
        setAddress(address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        parcel.writeString(address);
    }

    public Location(Parcel in) {
        id = in.readLong();
        name = in.readString();
        span = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
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
