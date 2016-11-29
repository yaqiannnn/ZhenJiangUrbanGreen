package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/11/23.
 */
public class EventListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout refreshLayout;
    private ListView lvEventList;
    private static final String ARG_POSITION = "position";
    private int position;

    private List<OneEvent> eventList = new ArrayList<OneEvent>();
    private EventAdapter eventAdapter;

    /**
     *下面两行是暂时用来测试listview的list数据
     */
    private ArrayList<String> eventArrayList = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;

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
        initEventList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list,container,false);
        lvEventList = (ListView) view.findViewById(R.id.lv_event_list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ly_refresh_events);
        //mAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,getData(position));

        eventAdapter = new EventAdapter(getContext(),R.layout.event_list_item,eventList);

        lvEventList.setAdapter(eventAdapter);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,android.R.color.holo_green_light,
                android.R.color.holo_orange_light,android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);
        return view;
//        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//
//        FrameLayout lyFrm = new FrameLayout(getActivity());
//
//        lyFrm.setLayoutParams(params);
//        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8, getResources().getDisplayMetrics());
//
//        TextView v = new TextView(getActivity());
//        params.setMargins(margin,margin,margin,margin);
//        v.setLayoutParams(params);
//        v.setGravity(Gravity.CENTER);
//        v.setBackgroundResource(R.drawable.background_tab);
//        v.setText("CARD " + (position + 1));
//        lyFrm.addView(v);
//
//        return lyFrm;
    }

    @Override
    public void onRefresh() {
        OneEvent oneEvent = new OneEvent("月黑风高","李白","长安","701年");
        eventList.add(oneEvent);
        eventAdapter.notifyDataSetChanged();
        //eventArrayList.add("card x");
        //mAdapter.notifyDataSetChanged();

        refreshLayout.setRefreshing(false);
    }

    private ArrayList<String> getData(int position){
        eventArrayList.add("card " + position);
        return eventArrayList;
    }

    private void initEventList(){
        OneEvent oneEvent = new OneEvent("暴走大事件","苏轼","眉山","1037年1月8号");
        eventList.add(oneEvent);
    }

}
