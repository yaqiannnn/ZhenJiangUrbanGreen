package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.UrbanGreenDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/11/23.
 */
public class EventListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout refreshLayout;//刷新布局
    private ListView lvEventList;//事件记录列表布局
    private RecyclerView rcyvEventList;
    private static final String ARG_POSITION = "position";
    private int position;//记录当前所在页面的位置

    private List<OneEvent> eventList = new ArrayList<OneEvent>();

    //private EventAdapter eventAdapter;
    private EventListAdapter eventListAdapter;


    /**
     *下面两行是暂时用来测试listview的list数据
     */
    private ArrayList<String> eventArrayList = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;

    //用以获得一个事件列表碎片的实例
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
        getData(position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_fragment_event_list,container,false);
        //lvEventList = (ListView) view.findViewById(R.id.lv_event_list);
        //lvEventList.setTextFilterEnabled(true);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ly_refresh_events);
        rcyvEventList = (RecyclerView) view.findViewById(R.id.rcyv_event_list);
        //eventAdapter = new EventAdapter(getContext(),R.layout.event_fragment_list_item,eventList);
        eventListAdapter = new EventListAdapter(getActivity(),position,eventList);
        //lvEventList.setAdapter(eventAdapter);

        rcyvEventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcyvEventList.setAdapter(eventListAdapter);

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,android.R.color.holo_green_light,
                android.R.color.holo_orange_light,android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);
        return view;

    }

    @Override
    public void onRefresh() {
        if(((EventsActivity)getActivity()).getSearchView().getVisibility() == View.VISIBLE){
            refreshLayout.setRefreshing(false);
            return;
        }
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(getContext());

        /**
         * 选择数据库中最新的一条记录来更新，因为如果更改eventList的指向，更新就失效了，对比下面注释的代码
         */

        int listSize = eventList.size();
        List<OneEvent> tempList = urbanGreenDB.loadEventsWithDiffState(position);
        for(int i = 0;i < listSize;i++){
            eventList.remove(eventList.size()-1);

        }
        Log.i("碎片", "onRefresh: "+ tempList.size() + "eventlist size" + listSize);

        for(int i = 0;i < tempList.size();i++){
            eventList.add(tempList.get(i));
        }

        //eventAdapter.notifyDataSetChanged();
        eventListAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    //可以根据position可以相应的获取不同的事件列表
    private void getData(int position){
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(getContext());
        eventList = urbanGreenDB.loadEventsWithDiffState(position);



    }

    public ListView getLvEventList(){
        return lvEventList;
    }

    public RecyclerView getRcyvEventList(){
        return rcyvEventList;
    }
}
