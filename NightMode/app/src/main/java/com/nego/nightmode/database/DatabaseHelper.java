package com.nego.nightmode.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nego.nightmode.Costants;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarmdb";
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_ALARM = "create table IF NOT EXISTS alarm (id integer primary key autoincrement, day integer default 0, start long default 0, end long default 0);";
    private static final String DATABASE_CREATE_MODE = "create table IF NOT EXISTS mode (id integer primary key autoincrement, name text default '', icon text default '', color text default '', def text default '', isDefault integer default 0, notification integer default 0, nfc text default '', wifi integer default 1, bluetooth integer default 0, alarm_sound integer default 0, alarm_level integer default 5, do_not_disturb integer default 0, priority_mode integer default 0, screen_off integer default 0, last_activation long default 0);";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_ALARM);
        database.execSQL(DATABASE_CREATE_MODE);
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {
        onCreate(database);
    }
}