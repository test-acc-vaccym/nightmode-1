package com.nego.nightmode.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nego.nightmode.Alarm;

import java.util.ArrayList;

public class DbAdapter {


    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE_PEOPLE = "alarm";

    public static final String KEY_ID = "id";
    public static final String KEY_DAY = "day";
    public static final String KEY_START = "start";
    public static final String KEY_END = "end";

    public DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        if (database.getVersion() < DatabaseHelper.DATABASE_VERSION)
            dbHelper.onUpgrade(database, database.getVersion(), DatabaseHelper.DATABASE_VERSION);
        else
            dbHelper.onCreate(database);
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(int day, long start, long end) {
        ContentValues values = new ContentValues();
        values.put(KEY_DAY, day);
        values.put(KEY_START, start);
        values.put(KEY_END, end);

        return values;
    }

    public long createAlarm(Alarm a) {
        ContentValues initialValues = createContentValues(a.getDay(), a.getStart(), a.getEnd());
        return database.insertOrThrow(DATABASE_TABLE_PEOPLE, null, initialValues);
    }

    public boolean deleteAlarm(int day) {
        return database.delete(DATABASE_TABLE_PEOPLE, KEY_DAY + "==" + day, null) > 0;
    }


    public Cursor fetchAllAlarms() {
        return database.query(DATABASE_TABLE_PEOPLE, new String[]{KEY_ID, KEY_DAY, KEY_START, KEY_END}, null, null, null, null, null);
    }

    public Cursor getAlarmByDay(int day) {
        Cursor mCursor = database.query(true, DATABASE_TABLE_PEOPLE, new String[]{
                        KEY_ID, KEY_DAY, KEY_START, KEY_END},
                KEY_DAY + " == " + day, null, null, null, null, null);

        return mCursor;
    }


}