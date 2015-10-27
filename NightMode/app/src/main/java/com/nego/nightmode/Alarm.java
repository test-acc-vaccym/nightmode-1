package com.nego.nightmode;


import android.app.PendingIntent;
import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.nego.nightmode.Functions.AlarmF;
import com.nego.nightmode.database.DbAdapter;

public class Alarm implements Parcelable {
    private int id;
    private int day;
    private long start;
    private long end;

    public Alarm(int id, int day, long start, long end){
        this.id = id;
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public Alarm(int day, long start, long end){
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public Alarm(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_ID));
        this.day = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_DAY));
        this.start = cursor.getLong(cursor.getColumnIndex(DbAdapter.KEY_START));
        this.end = cursor.getLong(cursor.getColumnIndex(DbAdapter.KEY_END));
    }

    public int getId() {
        return id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean create_alarm(Context context, DbAdapter dbHelper) {
        delete_alarm(context, dbHelper);
        this.id = (int) dbHelper.createAlarm(this);
        if (id > 0) {
            AlarmF.addAlarm(context, this.id + 10000, this.start, Costants.ACTION_NIGHT_MODE_ON);
            AlarmF.addAlarm(context, this.id, this.end, Costants.ACTION_NIGHT_MODE_OFF);
            return true;
        }
        return false;
    }

    public boolean delete_alarm(Context context, DbAdapter dbHelper) {
        if (dbHelper.deleteAlarm(this.getDay())) {
            AlarmF.deleteAlarm(context, this.id + 10000);
            AlarmF.deleteAlarm(context, this.id);
            return true;
        }
        return false;
    }


    // PARCELIZZAZIONE

    public static final Parcelable.Creator<Alarm> CREATOR = new Creator<Alarm>() {
        public Alarm createFromParcel(Parcel source) {
            return new Alarm(source.readInt(), source.readInt(), source.readLong(), source.readLong());
        }
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(day);
        dest.writeLong(start);
        dest.writeLong(end);
    }
}
