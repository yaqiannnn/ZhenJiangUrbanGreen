package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.List;

/**
 * Created by Liwei on 2016/11/25.
 */
public class EventAdapter extends ArrayAdapter<OneEvent>{

    public int resourceId;

    public EventAdapter(Context context, int resource, List<OneEvent> objects) {
        super(context, resource, objects);
        resourceId = resource;
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

    class ViewHolder{
        TextView eventName;
        TextView eventRegistrar;
        TextView eventLocation;
        TextView eventDT;
        Button btnEventDetail;
        Button btnEventLocation;
    }
}
