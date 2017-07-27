package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseListAdapter;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseRecordViewHolder;
import com.nju.urbangreen.zhenjiangurbangreen.util.UrbanGreenDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2017/4/13.
 */
public class EventListFragment extends Fragment {

    public RecyclerView rcyvEventList;
    public SwipeRefreshLayout refreshLayout;//刷新布局

    private static final String ARG_POSITION = "position";
    private int position;//记录当前所在页面的位置

    private List<OneEvent> eventList = new ArrayList<>();

    private BaseListAdapter eventListAdapter;


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
        View view = inflater.inflate(R.layout.fragment_event_list,container,false);
       rcyvEventList = (RecyclerView) view.findViewById(R.id.recycler_event_list);
        eventListAdapter = new BaseListAdapter(getContext(),eventList,position) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.event_fragment_list_item2;
            }

            @Override
            public void bindData(BaseRecordViewHolder holder, int position, OneEvent item) {
                holder.setText(R.id.tv_one_event_name,item.getName());
                holder.setText(R.id.tv_one_event_registrar,item.getRegistrar());
                holder.setText(R.id.tv_one_event_location,item.getLocation());
                holder.setText(R.id.tv_one_event_date_time,item.getDate_time().toString());
            }
        };

        rcyvEventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcyvEventList.setAdapter(eventListAdapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ly_refresh_events);
        refreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorEmptyWarning,
                R.color.colorPrimaryDark,R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(((EventListActivity)getActivity()).getSearchView().getVisibility() == View.VISIBLE){
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
        });
        return view;

    }

    //可以根据position可以相应的获取不同的事件列表
    private void getData(int position){
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(getContext());
        eventList = urbanGreenDB.loadEventsWithDiffState(position);
    }

    public RecyclerView getRcyvEventList(){
        return rcyvEventList;
    }
}
