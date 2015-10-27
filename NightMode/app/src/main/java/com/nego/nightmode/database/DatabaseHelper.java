package com.nego.nightmode.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarmdb";
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_PEOPLE = "create table IF NOT EXISTS alarm (id integer primary key autoincrement, day integer default 0, start long default 0, end long default 0);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_PEOPLE);
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {
        onCreate(database);
    }
}