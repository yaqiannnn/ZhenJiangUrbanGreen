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

    private SwipeRefreshLayout refreshLayout;//刷新布局
    private ListView lvEventList;//事件记录列表布局
    private static final String ARG_POSITION = "position";
    private int position;//记录当前所在页面的位置

    private List<OneEvent> eventList = new ArrayList<OneEvent>();
    private EventAdapter eventAdapter;

    /**
     *下面两行是暂时用来测试listview的list数据
     */
    private ArrayList<String> eventArrayList = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;

    //用以获得一个时间列表碎片的实例
    public static EventListFragment newInstance(int position){
        EventListFragment f = new EventListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION,position);
        f.setArguments(b);
        return f;
    }

    //在创建碎片的时候，获取position的值
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

    }

    @Override
    public void onRefresh() {
        OneEvent oneEvent = new OneEvent("月黑风高","李白","长安","701年");
        eventList.add(oneEvent);
        eventAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    //可以根据position可以相应的获取不同的事件列表
    private ArrayList<String> getData(int position){
        eventArrayList.add("card " + position);
        return eventArrayList;
    }

    private void initEventList(){
        OneEvent oneEvent = new OneEvent("暴走大事件","苏轼","眉山","1037年1月8号");
        eventList.add(oneEvent);
    }

}
