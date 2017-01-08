package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/11/25.
 */
public class EventAdapter extends ArrayAdapter<OneEvent>{

    public int resourceId;
    private List<OneEvent> originData;
    private List<OneEvent> returnData;
    private List<OneEvent> backupData;
    private EventsFilter eventsFilter;

    public EventAdapter(Context context, int resource, List<OneEvent> objects) {
        super(context, resource, objects);
        resourceId = resource;
        originData = objects;
        backupData = new ArrayList<OneEvent>(objects);
        returnData = objects;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OneEvent oneEvent = getItem(position);

        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view =LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.eventName = (TextView) view.findViewById(R.id.tv_oneevent_name);
            viewHolder.eventRegistrar = (TextView) view.findViewById(R.id.tv_oneevent_registrar);
            viewHolder.eventLocation = (TextView) view.findViewById(R.id.tv_oneevent_location);
            viewHolder.eventDT = (TextView) view.findViewById(R.id.tv_oneevent_date_time);

            viewHolder.btnEventDetail = (Button) view.findViewById(R.id.btn_event_detail);
            viewHolder.btnEventLocation = (Button) view.findViewById(R.id.btn_event_location);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }


        viewHolder.eventName.setText("事件名称: " + oneEvent.getName());
        viewHolder.eventRegistrar.setText("登记人员: " + oneEvent.getRegistrar());
        viewHolder.eventLocation.setText("事件位置: " + oneEvent.getLocation());
        viewHolder.eventDT.setText("发生时间: " + oneEvent.getDate_time());

        viewHolder.btnEventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.btnEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public Filter getFilter() {
        if(eventsFilter == null){
            eventsFilter = new EventsFilter();
        }
        return eventsFilter;
    }

    class ViewHolder{
        TextView eventName;
        TextView eventRegistrar;
        TextView eventLocation;
        TextView eventDT;
        Button btnEventDetail;
        Button btnEventLocation;
    }

    class EventsFilter extends Filter{


        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            int size = returnData.size();
            for(int i = 0;i < size;i++){
                originData.remove(returnData.size()-1);
            }
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
            //returnData = (List<OneEvent>) (filterResults.values);
            //returnData = ;
            if(filterResults.count >= 0){
                notifyDataSetChanged();
            }else {
                notifyDataSetInvalidated();
            }
        }
    }
}
