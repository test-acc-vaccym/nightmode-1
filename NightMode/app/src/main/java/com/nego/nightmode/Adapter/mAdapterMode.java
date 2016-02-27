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

        public CardView item;
        public TextView title;
        public TextView last_activation;
        public ImageView icon;
        public LinearLayout background;
        public ViewHolder(View v, CardView item, TextView title, TextView last_activation, ImageView icon, LinearLayout background) {
            super(v);
            mView = v;
            this.item = item;
            this.title = title;
            this.last_activation = last_activation;
            this.icon = icon;
            this.background = background;
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
                (CardView) v.findViewById(R.id.item),
                (TextView) v.findViewById(R.id.title),
                (TextView) v.findViewById(R.id.last_activation),
                (ImageView) v.findViewById(R.id.icon),
                (LinearLayout) v.findViewById(R.id.background_toolbar));

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Mode m = mDataset.get(position);

        // TITLE
        holder.title.setText(m.getName(mContext));

        // LAST ACTIVATION
        holder.last_activation.setText(mContext.getString(R.string.start_time_enabled, Utils.getDate(mContext, m.getLast_activation())));
        holder.icon.setImageResource(m.getIcon());
        holder.background.setBackgroundColor(ContextCompat.getColor(mContext, m.getColor()));

        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main) mContext).collapseBS();
                NMToggle.startAction(mContext, m);
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