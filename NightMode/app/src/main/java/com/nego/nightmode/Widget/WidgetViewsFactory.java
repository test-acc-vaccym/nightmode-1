package com.nego.nightmode.Widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
/*
public class WidgetViewsFactory implements
        RemoteViewsService.RemoteViewsFactory {
    private List<Reminder> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    public WidgetViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        DbAdapter dbHelper = new DbAdapter(mContext);
        dbHelper.open();
        Cursor c = dbHelper.fetchAllReminders();
        while (c.moveToNext()) {
            mWidgetItems.add(new Reminder(c));
        }
        c.close();
        dbHelper.close();
    }

    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        mWidgetItems.clear();

        DbAdapter dbHelper = new DbAdapter(mContext);
        dbHelper.open();
        Cursor c = dbHelper.fetchAllReminders();
        while (c.moveToNext()) {
            mWidgetItems.add(new Reminder(c));
        }
        c.close();
        dbHelper.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < mWidgetItems.size()) {
            SharedPreferences SP = mContext.getSharedPreferences(Costants.PREFERENCES_COSTANT, Context.MODE_PRIVATE);
            System.out.println("OOK" + SP.getString(Costants.PREFERENCE_STYLE_WIDGET, Costants.PREFERENCE_STYLE_WIDGET_MD));
            RemoteViews rv;
            if (SP.getString(Costants.PREFERENCE_STYLE_WIDGET, Costants.PREFERENCE_STYLE_WIDGET_MD).equals(Costants.PREFERENCE_STYLE_WIDGET_ML)) {
                rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_row_ml);
            } else {
                rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_row);
            }
            rv.setTextViewText(R.id.title, mWidgetItems.get(position).getTitle());
            if (mWidgetItems.get(position).getAlarm() == 0) {
                rv.setTextViewText(R.id.subtitle, Utils.getDate(mContext, mWidgetItems.get(position).getDate_create()));
            } else {
                rv.setTextViewText(R.id.subtitle, Utils.getDateAlarm(mContext, mWidgetItems.get(position).getAlarm()));
            }

            Intent i = new Intent();
            Bundle extras = new Bundle();
            extras.putParcelable(Costants.EXTRA_REMINDER, mWidgetItems.get(position));
            i.putExtras(extras);
            i.setAction(Costants.ACTION_EDIT_ITEM);
            rv.setOnClickFillInIntent(R.id.row, i);

            if (!mWidgetItems.get(position).getImg().equals("")) {
                rv.setViewVisibility(R.id.action_open_img, View.VISIBLE);
                Intent img_intent = new Intent(Intent.ACTION_VIEW).putExtra(Costants.EXTRA_ACTION_TYPE, mWidgetItems.get(position).getImg());
                rv.setOnClickFillInIntent(R.id.action_open_img, img_intent);
            } else {
                rv.setViewVisibility(R.id.action_open_img, View.GONE);
            }

            String url = Utils.checkURL(mWidgetItems.get(position).getTitle());
            if (url != "") {
                rv.setViewVisibility(R.id.action_open_browser, View.VISIBLE);
                Intent url_intent = new Intent(Intent.ACTION_VIEW).putExtra(Costants.EXTRA_ACTION_TYPE, url);
                rv.setOnClickFillInIntent(R.id.action_open_browser, url_intent);
            } else {
                rv.setViewVisibility(R.id.action_open_browser, View.GONE);
            }

            if (!mWidgetItems.get(position).getAction_type().equals("")) {
                switch (mWidgetItems.get(position).getAction_type()) {
                    case Costants.ACTION_CALL:
                        Intent call_intent = new Intent(Intent.ACTION_DIAL).putExtra(Costants.EXTRA_ACTION_TYPE, "tel:" + mWidgetItems.get(position).getAction_info());
                        rv.setViewVisibility(R.id.action_contact, View.VISIBLE);
                        rv.setImageViewResource(R.id.action_contact, R.drawable.ic_action_communication_call);
                        rv.setOnClickFillInIntent(R.id.action_contact, call_intent);
                        break;
                    case Costants.ACTION_SMS:
                        Intent sms_intent = new Intent(Intent.ACTION_VIEW).putExtra(Costants.EXTRA_ACTION_TYPE, "sms:" + mWidgetItems.get(position).getAction_info());
                        rv.setViewVisibility(R.id.action_contact, View.VISIBLE);
                        rv.setImageViewResource(R.id.action_contact, R.drawable.ic_action_communication_messenger);
                        rv.setOnClickFillInIntent(R.id.action_contact, sms_intent);
                        break;
                    case Costants.ACTION_MAIL:
                        Intent mail_intent = new Intent(Intent.ACTION_VIEW).putExtra(Costants.EXTRA_ACTION_TYPE, "mailto:" + mWidgetItems.get(position).getAction_info());
                        rv.setViewVisibility(R.id.action_contact, View.VISIBLE);
                        rv.setImageViewResource(R.id.action_contact, R.drawable.ic_action_communication_email);
                        rv.setOnClickFillInIntent(R.id.action_contact, mail_intent);
                        break;
                }
            } else {
                rv.setViewVisibility(R.id.action_contact, View.GONE);
            }

            return rv;
        } else {
            return null;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
*/