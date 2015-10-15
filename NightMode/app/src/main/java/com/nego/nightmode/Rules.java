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
import android.widget.SeekBar;

public class Rules extends AppCompatActivity {

    SharedPreferences SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);

        //MONDAY
        findViewById(R.id.day_0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = LayoutInflater.from(Rules.this).inflate(R.layout.dialog_day, null);
                final Dialog dialog_slider = new Dialog(Rules.this, R.style.mDialog);
                dialog_slider.setContentView(dialogView);
                dialog_slider.show();
            }
        });
    }

}
