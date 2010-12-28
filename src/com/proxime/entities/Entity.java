package com.proxime.entities;

import android.os.Parcelable;

public interface Entity extends Parcelable {
    long getId();
    String getName();
}
