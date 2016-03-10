package com.mehmetakiftutuncu.eshotroid.models;

import android.text.TextUtils;

import com.mehmetakiftutuncu.eshotroid.utilities.option.None;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Option;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Some;

public enum DayType {
    WEEK_DAYS("weekDays"),
    SATURDAY("saturday"),
    SUNDAY("sunday");

    public final String name;

    DayType(String name) {
        this.name = name;
    }

    public static Option<DayType> withName(String name) {
        if (TextUtils.isEmpty(name)) {
            return new None<>();
        } else if (name.equals(WEEK_DAYS.name)) {
            return new Some<>(WEEK_DAYS);
        } else if (name.equals(SATURDAY.name)) {
            return new Some<>(SATURDAY);
        } else if (name.equals(SUNDAY.name)) {
            return new Some<>(SUNDAY);
        } else {
            return new None<>();
        }
    }
}
