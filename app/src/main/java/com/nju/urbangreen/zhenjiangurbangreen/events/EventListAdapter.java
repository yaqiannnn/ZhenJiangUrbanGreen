package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Context;
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
import com.nju.urbangreen.zhenjiangurbangreen.util.UrbanGreenDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/12/18.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> implements Filterable{

    private Context context;
    private int position;
    private List<OneEvent> originData;
    private List<OneEvent> returnData;
    private List<OneEvent> backupData;

    private EventsFilter eventsFilter;

    public EventListAdapter(Context context, int position, List<OneEvent> objects) {
        this.position = position;
        this.context = context;
        originData = objects;
        returnData = objects;
        //backupData = new ArrayList<OneEvent>(objects);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_fragment_list_item2,parent,false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        OneEvent oneEvent = originData.get(position);
        holder.eventName.setText("事件名称: " + oneEvent.getName());
        holder.eventRegistrar.setText("登记人员: " + oneEvent.getRegistrar());
        holder.eventLocation.setText("事件位置: " + oneEvent.getLocation());
        holder.eventDT.setText("发生时间: " + oneEvent.getDate_time());

        holder.imgbtnEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.imgbtnEventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return originData.size();
    }

    @Override
    public Filter getFilter() {
        if(eventsFilter == null){
            eventsFilter = new EventsFilter();
        }
        return eventsFilter;
    }

    class EventViewHolder extends RecyclerView.ViewHolder{

        TextView eventName;
        TextView eventRegistrar;
        TextView eventLocation;
        TextView eventDT;
        ImageButton imgbtnEventDetail;
        ImageButton imgbtnEventLocation;

        public EventViewHolder(View itemView) {
            super(itemView);

            eventName = (TextView) itemView.findViewById(R.id.tv_one_event_name);
            eventRegistrar = (TextView) itemView.findViewById(R.id.tv_one_event_registrar);
            eventLocation = (TextView) itemView.findViewById(R.id.tv_one_event_location);
            eventDT = (TextView) itemView.findViewById(R.id.tv_one_event_date_time);

            imgbtnEventDetail = (ImageButton) itemView.findViewById(R.id.imgbtn_event_detail);
            imgbtnEventLocation = (ImageButton) itemView.findViewById(R.id.imgbtn_event_location);
        }
    }

    class EventsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            int size = returnData.size();
            for(int i = 0;i < size;i++){
                originData.remove(returnData.size()-1);
            }
            backupData = new ArrayList<OneEvent>(UrbanGreenDB.getInstance(context).loadEventsWithDiffState(position));
            for(int i = 0;i < backupData.size();i++){
                originData.add(backupData.get(i));
            }
            FilterResults results = new FilterResults();
            List<OneEvent> list;
            if(TextUtils.isEmpty(charSequence)){
                list = originData;
            }else {
                list = new ArrayList<OneEvent>();
                for(OneEvent oneEvent:originData){
                    if(oneEvent.getCode().contains(charSequence) || oneEvent.getName().contains(charSequence)){
                        list.add(oneEvent);
                    }
                }
            }
            results.values = list;
            results.count = list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            //returnData.clear();
            List<OneEvent> backupList = new ArrayList<OneEvent>((ArrayList<OneEvent>)(filterResults.values));
            int size = returnData.size();
            for(int i = 0;i < size;i++){
                returnData.remove(returnData.size()-1);
            }
            for(int i = 0;i < backupList.size();i++){
                returnData.add(backupList.get(i));
            }

            if(filterResults.count >= 0){
                notifyDataSetChanged();
            }else {

            }
        }
    }
}
