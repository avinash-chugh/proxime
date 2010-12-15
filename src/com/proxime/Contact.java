package com.proxime;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    private String name;
    private Uri uri;

    public Contact(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int parcelFlags) {
        parcel.writeString(name);
        parcel.writeParcelable(uri, parcelFlags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            String name = in.readString();
            Uri uri = in.readParcelable(null);
            return new Contact(name, uri);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
