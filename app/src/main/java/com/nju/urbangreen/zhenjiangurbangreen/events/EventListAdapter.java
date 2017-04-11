package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;
import com.nju.urbangreen.zhenjiangurbangreen.util.UrbanGreenDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/12/18.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> implements Filterable{

//    public static final int ITEM_TYPE_CONTENT = 0;
//    public static final int ITEM_TYPE_BOTTOM = 1;

    private int position;//用来记录事件活动当前处在哪个fragment上
    private List<OneEvent> currList;//实际显示的数据列表

    private EventsFilter eventsFilter;

    public EventListAdapter(Context context, int position, List<OneEvent> list) {
        this.position = position;
        this.currList=list;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if(viewType == ITEM_TYPE_BOTTOM){
//            return new FootViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_footer,parent,false));
//        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_fragment_list_item2,parent,false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.setEventData(currList.get(position));
    }


    @Override
    public int getItemCount() {
        return currList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(position >= originData.size()){
//            return ITEM_TYPE_BOTTOM;
//        }else {
//            return ITEM_TYPE_CONTENT;
//        }
//    }

    @Override
    public Filter getFilter() {
        if(this.eventsFilter == null){
            this.eventsFilter = new EventsFilter(this,currList);
        }
        return eventsFilter;
    }

    public void updateDataFromDB()
    {
        //从数据库查询得到最新的事件列表
        List<OneEvent> updateList=new ArrayList<>(UrbanGreenDB.getInstance(MyApplication.getContext())
                .loadEventsWithDiffState(position));
        //更新源事件列表
        this.eventsFilter.refreshOriginList(updateList);
    }

    class EventsFilter extends Filter {
        private final EventListAdapter m_adapter;
        private List<OneEvent> m_originList;//源数据，所有的事件
        private List<OneEvent> m_filteredList;//过滤后返回的事件列表

        public EventsFilter(EventListAdapter adapter,List<OneEvent> originList) {
            this.m_adapter=adapter;
            this.m_originList=new ArrayList<>(originList);
            this.m_filteredList=new ArrayList<>();
        }
        public void refreshOriginList(List<OneEvent> originList) {
            this.m_originList=new ArrayList<>(originList);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 在程序实际的运行过程中，returnData和originData中的事件列表有可能和数据库中的列表不一致
            // 这是由于adapter中的事件列表和数据库中的事件列表不一致造成的
            // 因此，需要从数据库更新一下originData的数据
            updateDataFromDB();

            String filterStr=constraint.toString().toLowerCase().trim();
            FilterResults results = new FilterResults();
            m_filteredList.clear();
            //如果查询内容为空，就直接返回originData
            if(TextUtils.isEmpty(filterStr)){
                m_filteredList.addAll(m_originList);
            }else {
                for(OneEvent oneEvent:m_originList){
                    if(oneEvent.getCode().contains(filterStr) 
                        || oneEvent.getName().contains(filterStr))
                        m_filteredList.add(oneEvent);
                }
            }
            results.values = m_filteredList;
            results.count = m_filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            m_adapter.currList.clear();
            m_adapter.currList.addAll((ArrayList<OneEvent>)(filterResults.values));
            m_adapter.notifyDataSetChanged();
        }
    }
}
