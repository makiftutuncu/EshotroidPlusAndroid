package com.mehmetakiftutuncu.eshotroid.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mehmetakiftutuncu.eshotroid.models.Bus;
import com.mehmetakiftutuncu.eshotroid.utilities.Log;
import com.mehmetakiftutuncu.eshotroid.utilities.option.None;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Option;
import com.mehmetakiftutuncu.eshotroid.utilities.option.Some;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final String TAG = "Database";

    public static final String DATABASE_NAME = "eshotroidplus";
    public static final int DATABASE_VERSION = 1;

    public class TableBus {
        public static final String TABLE_NAME = "bus";

        public static final String COLUMN_ID        = "id";
        public static final String COLUMN_DEPARTURE = "departure";
        public static final String COLUMN_ARRIVAL   = "arrival";

        public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_DEPARTURE + " TEXT NOT NULL," +
            COLUMN_ARRIVAL + " TEXT NOT NULL" +
        ");";
    }

    private Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Database with(Context context) {
        return new Database(context);
    }

    public Option<ArrayList<Bus>> loadBusList() {
        try {
            ArrayList<Bus> busList = new ArrayList<>();

            SQLiteDatabase database = getReadableDatabase();

            Cursor cursor = database.rawQuery("SELECT * FROM " + TableBus.TABLE_NAME, null);

            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int id           = cursor.getInt(cursor.getColumnIndex(TableBus.COLUMN_ID));
                    String departure = cursor.getString(cursor.getColumnIndex(TableBus.COLUMN_DEPARTURE));
                    String arrival   = cursor.getString(cursor.getColumnIndex(TableBus.COLUMN_ARRIVAL));

                    Bus bus = new Bus(id, departure, arrival);

                    busList.add(bus);
                }

                cursor.close();
            }

            return new Some<>(busList);
        } catch (Throwable t) {
            Log.error(TAG, "Failed to get bus list from database!", t);

            return new None<>();
        }
    }

    @Override public void onCreate(SQLiteDatabase database) {
        database.execSQL(TableBus.CREATE_TABLE_SQL);
    }

    @Override public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.warn(TAG, String.format("Upgrading database %s from version %d to %d!", DATABASE_NAME, oldVersion, newVersion));

        database.execSQL("DROP TABLE IF EXISTS " + TableBus.TABLE_NAME);

        onCreate(database);
    }
}
