package com.mehmetakiftutuncu.eshotroid.models;

public class Location {
    public final double latitude;
    public final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude  = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"latitude\":%f,\"longitude\":%f}",
            latitude,
            longitude
        );
    }
}
