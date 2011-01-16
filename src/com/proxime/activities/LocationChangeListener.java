package com.proxime.activities;

import com.proxime.entities.Location;

public interface LocationChangeListener {
    void change(Location location, boolean isNew);
}
