package com.nego.nightmode;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.nego.nightmode.database.DbAdapter;

public class Mode implements Parcelable {

    private int id;
    private String name;
    private String icon;
    private String color;
    private String def;
    private int isDefault;
    private int notification;
    private String nfc;
    private int wifi;
    private int bluetooth;
    private int alarm_sound;
    private int alarm_level;
    private int do_no_disturb;
    private int priority_mode;
    private int screen_off;
    private long last_activation;

    public Mode() {
        this.name = "";
        this.icon = "";
        this.color = "";
        this.def = "";
        this.isDefault = 0;
        this.notification = 0;
        this.nfc = "";
        this.wifi = 1;
        this.bluetooth = 1;
        this.alarm_sound = 0;
        this.alarm_level = 0;
        this.do_no_disturb = 0;
        this.priority_mode = 0;
        this.screen_off = 0;
        this.last_activation = 0;
    }

    public Mode(String name, String icon, String color, String def, int isDefault, int notification, String nfc, int wifi, int bluetooth, int alarm_sound, int alarm_level, int do_no_disturb, int priority_mode, int screen_off, long last_activation) {
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.def = def;
        this.isDefault = isDefault;
        this.notification = notification;
        this.nfc = nfc;
        this.wifi = wifi;
        this.bluetooth = bluetooth;
        this.alarm_sound = alarm_sound;
        this.alarm_level = alarm_level;
        this.do_no_disturb = do_no_disturb;
        this.priority_mode = priority_mode;
        this.screen_off = screen_off;
        this.last_activation = last_activation;
    }

    public Mode(int id, String name, String icon, String color, String def, int isDefault, int notification, String nfc, int wifi, int bluetooth, int alarm_sound, int alarm_level, int do_no_disturb, int priority_mode, int screen_off, long last_activation) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.def = def;
        this.isDefault = isDefault;
        this.notification = notification;
        this.nfc = nfc;
        this.wifi = wifi;
        this.bluetooth = bluetooth;
        this.alarm_sound = alarm_sound;
        this.alarm_level = alarm_level;
        this.do_no_disturb = do_no_disturb;
        this.priority_mode = priority_mode;
        this.screen_off = screen_off;
        this.last_activation = last_activation;
    }

    public Mode(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_ID));
        this.name = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_MODE_NAME));
        this.icon = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_MODE_ICON));
        this.color = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_MODE_COLOR));
        this.def = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_MODE_DEF));
        this.isDefault = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_IS_DEFAULT));
        this.notification = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_NOTIFICATION));
        this.nfc = cursor.getString(cursor.getColumnIndex(DbAdapter.KEY_MODE_NFC));
        this.wifi = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_WIFI));
        this.bluetooth = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_BLUETOOTH));
        this.alarm_sound = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_ALARM_SOUND));
        this.alarm_level = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_ALARM_LEVEL));
        this.do_no_disturb = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_DO_NOT_DISTURB));
        this.priority_mode = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_PRIORITY_MODE));
        this.screen_off = cursor.getInt(cursor.getColumnIndex(DbAdapter.KEY_MODE_SCREEN_OFF));
        this.last_activation = cursor.getLong(cursor.getColumnIndex(DbAdapter.KEY_MODE_LAST_ACTIVATION));
    }

    public int getId() {
        return id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setAlarm_sound(boolean alarm_sound) {
        this.alarm_sound = alarm_sound ? 1 : 0;
    }

    public boolean getAlarm_sound() {
        return (alarm_sound == 1);
    }

    public int getAlarm_soundDB() {
        return alarm_sound;
    }

    public void setAlarm_level(int alarm_level) {
        this.alarm_level = alarm_level;
    }

    public int getAlarm_level() {
        return alarm_level;
    }

    public void setBluetooth(boolean bluetooth) {
        this.bluetooth = bluetooth ? 1 : 0;
    }

    public boolean getBluetooth() {
        return (bluetooth == 1);
    }

    public int getBluetoothDB() {
        return bluetooth;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getDef() {
        return def;
    }

    public void setDo_no_disturb(boolean do_no_disturb) {
        this.do_no_disturb = do_no_disturb ? 1 : 0;
    }

    public void setDo_no_disturb(int do_no_disturb) {
        this.do_no_disturb = do_no_disturb;
    }

    public boolean getDo_no_disturb() {
        return (do_no_disturb == 1);
    }

    public int getDo_no_disturbDB() {
        return do_no_disturb;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault ? 1 : 0;
    }

    public boolean getIsDefault() {
        return isDefault == 1;
    }

    public int getIsDefaultDB() {
        return isDefault;
    }

    public void setLast_activation(long last_activation) {
        this.last_activation = last_activation;
    }

    public long getLast_activation() {
        return last_activation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNotification(boolean notification) {
        this.notification = notification ? 1 : 0;
    }

    public boolean getNotification() {
        return (notification == 1);
    }

    public int getNotificationDB() {
        return notification;
    }

    public void setPriority_mode(boolean priority_mode) {
        this.priority_mode = priority_mode ? 1 : 0;
    }

    public boolean getPriority_mode() {
        return (priority_mode == 1);
    }

    public int getPriority_modeDB() {
        return priority_mode;
    }

    public void setScreen_off(boolean screen_off) {
        this.screen_off = screen_off ? 1 : 0;
    }

    public boolean getScreen_off() {
        return (screen_off == 1);
    }

    public int getScreen_offDB() {
        return screen_off;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi ? 1 : 0;
    }

    public boolean getWifi() {
        return wifi == 1;
    }

    public int getWifiDB() {
        return wifi;
    }

    public boolean insert(DbAdapter dbAdapter) {
        return dbAdapter.createMode(this) > 0;
    }

    public boolean insertDefault(DbAdapter dbAdapter) {
        return dbAdapter.createDefaultMode(this) > 0;
    }

    public boolean update(DbAdapter dbAdapter) {
        return dbAdapter.updateMode(this);
    }

    public boolean delete(DbAdapter dbAdapter) {
        return dbAdapter.deleteMode(id);
    }


    // PARCELIZZAZIONE

    public static final Parcelable.Creator<Mode> CREATOR = new Creator<Mode>() {
        public Mode createFromParcel(Parcel source) {
            return new Mode(source.readInt(), source.readString(), source.readString(), source.readString(), source.readString(), source.readInt(), source.readInt(), source.readString(), source.readInt(), source.readInt(), source.readInt(), source.readInt(), source.readInt(), source.readInt(), source.readInt(), source.readLong());
        }
        public Mode[] newArray(int size) {
            return new Mode[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(color);
        dest.writeString(def);
        dest.writeInt(isDefault);
        dest.writeInt(notification);
        dest.writeString(nfc);
        dest.writeInt(wifi);
        dest.writeInt(bluetooth);
        dest.writeInt(alarm_sound);
        dest.writeInt(alarm_level);
        dest.writeInt(do_no_disturb);
        dest.writeInt(priority_mode);
        dest.writeInt(screen_off);
        dest.writeLong(last_activation);
    }
}
