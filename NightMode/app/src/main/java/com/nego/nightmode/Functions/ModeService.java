package com.nego.nightmode.Functions;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Mode;
import com.nego.nightmode.Utils;
import com.nego.nightmode.database.DbAdapter;

import java.util.ArrayList;

public class ModeService extends IntentService {

    public static void startAction(Context context, String action, Mode m) {
        Intent intent = new Intent(context, ModeService.class);
        intent.setAction(action);
        intent.putExtra(Costants.MODE_EXTRA, m);
        context.startService(intent);
    }

    private void sendResponse() {
        Intent i = new Intent(Costants.ACTION_NIGHT_MODE_TOGGLE);
        sendBroadcast(i);
    }

    public ModeService() {
        super("ElementService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Costants.ACTION_CREATE.equals(action)) {
                final Mode m = intent.getParcelableExtra(Costants.MODE_EXTRA);
                createElement(m);
            } else if (Costants.ACTION_UPDATE.equals(action)) {
                final Mode m = intent.getParcelableExtra(Costants.MODE_EXTRA);
                updateElement(m);
            } else if (Costants.ACTION_DELETE.equals(action)) {
                final Mode m = intent.getParcelableExtra(Costants.MODE_EXTRA);
                deleteElement(m);
            }
        }
    }

    private void createElement(Mode m) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (m.insert(dbHelper)) {
            sendResponse();
        }
        dbHelper.close();
    }

    private void updateElement(Mode m) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (m.update(dbHelper)) {
            sendResponse();
        }
        dbHelper.close();
    }

    private void deleteElement(Mode m) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (!m.getIsDefault() && m.delete(dbHelper)) {
            sendResponse();
        }
        // CHECK DEFAULT
        SharedPreferences SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        int n = SP.getInt(Costants.ACTUAL_MODE, 0);
        if (n != 0 && m.getId() == n) {
            Cursor c = dbHelper.getModeByName(Costants.DEFAULT_MODE_DAY);
            if (c.moveToFirst()) {
                NMToggle.startAction(this, new Mode(c));
            }
        }
        dbHelper.close();
    }
}
