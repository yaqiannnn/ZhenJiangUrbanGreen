package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by HCQIN on 2016/11/23.
 */
public class EventListFragment extends Fragment{

    private static final String ARG_POSITION = "position";
    private int position;

    public static EventListFragment newInstance(int position){
        EventListFragment f = new EventListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION,position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout lyFrm = new FrameLayout(getActivity());

        lyFrm.setLayoutParams(params);
        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8, getResources().getDisplayMetrics());

        TextView v = new TextView(getActivity());
        params.setMargins(margin,margin,margin,margin);
        v.setLayoutParams(params);
        v.setGravity(Gravity.CENTER);
        v.setBackgroundResource(R.drawable.background_tab);
        v.setText("CARD " + (position + 1));
        lyFrm.addView(v);

        return lyFrm;
    }
}
