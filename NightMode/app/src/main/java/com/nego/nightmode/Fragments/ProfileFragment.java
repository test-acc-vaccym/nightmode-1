package com.nego.nightmode.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nego.nightmode.Costants;
import com.nego.nightmode.Functions.NMToggle;
import com.nego.nightmode.Mode;
import com.nego.nightmode.R;
import com.nego.nightmode.Utils;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.item_layout, container, false);
        final Mode m = getArguments().getParcelable(Costants.MODE_EXTRA);

        if (m != null) {
            // TITLE
            ((TextView) view.findViewById(R.id.title)).setText(getString(R.string.text_mode, Utils.getModeName(getActivity(), m.getName())));
            // LAST ACTIVATION
            ((TextView) view.findViewById(R.id.last_activation)).setText(Utils.getDate(getActivity(), m.getLast_activation()));
            // DEF
            //((TextView) view.findViewById(R.id.definition)).setText(m.getDef());
            //ICON
            ((ImageView) view.findViewById(R.id.icon)).setImageResource(Utils.getModeIcon(m.getIcon()));

            // BACKGROUND COLOR
            //view.findViewById(R.id.background_toolbar).setBackgroundColor(ContextCompat.getColor(getActivity(), Utils.getModeColor(m.getColor())));

            // ENABLE BUTTON
            view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NMToggle.startAction(getActivity(), m);
                }
            });
        }

        return view;
    }

    public static ProfileFragment newInstance(Mode m) {

        Bundle args = new Bundle();
        args.putParcelable(Costants.MODE_EXTRA, m);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }


}