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
    private int pStart;
    private int pEnd;

    public Alarm(int id, int day, long start, long end, int pStart, int pEnd){
        this.id = id;
        this.day = day;
        this.start = start;
        this.end = end;
        this.pStart = pStart;
        this.pEnd = pEnd;
    }

    public Alarm(int day, long start, long end, int pStart, int pEnd){
        this.day = day;
        this.start = start;
        this.end = end;
        this.pStart = pStart;
        this.pEnd = pEnd;
    }

    public Alarm(Cursor cursor){
        this.id = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_ID));
        this.day = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_DAY));
        this.start = cursor.getLong(cursor.getColumnIndex(DbAdapter.KEY_START));
        this.end = cursor.getLong(cursor.getColumnIndex(DbAdapter.KEY_END));
        this.pStart = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_PSTART));
        this.pEnd = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_PEND));
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

    public int getpStart() {
        return pStart;
    }

    public void setpStart(int pStart) {
        this.pStart = pStart;
    }

    public int getpEnd() {
        return pEnd;
    }

    public void setpEnd(int pEnd) {
        this.pEnd = pEnd;
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
        pStart = AlarmF.addAlarm(context, this.id, this.start, Costants.ACTION_NIGHT_MODE_ON);
        pEnd = AlarmF.addAlarm(context, this.id, this.end, Costants.ACTION_NIGHT_MODE_OFF);
        if (dbHelper.createAlarm(this)) {
            return true;
        }
        return false;
    }

    public boolean delete_alarm(Context context, DbAdapter dbHelper) {
        if (dbHelper.deleteAlarm(this.getDay())) {
            AlarmF.deleteAlarm(context, this.id);
            AlarmF.deleteAlarm(context, this.id);
            return true;
        }
        return false;
    }


    // PARCELIZZAZIONE

    public static final Parcelable.Creator<Alarm> CREATOR = new Creator<Alarm>() {
        public Alarm createFromParcel(Parcel source) {
            return new Alarm(source.readInt(), source.readInt(), source.readLong(), source.readLong(), source.readInt(), source.readInt());
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
        dest.writeInt(pStart);
        dest.writeInt(pEnd);
    }
}
