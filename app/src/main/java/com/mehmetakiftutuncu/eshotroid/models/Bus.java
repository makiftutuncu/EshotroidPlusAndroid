package com.mehmetakiftutuncu.eshotroid.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Random;

public class Bus implements Comparable, Parcelable {
    public final int id;
    public final String departure;
    public final String arrival;

    public Bus(int id, String departure, String arrival) {
        this.id        = id;
        this.departure = departure;
        this.arrival   = arrival;
    }

    public Bus(Parcel parcel) {
        this.id        = parcel.readInt();
        this.departure = parcel.readString();
        this.arrival   = parcel.readString();
    }

    public int getColor() {
        Random random = new Random();

        return Color.rgb(
            random.nextInt(255) + 1,
            random.nextInt(255) + 1,
            random.nextInt(255) + 1
        );
    }

    @Override public String toString() {
        return String.format(
            "{\"id\":%d,\"departure\":\"%s\",\"arrival\":\"%s\"}",
            id,
            departure,
            arrival
        );
    }

    @Override public int compareTo(@NonNull Object other) {
        if (other instanceof Bus) {
            Bus otherBus = (Bus) other;

            return (id > otherBus.id) ? 1 : ((id < otherBus.id) ? -1 : 0);
        }

        return 1;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(departure);
        parcel.writeString(arrival);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Bus createFromParcel(Parcel parcel) {
            return new Bus(parcel);
        }

        public Bus[] newArray(int size) {
            return new Bus[size];
        }
    };
}
