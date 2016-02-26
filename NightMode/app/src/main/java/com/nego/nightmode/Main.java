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

    private RecyclerView recList;
    private BottomSheetBehavior behavior;

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

        actual = Utils.getActualMode(this);
        if (actual == null) {
            Toast.makeText(this, getString(R.string.text_error), Toast.LENGTH_SHORT).show();
            finish();
        }

        background_toolbar = (RelativeLayout) findViewById(R.id.background_toolbar);

        behavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Main.this, R.color.white_dark));
                    } else {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Main.this, R.color.primary_dark));
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        behavior.setPeekHeight((int) getResources().getDimension(R.dimen.activity_horizontal_margin) * 4);

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

    public void updateUI() {
        //behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        /*
        background_toolbar.animate()
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            int cx = background_toolbar.getWidth() / 2;
                            int cy = background_toolbar.getHeight() / 2;
                            int finalRadius = Math.max(background_toolbar.getWidth(), background_toolbar.getHeight());
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(background_toolbar, cx, cy, 0, finalRadius);

                            background_toolbar.setBackgroundColor(ContextCompat.getColor(Main.this, Utils.getModeColor(actual.getColor())));
                            background_toolbar.setAlpha(1);
                            anim.setInterpolator(new AccelerateDecelerateInterpolator());
                            anim.setDuration(500);
                            anim.start();
                        } else {
                            background_toolbar.setBackgroundColor(ContextCompat.getColor(Main.this, Utils.getModeColor(actual.getColor())));
                            background_toolbar.setAlpha(1);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();

*/
        final Handler mHandler = new Handler();

        new Thread(new Runnable() {
            public void run() {
                DbAdapter dbHelper = new DbAdapter(Main.this);
                dbHelper.open();
                final mAdapterMode mAdapterMode = new mAdapterMode(dbHelper, Main.this);
                dbHelper.close();

                mHandler.post(new Runnable() {
                    public void run() {
                        recList.setAdapter(mAdapterMode);
                        //behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
            }
        }).start();
    }
}
