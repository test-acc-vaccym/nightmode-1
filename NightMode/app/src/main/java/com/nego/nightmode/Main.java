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
import android.widget.ImageView;

import com.nego.nightmode.Functions.NMToggle;

public class Main extends AppCompatActivity {

    private FloatingActionButton button;
    private SharedPreferences SP;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (FloatingActionButton) findViewById(R.id.fab);

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);

        updateUI(SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchNightModeWithText(!button.isSelected());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(this, Settings.class), 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateUI(boolean activated) {
        button.setSelected(activated);
        button.setImageResource(activated ? R.drawable.ic_action_night_mode_on : R.drawable.ic_action_sunny);
    }

    public void switchNightMode(boolean activated) {
        updateUI(activated);
        NMToggle.startAction(this, activated ? Costants.ACTION_NIGHT_MODE_ON : Costants.ACTION_NIGHT_MODE_OFF);
    }

    public void switchNightModeWithText(boolean activated) {
        switchNightMode(activated);
        Snackbar.make(button, activated ? R.string.app_name_enabled : R.string.app_name_disabled , Snackbar.LENGTH_SHORT).show();
    }

}
