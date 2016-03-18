package com.nego.nightmode;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // VERSIONE
        String version = "";
        try {
            version = getString(R.string.text_version, getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (Exception e) {
            version = " not found";
        }
        ((TextView) findViewById(R.id.subtitle_versione)).setText(version);


        // COMMUNITY
        findViewById(R.id.action_community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/communities/100614116200820350356/stream/9bc2ab8b-45f2-40ed-822f-0e7439009a62")));
            }
        });

    }
}
