package com.nego.nightmode;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
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
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    SharedPreferences SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);

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

        // TOGGLE MENU
        findViewById(R.id.action_choose_toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, ToggleSettings.class));
            }
        });
    }

}
