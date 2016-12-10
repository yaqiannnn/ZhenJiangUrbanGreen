package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private static final String ARG_POSITION = "position";
    private int position;//记录当前所在页面的位置

    private List<OneEvent> eventList = new ArrayList<OneEvent>();
    private EventAdapter eventAdapter;

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
        lvEventList = (ListView) view.findViewById(R.id.lv_event_list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ly_refresh_events);
        //mAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,getData(position));
        //eventList = getData(position);
        eventAdapter = new EventAdapter(getContext(),R.layout.event_fragment_list_item,eventList);

        lvEventList.setAdapter(eventAdapter);
//        lvEventList.getTextFilter();
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark,android.R.color.holo_green_light,
                android.R.color.holo_orange_light,android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);
        return view;

    }

    @Override
    public void onRefresh() {
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(getContext());

        /**
         * 选择数据库中最新的一条记录来更新，因为如果更改eventList的指向，更新就失效了，对比下面注释的代码
         */
        //OneEvent oneEvent;
        int listSize = eventList.size();
        List<OneEvent> tempList = urbanGreenDB.loadEventsWithDiffState(position);
        for(int i = 0;i < listSize;i++){
            eventList.remove(eventList.size()-1);

        }
        Log.i("碎片", "onRefresh: "+ tempList.size() + "eventlist size" + listSize);
        //eventList.removeAll(eventList);
        for(int i = 0;i < tempList.size();i++){
            eventList.add(tempList.get(i));
        }
        //eventList.addAll(tempList);
//        oneEvent = tempList.get(tempList.size()-1);
//        eventList.add(oneEvent);
        //eventList = urbanGreenDB.loadEventsNotUpload(0);
        ////忽略这一行OneEvent oneEvent = new OneEvent("月黑风高","李白","长安","701年");
        //eventList.add(oneEvent);
        //eventList = urbanGreenDB.loadEventsWithDiffState(0);
        //Log.i("碎片", "onRefresh: "+ eventList.get(1).getName());
        eventAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    //可以根据position可以相应的获取不同的事件列表
    private void getData(int position){
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(getContext());
        eventList = urbanGreenDB.loadEventsWithDiffState(position);

        //eventArrayList.add("card " + position);

    }

    private void initEventList(){
        OneEvent oneEvent = new OneEvent("暴走大事件","苏轼","眉山","1037年1月8号");
        eventList.add(oneEvent);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("碎片","onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("碎片","onActivityCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("碎片","onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("碎片","onPause");
    }
}
