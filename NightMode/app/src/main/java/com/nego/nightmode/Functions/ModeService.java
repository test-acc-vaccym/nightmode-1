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

    public static void startAction(Context context, String action, Mode m, boolean sendResponse) {
        Intent intent = new Intent(context, ModeService.class);
        intent.setAction(action);
        intent.putExtra(Costants.MODE_EXTRA, m);
        intent.putExtra(Costants.SEND_RESPONSE_EXTRA, sendResponse);
        context.startService(intent);
    }

    private void sendResponse(boolean ok) {
        if (ok) {
            Intent i = new Intent(Costants.ACTION_NIGHT_MODE_TOGGLE);
            sendBroadcast(i);
        }
    }

    public ModeService() {
        super("ElementService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            boolean sendResponse = intent.getBooleanExtra(Costants.SEND_RESPONSE_EXTRA, true);
            if (Costants.ACTION_CREATE.equals(action)) {
                final Mode m = intent.getParcelableExtra(Costants.MODE_EXTRA);
                createElement(m, sendResponse);
            } else if (Costants.ACTION_UPDATE.equals(action)) {
                final Mode m = intent.getParcelableExtra(Costants.MODE_EXTRA);
                updateElement(m, sendResponse);
            } else if (Costants.ACTION_DELETE.equals(action)) {
                final Mode m = intent.getParcelableExtra(Costants.MODE_EXTRA);
                deleteElement(m, sendResponse);
            }
        }
    }

    private void createElement(Mode m, boolean sendResponse) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (m.insert(dbHelper)) {
            sendResponse(sendResponse);
        }
        dbHelper.close();
    }

    private void updateElement(Mode m, boolean sendResponse) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (m.update(dbHelper)) {
            sendResponse(sendResponse);
        }
        dbHelper.close();
    }

    private void deleteElement(Mode m, boolean sendResponse) {
        DbAdapter dbHelper = new DbAdapter(this);
        dbHelper.open();
        if (!m.getIsDefault() && m.delete(dbHelper)) {
            sendResponse(sendResponse);
        }
        // CHECK DEFAULT
        SharedPreferences SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        int n = SP.getInt(Costants.ACTUAL_MODE, 0);
        if (n != 0 && m.getId() == n) {
            NMToggle.startAction(this, Utils.getDayMode(this));
        }
        dbHelper.close();
    }
}
