package com.nego.nightmode.Receiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.NMToggle;
import com.nego.nightmode.R;
import com.nego.nightmode.Utils;

public class NfcReceiver extends AppCompatActivity {

    private Dialog dialog_nfc;
    private SharedPreferences SP;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("NFC", "OK");
        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        mNfcAdapter = manager.getDefaultAdapter();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            Log.i("NFC", "TECH");
            String id = Utils.ByteArrayToHexString(getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID));
            if (SP.getString(Costants.PREFERENCES_NFC_ID, "").equals(id)) {
                NMToggle.startAction(this, SP.getBoolean(Costants.PREFERENCES_NIGHT_MODE_ACTIVE, false) ? Costants.ACTION_NIGHT_MODE_OFF : Costants.ACTION_NIGHT_MODE_ON);
                finish();
            } else {
                final View dialogView = LayoutInflater.from(NfcReceiver.this).inflate(R.layout.dialog_line, null);
                dialog_nfc = new Dialog(NfcReceiver.this, R.style.mDialog);
                final TextView tag_id = (TextView) dialogView.findViewById(R.id.nfc_id);
                tag_id.setText(id);

                dialogView.findViewById(R.id.action_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!SP.getString(Costants.PREFERENCES_NFC_ID, "").equals("")) {
                            SP.edit().putString(Costants.PREFERENCES_NFC_ID, tag_id.getText().toString()).apply();
                            dialog_nfc.dismiss();
                        } else {
                            new AlertDialog.Builder(NfcReceiver.this)
                                    .setTitle(R.string.text_attention)
                                    .setMessage(R.string.text_ask_update_nfc)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            SP.edit().putString(Costants.PREFERENCES_NFC_ID, tag_id.getText().toString()).apply();
                                            dialog_nfc.dismiss();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                        }
                    }
                });
                dialogView.findViewById(R.id.button_reset).setVisibility(View.GONE);
                dialog_nfc.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog_nfc.setContentView(dialogView);
                dialog_nfc.show();
            }
        } else if (getIntent().getAction().equals(Costants.ACTION_PAIR_NFC)) {
            final View dialogView = LayoutInflater.from(NfcReceiver.this).inflate(R.layout.dialog_line, null);
            dialog_nfc = new Dialog(NfcReceiver.this, R.style.mDialog);
            final TextView tag_id = (TextView) dialogView.findViewById(R.id.nfc_id);
            if (!SP.getString(Costants.PREFERENCES_NFC_ID, "").equals(""))
                tag_id.setText(SP.getString(Costants.PREFERENCES_NFC_ID, ""));

            dialogView.findViewById(R.id.action_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SP.edit().putString(Costants.PREFERENCES_NFC_ID, tag_id.getText().toString()).apply();
                    dialog_nfc.dismiss();
                }
            });
            dialogView.findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(NfcReceiver.this)
                            .setTitle(R.string.text_attention)
                            .setMessage(R.string.text_ask_reset_nfc)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SP.edit().putString(Costants.PREFERENCES_NFC_ID, "").apply();
                                    dialog_nfc.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });
            dialog_nfc.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
            dialog_nfc.setContentView(dialogView);
            dialog_nfc.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("NFC", "NEW INTENT");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            String id = Utils.ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            ((TextView) dialog_nfc.findViewById(R.id.nfc_id)).setText(id);
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

}
