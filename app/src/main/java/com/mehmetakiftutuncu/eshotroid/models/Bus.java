package com.mehmetakiftutuncu.eshotroid.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Bus implements Comparable, Parcelable {
    public final int id;
    public final String departure;
    public final String arrival;
    public boolean isStarred;

    public Bus(int id, String departure, String arrival, boolean isStarred) {
        this.id        = id;
        this.departure = departure;
        this.arrival   = arrival;
        this.isStarred = isStarred;
    }

    public Bus(Parcel parcel) {
        this.id        = parcel.readInt();
        this.departure = parcel.readString();
        this.arrival   = parcel.readString();
        this.isStarred = parcel.readInt() == 1;
    }

    @Override public String toString() {
        return String.format(
            "{\"id\":%d,\"departure\":\"%s\",\"arrival\":\"%s\",\"isStarred\":%s}",
            id,
            departure,
            arrival,
            isStarred
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
        parcel.writeInt(isStarred ? 1 : 0);
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
