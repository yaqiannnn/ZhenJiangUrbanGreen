package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

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
import com.nju.urbangreen.zhenjiangurbangreen.events.OneEvent;
import com.nju.urbangreen.zhenjiangurbangreen.util.UrbanGreenDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2017/4/14.
 */
public class InspectListFragment extends Fragment{
    private SwipeRefreshLayout refreshLayout;//刷新布局
    private RecyclerView rcyvInspectList;
    private static final String ARG_POSITION = "position";
    private int position;//记录当前所在页面的位置

    private List<OneEvent> inspectList = new ArrayList<>();

    private BaseListAdapter inspectListAdapter;

    /**
     *下面两行是暂时用来测试listview的list数据
     */
    private ArrayList<String> inspectArrayList = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;

    //用以获得一个事件列表碎片的实例
    public static InspectListFragment newInstance(int position){
        InspectListFragment f = new InspectListFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION,position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        getData(position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspect_list,container,false);
        rcyvInspectList = (RecyclerView) view.findViewById(R.id.recycler_inspect_list);
        inspectListAdapter = new BaseListAdapter(getContext(),inspectList,position) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.inspect_fragment_list_item;
            }

            @Override
            public void bindData(BaseRecordViewHolder holder, int position, OneEvent item) {
                holder.setText(R.id.tv_one_insepct_name,item.getUGE_Name());
                holder.setText(R.id.tv_one_insepct_registrar,item.getRegistrar());
                holder.setText(R.id.tv_one_insepct_location,item.getUGE_Location());
                holder.setText(R.id.tv_one_insepct_date_time,item.getUGE_Time().toString());
            }
        };

        rcyvInspectList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcyvInspectList.setAdapter(inspectListAdapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ly_refresh_inspect);
        refreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorEmptyWarning,
                R.color.colorPrimaryDark,R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(((InspectListActivity)getActivity()).getSearchView().getVisibility() == View.VISIBLE){
                    refreshLayout.setRefreshing(false);
                    return;
                }
                UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(getContext());

                /**
                 * 选择数据库中最新的一条记录来更新，因为如果更改eventList的指向，更新就失效了，对比下面注释的代码
                 */

                int listSize = inspectList.size();
                List<OneEvent> tempList = urbanGreenDB.loadEventsWithDiffState(position);
                for(int i = 0;i < listSize;i++){
                    inspectList.remove(inspectList.size()-1);

                }
                Log.i("碎片", "onRefresh: "+ tempList.size() + "eventlist size" + listSize);

                for(int i = 0;i < tempList.size();i++){
                    inspectList.add(tempList.get(i));
                }

                //eventAdapter.notifyDataSetChanged();
                inspectListAdapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    //可以根据position可以相应的获取不同的事件列表
    private void getData(int position){
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(getContext());
        inspectList = urbanGreenDB.loadEventsWithDiffState(position);
    }

    public RecyclerView getRcyvInspectList(){
        return rcyvInspectList;
    }
}
