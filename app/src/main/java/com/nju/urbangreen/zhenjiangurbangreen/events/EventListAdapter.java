package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxs on 2016/11/28.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> implements Filterable{
    private List<OneEvent> eventList;
    private EventFilter eventFilter;
    private int position;//用来记录事件活动当前处在哪个fragment上

    public EventListAdapter(List<OneEvent> list)
    {
        this.eventList=list;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.recycleritem_event,null);
        EventViewHolder holder=new EventViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.setEventData(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public Filter getFilter(){
        if(this.eventFilter == null){
            this.eventFilter = new EventFilter(this,eventList);
        }
        return this.eventFilter;
    }

    public void updateDataFromDB()
    {
        //从数据库查询得到最新的事件列表
//        List<OneEvent> updateList=new ArrayList<>(UrbanGreenDB.getInstance(MyApplication.getContext())
//                .loadEventsWithDiffState(this.position));
//        //更新源事件列表
//        this.eventFilter.freshOriginList(updateList);
    }

    public void updateDataFromWeb()
    {
        //todo get data from web
    }

    private class EventFilter extends Filter
    {
        private final EventListAdapter m_adapter;
        private List<OneEvent> m_originList;//源数据，所有的事件记录
        private List<OneEvent> m_filteredList;//过滤后返回的事件记录

        public EventFilter(EventListAdapter adapter,List<OneEvent> originList)
        {
            this.m_adapter=adapter;
            this.m_originList=new ArrayList<>(originList);
            this.m_filteredList=new ArrayList<>();
        }
        public void freshOriginList(List<OneEvent> originList)
        {
            this.m_originList=new ArrayList<>(originList);
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //从本地数据库获得最新的事件记录列表
            updateDataFromDB();

            String filterStr=constraint.toString().toLowerCase().trim();
            FilterResults results=new FilterResults();
            m_filteredList.clear();
            if(TextUtils.isEmpty(filterStr))
                m_filteredList.addAll(m_originList);
            else {
//                for(OneEvent object:m_originList)
//                {
//                    if(object.getID().contains(filterStr)
//                        || object.getCode().contains(filterStr))
//                        m_filteredList.add(object);
//                }
            }
            results.values=m_filteredList;
            results.count=m_filteredList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            m_adapter.eventList.clear();
            m_adapter.eventList.addAll((ArrayList<OneEvent>)results.values);
            m_adapter.notifyDataSetChanged();
        }
    }
}
