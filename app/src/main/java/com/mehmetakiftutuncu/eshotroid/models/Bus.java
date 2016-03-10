package com.mehmetakiftutuncu.eshotroid.models;

public class Bus {
    public final int id;
    public final String departure;
    public final String arrival;

    public Bus(int id, String departure, String arrival) {
        this.id        = id;
        this.departure = departure;
        this.arrival   = arrival;
    }

    @Override
    public String toString() {
        return String.format(
            "{\"id\":%d,\"departure\":\"%s\",\"arrival\":\"%s\"",
            id,
            departure,
            arrival
        );
    }
}
