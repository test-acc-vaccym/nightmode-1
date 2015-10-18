package com.nego.nightmode.Receiver;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.NMToggle;

public class NfcReceiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("NFC", "OK");
        Log.i("NFC", "" + getIntent().getIntExtra(NfcAdapter.EXTRA_ID, -1));
        finish();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
            int id = getIntent().getIntExtra(NfcAdapter.EXTRA_ID, -1);
            Toast.makeText(this, "ID: " + id, Toast.LENGTH_LONG).show();
            SharedPreferences SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
            if (SP.getString(Costants.PREFERENCES_NFC_ID, "").equals(id)) {
                NMToggle.startAction(this, SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false) ? Costants.ACTION_NIGHT_MODE_OFF : Costants.ACTION_NIGHT_MODE_ON);
            }
        }
    }
}
