package com.nego.nightmode;

import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nego.nightmode.Adapter.ViewPagerAdapter;
import com.nego.nightmode.Adapter.mAdapterMode;
import com.nego.nightmode.database.DbAdapter;
import com.viewpagerindicator.CirclePageIndicator;

public class Main extends AppCompatActivity {

    private SharedPreferences SP;
    private BroadcastReceiver mReceiver;
    private RelativeLayout background_toolbar;
    private RelativeLayout support_background;

    private mAdapterMode mAdapterMode;

    private RecyclerView recList;
    private BottomSheetBehavior behavior;
    private TextView name;
    private TextView time;
    private ImageView icon;

    Mode actual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        SP = getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
        if (Utils.updateToMode(this, SP)) {
            // TODO start Tutorial update
        }

        // MAIN ACTIVITY
        actual = Utils.getActualMode(this);
        if (actual == null) {
            Toast.makeText(this, getString(R.string.text_error), Toast.LENGTH_SHORT).show();
            finish();
        }

        background_toolbar = (RelativeLayout) findViewById(R.id.background_toolbar);
        support_background = (RelativeLayout) findViewById(R.id.support_background);
        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        icon = (ImageView) findViewById(R.id.icon);

        // BOTTOM SHEET
        behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                updateStatusBar();
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        // OPEN/CLOSE FUNCTIONS
        findViewById(R.id.action_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseBS();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // RECYCLER LIST
        recList = (RecyclerView) findViewById(R.id.listView);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        updateUI();

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Costants.ACTION_NIGHT_MODE_TOGGLE);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                actual = Utils.getActualMode(Main.this);
                updateUI();
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
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
            super.onBackPressed();
    }

    public void updateStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Main.this, actual.getDarkColor()));
        }
    }

    public void updateUI() {
        background_toolbar.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        name.setText(getString(R.string.text_mode, actual.getName(Main.this)));
                        time.setText(getString(R.string.start_time_enabled, Utils.getDate(Main.this, actual.getLast_activation())));
                        icon.setImageDrawable(ContextCompat.getDrawable(Main.this, actual.getIcon()));

                        background_toolbar.setBackgroundColor(ContextCompat.getColor(Main.this, actual.getColor()));
                        background_toolbar
                                .animate()
                                .alpha(1)
                                .setInterpolator(new AccelerateDecelerateInterpolator())
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        support_background.setBackgroundColor(ContextCompat.getColor(Main.this, actual.getColor()));
                                        updateStatusBar();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

        final Handler mHandler = new Handler();

        new Thread(new Runnable() {
            public void run() {
                DbAdapter dbHelper = new DbAdapter(Main.this);
                dbHelper.open();
                mAdapterMode = new mAdapterMode(dbHelper, Main.this);
                dbHelper.close();
                mHandler.post(new Runnable() {
                    public void run() {
                        recList.setAdapter(mAdapterMode);
                    }
                });
            }
        }).start();
    }

    public void collapseBS() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
