package com.proxime.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Entity {

    private Contact contact;
    private Location location;
    private String name;
    private String message;
    private long id;

    public Event() {}

    public boolean hasContact() {
        return contact != null;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    public boolean hasLocation() {
        return location != null;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String message) {
        this.message = message;
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

    //Catering to Parcelable interface

    public Event(Parcel in) {
        id = in.readLong();
        name = in.readString();
        message = in.readString();
        contact = in.readParcelable(Contact.class.getClassLoader()); //throws ClassNotFoundException if passed null
        location = in.readParcelable(Location.class.getClassLoader());
    }


    public void writeToParcel(Parcel parcel, int parcelFlags) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(message);
        parcel.writeParcelable(contact, parcelFlags);
        parcel.writeParcelable(location,parcelFlags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Entity createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public boolean isValidForNotification() {
        return contact != null && location != null && location.isValid() && !(message.length() == 0);
    }
}
