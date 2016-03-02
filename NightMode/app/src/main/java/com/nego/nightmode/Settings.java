package com.nego.nightmode;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import com.nego.nightmode.Receiver.NfcReceiver;

public class Settings extends AppCompatActivity {

    private SharedPreferences SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);

        // AUTOMATIC RULES
        findViewById(R.id.action_auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, Rules.class));
            }
        });

        // VERSION
        String version = "";
        try {
            version = getString(R.string.text_version, getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (Exception e) {
            version = getString(R.string.error_version_not_found);
        } finally {
            ((TextView) findViewById(R.id.subtitle_versione)).setText(version);
        }

        // COMMUNITY
        findViewById(R.id.action_community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/communities/100614116200820350356/stream/9bc2ab8b-45f2-40ed-822f-0e7439009a62")));
            }
        });

        // NFC
        /* TODO NFC
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter == null) {
            findViewById(R.id.nfcOk).setVisibility(View.GONE);
        } else {
            findViewById(R.id.nfcOk).setVisibility(View.VISIBLE);
            if (adapter.isEnabled()) {
                findViewById(R.id.action_nfc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Settings.this, NfcReceiver.class);
                        i.setAction(Costants.ACTION_PAIR_NFC);
                        startActivity(i);
                    }
                });
            } else {
                findViewById(R.id.action_nfc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Settings.this, getString(R.string.error_nfc_disabled), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }*/
    }



}
