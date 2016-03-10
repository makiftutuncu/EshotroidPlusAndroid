package com.mehmetakiftutuncu.eshotroid.models;

public class Stop {
    public final int id;
    public final String name;
    public final Location location;

    public Stop(int id, String name, Location location) {
        this.id       = id;
        this.name     = name;
        this.location = location;
    }

    public Stop(int id, String name, double latitude, double longitude) {
        this(id, name, new Location(latitude, longitude));
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":%d,\"name\":\"%s\",\"location\":%s}",
            id,
            name,
            location.toString()
        );
    }
}
