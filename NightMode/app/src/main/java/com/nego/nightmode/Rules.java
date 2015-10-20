package com.nego.nightmode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.nego.nightmode.Adapter.MyAdapter;
import com.nego.nightmode.database.DbAdapter;

public class Rules extends AppCompatActivity {

    private SharedPreferences SP;
    private RecyclerView day_list;
    private DbAdapter dbHelper;
    private MyAdapter mAdapter;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        day_list = (RecyclerView) findViewById(R.id.day_list);
        day_list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        day_list.setLayoutManager(llm);

        doList();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Costants.ACTION_CREATE);
        intentFilter.addAction(Costants.ACTION_DELETE);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doList();
            }
        };
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    public void doList() {
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
        day_list.setVisibility(View.GONE);
        final Handler mHandler = new Handler();

        new Thread(new Runnable() {
            public void run() {
                dbHelper = new DbAdapter(Rules.this);
                dbHelper.open();
                mAdapter = new MyAdapter(dbHelper, Rules.this);
                dbHelper.close();

                mHandler.post(new Runnable() {
                    public void run() {
                        day_list.setAdapter(mAdapter);
                        findViewById(R.id.loader).setVisibility(View.GONE);
                        day_list.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }

}
