package com.nego.nightmode.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

        public LinearLayout item;
        public TextView title;
        public TextView subtitle;
        public ImageView icon;
        public ViewHolder(View v, LinearLayout item, TextView title, TextView subtitle, ImageView icon) {
            super(v);
            mView = v;
            this.item = item;
            this.title = title;
            this.subtitle = subtitle;
            this.icon = icon;
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
                (LinearLayout) v.findViewById(R.id.item),
                (TextView) v.findViewById(R.id.title),
                (TextView) v.findViewById(R.id.subtitle),
                (ImageView) v.findViewById(R.id.icon));

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Mode m = mDataset.get(position);

        holder.title.setText(Utils.getModeName(mContext, m.getName()));
        holder.subtitle.setText(Utils.getDate(mContext, m.getLast_activation()));
        holder.icon.setImageResource(Utils.getModeIcon(m.getIcon()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // GENERATE LIST
    public void generate_list(DbAdapter dbHelper) {
        mDataset.clear();
        Cursor c = dbHelper.fetchAllModes();
        while (c.moveToNext())
            mDataset.add(new Mode(c));
        c.close();
    }
}