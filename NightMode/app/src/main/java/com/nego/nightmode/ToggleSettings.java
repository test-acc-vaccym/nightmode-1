package com.nego.nightmode;

import android.app.Activity;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nego.nightmode.Functions.NMToggle;
import com.nego.nightmode.Receiver.DeviceAdminReceiver;

public class ToggleSettings extends AppCompatActivity {

    SharedPreferences SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosetoggle);

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
                final View dialogView = LayoutInflater.from(ToggleSettings.this).inflate(R.layout.dialog_slider, null);
                final Dialog dialog_slider = new Dialog(ToggleSettings.this, R.style.mDialog);
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

        // PRIORITY MODE AND SCREEN OFF
        updateUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
    }

    public void updateUi() {
        final CheckBox screenOffC = (CheckBox) findViewById(R.id.screen_check);
        if (checkAdmin()) {
            screenOffC.setVisibility(View.VISIBLE);
            screenOffC.setChecked(SP.getBoolean(Costants.PREFERENCES_SCREEN_OFF, true));
            screenOffC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    SP.edit().putBoolean(Costants.PREFERENCES_SCREEN_OFF, isChecked).apply();
                }
            });
            findViewById(R.id.action_screen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screenOffC.setChecked(!screenOffC.isChecked());
                }
            });
        } else {
            screenOffC.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.screen_subtitle)).setText(R.string.action_grant_deviceadmin);
            findViewById(R.id.action_screen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComponentName mAdminName = new ComponentName(ToggleSettings.this, DeviceAdminReceiver.class);
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                            mAdminName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            getString(R.string.text_request_dev));
                    startActivityForResult(intent, 5);
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final CheckBox pC = (CheckBox) findViewById(R.id.priority_check);
            if (haveNotificationAccess()) {
                if (SP.getBoolean(Costants.PREFERENCES_NOTIFICATION, true)) {
                    ((TextView) findViewById(R.id.priority_subtitle)).setText(R.string.subtitle_priority_preferences);
                    pC.setVisibility(View.VISIBLE);
                    pC.setChecked(SP.getBoolean(Costants.PREFERENCES_PRIORITY_MODE, true));
                    pC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            SP.edit().putBoolean(Costants.PREFERENCES_PRIORITY_MODE, isChecked).apply();
                        }
                    });
                    findViewById(R.id.action_priority).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pC.setChecked(!pC.isChecked());
                        }
                    });
                } else {
                    pC.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.priority_subtitle)).setText(R.string.active_notification);
                    findViewById(R.id.action_priority).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }
            } else {
                pC.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.priority_subtitle)).setText(R.string.action_grant_notificationaccess);
                findViewById(R.id.action_priority).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                        startActivity(intent);
                    }
                });
            }
        } else {
            findViewById(R.id.container_priority).setVisibility(View.GONE);
        }
    }

    public boolean checkAdmin() {
        DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mAdminName = new ComponentName(this, DeviceAdminReceiver.class);

        if (!mDPM.isAdminActive(mAdminName)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean haveNotificationAccess() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = android.provider.Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();

        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName))
            return false;
        else
            return true;
    }
}
