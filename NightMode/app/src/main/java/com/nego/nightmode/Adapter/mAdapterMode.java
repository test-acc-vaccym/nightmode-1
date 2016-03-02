package com.nego.nightmode.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.NMToggle;
import com.nego.nightmode.Main;
import com.nego.nightmode.Mode;
import com.nego.nightmode.R;
import com.nego.nightmode.Utils;
import com.nego.nightmode.database.DbAdapter;

import java.util.ArrayList;
import java.util.List;


public class mAdapterMode extends RecyclerView.Adapter<mAdapterMode.ViewHolder> {
    private List<Mode> mDataset = new ArrayList<>();
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }

        public LinearLayout top_divider;
        public CardView item;
        public TextView title;
        public TextView last_activation;
        public ImageView icon;
        public LinearLayout item_to_click;
        public ImageView action_info;
        public ViewHolder(View v, LinearLayout top_divider, CardView item, TextView title, TextView last_activation, ImageView icon, LinearLayout item_to_click, ImageView action_info) {
            super(v);
            mView = v;
            this.top_divider = top_divider;
            this.item = item;
            this.title = title;
            this.last_activation = last_activation;
            this.icon = icon;
            this.item_to_click = item_to_click;
            this.action_info = action_info;
        }

    }

    public mAdapterMode(DbAdapter dbHelper, Context mContext) {
        this.mContext = mContext;
        generate_list(dbHelper);
    }

    @Override
    public mAdapterMode.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        ViewHolder vh;
        View v;

        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        vh = new ViewHolder(v,
                (LinearLayout) v.findViewById(R.id.top_divider),
                (CardView) v.findViewById(R.id.item),
                (TextView) v.findViewById(R.id.title),
                (TextView) v.findViewById(R.id.last_activation),
                (ImageView) v.findViewById(R.id.icon),
                (LinearLayout) v.findViewById(R.id.item_to_click),
                (ImageView) v.findViewById(R.id.action_info));

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Mode m = mDataset.get(position);

        if (position == 0) {
            holder.top_divider.setVisibility(View.VISIBLE);
        } else {
            holder.top_divider.setVisibility(View.GONE);
        }

        // TITLE
        holder.title.setText(m.getName(mContext));

        // LAST ACTIVATION
        holder.last_activation.setText(mContext.getString(R.string.start_time_enabled, Utils.getDate(mContext, m.getLast_activation())));
        holder.icon.setImageResource(m.getIcon());
        holder.item_to_click.setBackgroundColor(ContextCompat.getColor(mContext, m.getColor()));

        holder.item_to_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main) mContext).collapseBS();
                NMToggle.startAction(mContext, m);
            }
        });

        holder.action_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main) mContext).collapseBS();
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
        Cursor c = dbHelper.fetchAllModes();
        while (c.moveToNext()) {
            mDataset.add(new Mode(c));
        }
        c.close();
    }
}