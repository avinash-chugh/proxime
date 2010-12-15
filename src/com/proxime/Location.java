package com.proxime;

import java.io.Serializable;

public class Location implements Serializable {
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


    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
