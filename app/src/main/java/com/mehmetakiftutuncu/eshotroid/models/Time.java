package com.mehmetakiftutuncu.eshotroid.models;

import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.option.None;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Option;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Some;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time {
    private final static String TAG = "Time";
    private final static Pattern regex = Pattern.compile("([0-2][0-9]):([0-5][0-9])");

    public final int hour;
    public final int minute;

    public Time(int hour, int minute) {
        this.hour   = hour;
        this.minute = minute;
    }

    public static Option<Time> parse(String timeString) {
        try {
            Matcher matcher = regex.matcher(timeString);

            if (matcher.find()) {
                String hourString   = matcher.group(1);
                String minuteString = matcher.group(2);

                int hour   = Integer.parseInt(hourString);
                int minute = Integer.parseInt(minuteString);

                return new Some<>(new Time(hour, minute));
            } else {
                Log.error(TAG, "Failed to parse time '%s'!", timeString);

                return new None<>();
            }
        } catch (Throwable t) {
            Log.error(TAG, t, "Failed to parse time '%s'!", timeString);

            return new None<>();
        }
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }
}
