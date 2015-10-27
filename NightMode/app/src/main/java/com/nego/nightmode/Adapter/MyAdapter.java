package com.nego.nightmode.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nego.nightmode.Alarm;
import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.AlarmService;
import com.nego.nightmode.R;
import com.nego.nightmode.Utils;
import com.nego.nightmode.database.DbAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Alarm> mDataset = new ArrayList<>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }

        public RelativeLayout day_container;
        public TextView title_day;
        public TextView subtitle_day;
        public ViewHolder(View v, RelativeLayout day_container, TextView title_day, TextView subtitle_day) {
            super(v);
            mView = v;
            this.day_container = day_container;
            this.title_day = title_day;
            this.subtitle_day = subtitle_day;
        }

    }

    public MyAdapter(DbAdapter dbHelper, Context mContext) {
        this.mContext = mContext;
        generate_list(dbHelper);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        ViewHolder vh;
        View v;

        v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.day_layout, parent, false);
        vh = new ViewHolder(v,
                (RelativeLayout) v.findViewById(R.id.day_container),
                (TextView) v.findViewById(R.id.title_day),
                (TextView) v.findViewById(R.id.subtitle_day));

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        switch (position) {
            case 0:
                holder.title_day.setText(mContext.getString(R.string.text_saturday));
                break;
            case 1:
                holder.title_day.setText(mContext.getString(R.string.text_sunday));
                break;
            case 2:
                holder.title_day.setText(mContext.getString(R.string.text_monday));
                break;
            case 3:
                holder.title_day.setText(mContext.getString(R.string.text_tuesday));
                break;
            case 4:
                holder.title_day.setText(mContext.getString(R.string.text_wednesday));
                break;
            case 5:
                holder.title_day.setText(mContext.getString(R.string.text_thursday));
                break;
            case 6:
                holder.title_day.setText(mContext.getString(R.string.text_friday));
                break;
        }

        String subtitle = mContext.getString(R.string.text_no_alarm);
        if (mDataset.get(position).getStart() > 0) {
            subtitle = mContext.getString(R.string.text_from)
                    + " "
                    + Utils.getHour(mContext, mDataset.get(position).getStart())
                    + " "
                    + mContext.getString(R.string.text_to)
                    + " "
                    + Utils.getHour(mContext, mDataset.get(position).getEnd());
        }
        holder.subtitle_day.setText(subtitle);

        // CLICK LISTENER
        holder.day_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_day, null);
                final Dialog dialog_day = new Dialog(mContext, R.style.mDialog);

                final Calendar start = Calendar.getInstance();
                final Calendar end = Calendar.getInstance();

                if (mDataset.get(position).getStart() > 0) {
                    ((TextView) dialogView.findViewById(R.id.time_from)).setText(Utils.getHour(mContext, mDataset.get(position).getStart()));
                    ((TextView) dialogView.findViewById(R.id.time_to)).setText(Utils.getHour(mContext, mDataset.get(position).getEnd()));
                    start.setTimeInMillis(mDataset.get(position).getStart());
                    end.setTimeInMillis(mDataset.get(position).getStart());
                } else {
                    ((TextView) dialogView.findViewById(R.id.time_from)).setText("23:00");
                    ((TextView) dialogView.findViewById(R.id.time_to)).setText("08:00");
                    start.set(Calendar.HOUR_OF_DAY, 23);
                    start.set(Calendar.MINUTE, 0);
                    start.set(Calendar.SECOND, 0);
                    start.set(Calendar.MILLISECOND, 0);
                    start.set(Calendar.DAY_OF_WEEK, mDataset.get(position).getDay());
                    end.set(Calendar.HOUR_OF_DAY, 8);
                    end.set(Calendar.MINUTE, 0);
                    end.set(Calendar.SECOND, 0);
                    end.set(Calendar.MILLISECOND, 0);
                    end.set(Calendar.DAY_OF_WEEK, mDataset.get(position).getDay());
                    end.add(Calendar.DAY_OF_WEEK, 1);
                    mDataset.get(position).setStart(start.getTimeInMillis());
                    mDataset.get(position).setEnd(end.getTimeInMillis());
                }

                if ((start.get(Calendar.HOUR_OF_DAY) > end.get(Calendar.HOUR_OF_DAY)) || ((start.get(Calendar.HOUR_OF_DAY) == end.get(Calendar.HOUR_OF_DAY)) && (start.get(Calendar.MINUTE) > end.get(Calendar.MINUTE)))) {
                    dialogView.findViewById(R.id.day_after).setVisibility(View.VISIBLE);
                } else {
                    dialogView.findViewById(R.id.day_after).setVisibility(View.GONE);
                }

                dialogView.findViewById(R.id.action_from).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Context mContextPicker;
                        if (Utils.isBrokenSamsungDevice()) {
                            mContextPicker = new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog);
                        } else {
                            mContextPicker = mContext;
                        }
                        TimePickerDialog mTimePicker = new TimePickerDialog(mContextPicker, R.style.mDialog_Picker, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                start.set(Calendar.HOUR_OF_DAY, selectedHour);
                                start.set(Calendar.MINUTE, selectedMinute);
                                mDataset.get(position).setStart(start.getTimeInMillis());
                                if ((start.get(Calendar.HOUR_OF_DAY) > end.get(Calendar.HOUR_OF_DAY)) || ((start.get(Calendar.HOUR_OF_DAY) == end.get(Calendar.HOUR_OF_DAY)) && (start.get(Calendar.MINUTE) > end.get(Calendar.MINUTE)))) {
                                    dialogView.findViewById(R.id.day_after).setVisibility(View.VISIBLE);
                                } else {
                                    dialogView.findViewById(R.id.day_after).setVisibility(View.GONE);
                                }
                                ((TextView) dialogView.findViewById(R.id.time_from)).setText(Utils.getHour(mContextPicker, start.getTimeInMillis()));
                            }
                        }, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true);
                        mTimePicker.show();
                    }
                });

                dialogView.findViewById(R.id.action_to).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Context mContextPicker;
                        if (Utils.isBrokenSamsungDevice()) {
                            mContextPicker = new ContextThemeWrapper(mContext, android.R.style.Theme_Holo_Light_Dialog);
                        } else {
                            mContextPicker = mContext;
                        }
                        TimePickerDialog mTimePicker = new TimePickerDialog(mContextPicker, R.style.mDialog_Picker, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                end.set(Calendar.HOUR_OF_DAY, selectedHour);
                                end.set(Calendar.MINUTE, selectedMinute);
                                ((TextView) dialogView.findViewById(R.id.time_to)).setText(Utils.getHour(mContextPicker, end.getTimeInMillis()));
                                if ((start.get(Calendar.HOUR_OF_DAY) > end.get(Calendar.HOUR_OF_DAY)) || ((start.get(Calendar.HOUR_OF_DAY) == end.get(Calendar.HOUR_OF_DAY)) && (start.get(Calendar.MINUTE) > end.get(Calendar.MINUTE)))) {
                                    dialogView.findViewById(R.id.day_after).setVisibility(View.VISIBLE);
                                    end.set(Calendar.DAY_OF_WEEK, mDataset.get(position).getDay());
                                    end.add(Calendar.DAY_OF_WEEK, 1);
                                } else {
                                    dialogView.findViewById(R.id.day_after).setVisibility(View.GONE);
                                    end.set(Calendar.DAY_OF_WEEK, mDataset.get(position).getDay());
                                }
                                mDataset.get(position).setEnd(end.getTimeInMillis());
                            }
                        }, end.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true);
                        mTimePicker.show();
                    }
                });

                dialogView.findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(mContext)
                                .setTitle(mContext.getString(R.string.text_attention))
                                .setMessage(mContext.getString(R.string.text_ask_reset_alarm))
                                .setPositiveButton(R.string.action_reset, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        AlarmService.startAction(mContext, Costants.ACTION_DELETE, mDataset.get(position));
                                        dialog_day.dismiss();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
                });

                dialogView.findViewById(R.id.action_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmService.startAction(mContext, Costants.ACTION_CREATE, mDataset.get(position));
                        dialog_day.dismiss();
                    }
                });

                dialog_day.setContentView(dialogView);
                dialog_day.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // GENERATE LIST
    public void generate_list(DbAdapter dbHelper) {
        mDataset.clear();
        for (int i = 0; i < 7; i++) {
            Cursor cursor = dbHelper.getAlarmByDay(i);
            while (cursor.moveToNext()) {
                Alarm a = new Alarm(cursor);
                mDataset.add(a);
            }
            if (mDataset.size() <= i) {
                mDataset.add(new Alarm(i, 0, 0));
            }
            cursor.close();
        }
    }
}
