package com.proxime;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Entity {

    private Contact contact;
    private Location location;
    private String name;
    private String message;
    private long id;

    public Event() {
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public Contact getContact() {
        return contact;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int parcelFlags) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(message);
        parcel.writeParcelable(contact, parcelFlags);
    }

    public Event(Parcel in) {
        id = in.readLong();
        name = in.readString();
        message = in.readString();
        contact = in.readParcelable(Contact.class.getClassLoader()); //throws ClassNotFoundException if passed null
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Entity createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
