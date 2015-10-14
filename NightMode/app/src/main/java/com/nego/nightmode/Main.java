package com.nego.nightmode;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nego.nightmode.Functions.NMToggle;

public class Main extends AppCompatActivity {

    private ImageView button;
    private SharedPreferences SP;
    private BroadcastReceiver mReceiver;
    private TextView ui_enabled;
    private TextView ui_start_time;
    private Button button_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        button = (ImageView) findViewById(R.id.button_center);
        button_title = (Button) findViewById(R.id.button_title);
        ui_enabled = (TextView) findViewById(R.id.app_isenabled);
        ui_start_time = (TextView) findViewById(R.id.start_time);

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);

        updateUI(SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false));
        button_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchNightMode(!button.isSelected());
            }
        });

        findViewById(R.id.action_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Main.this, Settings.class), 1);
            }
        });

        findViewById(R.id.action_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Main.this, About.class), 1);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Costants.ACTION_NIGHT_MODE_ON);
        intentFilter.addAction(Costants.ACTION_NIGHT_MODE_OFF);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI(SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false));
            }
        };
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
        }
    }

    public void updateUI(boolean activated) {
        button.setSelected(activated);
        button_title.setText(activated ? R.string.action_disable : R.string.action_enable);
        ui_enabled.setText(activated ? R.string.app_name_disabled : R.string.app_name_disabled);
        ui_start_time.setText(getString(activated ? R.string.start_time_enabled : R.string.start_time_disabled, Utils.getDate(this, SP.getLong(Costants.PREFERENCES_START_TIME, 0))));
    }

    public void switchNightMode(boolean activated) {
        updateUI(activated);
        NMToggle.startAction(this, activated ? Costants.ACTION_NIGHT_MODE_ON : Costants.ACTION_NIGHT_MODE_OFF);
    }

}
