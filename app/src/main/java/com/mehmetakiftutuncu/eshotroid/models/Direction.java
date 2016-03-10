package com.mehmetakiftutuncu.eshotroid.models;

import android.text.TextUtils;

import com.mehmetakiftutuncu.eshotroid.utilities.option.None;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Option;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Some;

public enum Direction {
    DEPARTURE("departure"),
    ARRIVAL("arrival");

    public final String name;

    Direction(String name) {
        this.name = name;
    }

    public static Option<Direction> withName(String name) {
        if (TextUtils.isEmpty(name)) {
            return new None<>();
        } else if (name.equals(DEPARTURE.name)) {
            return new Some<>(DEPARTURE);
        } else if (name.equals(ARRIVAL.name)) {
            return new Some<>(ARRIVAL);
        } else {
            return new None<>();
        }
    }
}
