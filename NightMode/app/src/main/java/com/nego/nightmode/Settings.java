package com.nego.nightmode;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    SharedPreferences SP;
    boolean menu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);

        // WIFI
        final android.support.v7.widget.AppCompatCheckBox wifiC = (android.support.v7.widget.AppCompatCheckBox) findViewById(R.id.wifi_check);
        wifiC.setChecked(SP.getBoolean(Costants.PREFERENCES_WIFI, true));
        wifiC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.edit().putBoolean(Costants.PREFERENCES_WIFI, isChecked).apply();
            }
        });
        findViewById(R.id.action_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiC.setChecked(!wifiC.isChecked());
            }
        });

        // BLUETOOTH
        final android.support.v7.widget.AppCompatCheckBox bC = (android.support.v7.widget.AppCompatCheckBox) findViewById(R.id.bluetooth_check);
        bC.setChecked(SP.getBoolean(Costants.PREFERENCES_BLUETOOTH, true));
        bC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.edit().putBoolean(Costants.PREFERENCES_BLUETOOTH, isChecked).apply();
            }
        });
        findViewById(R.id.action_bluetooth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bC.setChecked(!bC.isChecked());
            }
        });

        // BRIGHTNESS
        final android.support.v7.widget.AppCompatCheckBox briC = (android.support.v7.widget.AppCompatCheckBox) findViewById(R.id.brightness_check);
        briC.setChecked(SP.getBoolean(Costants.PREFERENCES_BRIGHTNESS, false));
        briC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.edit().putBoolean(Costants.PREFERENCES_BRIGHTNESS, isChecked).apply();
            }
        });
        findViewById(R.id.action_brightness).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                briC.setChecked(!briC.isChecked());
            }
        });
        findViewById(R.id.action_brightness_level).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Settings.this, "DA FARE", Toast.LENGTH_SHORT).show();
            }
        });

        // ALARM
        final android.support.v7.widget.AppCompatCheckBox aC = (android.support.v7.widget.AppCompatCheckBox) findViewById(R.id.alarm_check);
        aC.setChecked(SP.getBoolean(Costants.PREFERENCES_ALARM_SOUND, true));
        aC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.edit().putBoolean(Costants.PREFERENCES_ALARM_SOUND, isChecked).apply();
            }
        });
        findViewById(R.id.action_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aC.setChecked(!aC.isChecked());
            }
        });

        // ALARM LEVEL
        final AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        findViewById(R.id.action_alarm_level).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = LayoutInflater.from(Settings.this).inflate(R.layout.dialog_slider, null);
                final Dialog dialog_slider = new Dialog(Settings.this, R.style.mDialog);
                SeekBar slider = (SeekBar) dialogView.findViewById(R.id.slider);
                slider.setMax(am.getStreamMaxVolume(AudioManager.STREAM_ALARM));
                slider.setProgress(SP.getInt(Costants.PREFERENCES_ALARM_SOUND_LEVEL, 5));
                slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SP.edit().putInt(Costants.PREFERENCES_ALARM_SOUND_LEVEL, seekBar.getProgress()).apply();
                        dialog_slider.dismiss();
                    }
                });
                dialog_slider.setContentView(dialogView);
                dialog_slider.show();
            }
        });

        // SILENT
        final android.support.v7.widget.AppCompatCheckBox sC = (android.support.v7.widget.AppCompatCheckBox) findViewById(R.id.silent_check);
        sC.setChecked(SP.getBoolean(Costants.PREFERENCES_DO_NOT_DISTURB, true));
        sC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.edit().putBoolean(Costants.PREFERENCES_DO_NOT_DISTURB, isChecked).apply();
            }
        });
        findViewById(R.id.action_silent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sC.setChecked(!sC.isChecked());
            }
        });

        // REMEMBER OLD
        final CheckBox oC = (CheckBox) findViewById(R.id.remember_old_check);
        oC.setChecked(SP.getBoolean(Costants.PREFERENCES_REMEMBER_OLD, true));
        oC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.edit().putBoolean(Costants.PREFERENCES_REMEMBER_OLD, isChecked).apply();
            }
        });
        findViewById(R.id.action_remember_old).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oC.setChecked(!oC.isChecked());
            }
        });

        // NOTIFICATION
        final CheckBox notC = (CheckBox) findViewById(R.id.notification_check);
        notC.setChecked(SP.getBoolean(Costants.PREFERENCES_NOTIFICATION, true));
        notC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SP.edit().putBoolean(Costants.PREFERENCES_NOTIFICATION, isChecked).apply();
            }
        });
        findViewById(R.id.action_notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notC.setChecked(!notC.isChecked());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (menu) {
            showMenu("");
        } else {
            super.onBackPressed();
        }
    }

    public void showMenu(String what) {
        switch (what) {
            case "toogle":
                menu = true;
                // TODO: mettere le visibility dei vari menu
            default:
                menu = false;
                // TODO: ripristinare le visibility dei vari menu
                break;

        }
    }

}
