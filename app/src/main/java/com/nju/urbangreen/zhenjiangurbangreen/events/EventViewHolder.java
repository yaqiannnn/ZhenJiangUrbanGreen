package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.text.SimpleDateFormat;

/**
 * Created by lxs on 2017/1/14.
 */
public class EventViewHolder extends RecyclerView.ViewHolder{
    private TextView eventName;
    private TextView eventRegistrar;
    private TextView eventLocation;
    private TextView eventDate;
    private ImageButton imgbtnEventDetail;
    private ImageButton imgbtnEventLocation;
    private OneEvent myObject;

    public EventViewHolder(View itemView) {
        super(itemView);

        eventName = (TextView) itemView.findViewById(R.id.tv_one_event_name);
        eventRegistrar = (TextView) itemView.findViewById(R.id.tv_one_event_registrar);
        eventLocation = (TextView) itemView.findViewById(R.id.tv_one_event_location);
        eventDate = (TextView) itemView.findViewById(R.id.tv_one_event_date_time);

        imgbtnEventDetail = (ImageButton) itemView.findViewById(R.id.imgbtn_event_detail);
        imgbtnEventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),EventRegisterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event",myObject);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
        imgbtnEventLocation = (ImageButton) itemView.findViewById(R.id.imgbtn_event_location);
        imgbtnEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setEventData(OneEvent oneEvent)
    {
        this.myObject=oneEvent;
        eventName.setText(myObject.getName());
        eventDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(myObject.getDate_time()));
        eventLocation.setText(myObject.getLocation());
        eventRegistrar.setText(myObject.getRegistrar());
    }
}
//class FootViewHolder extends RecyclerView.ViewHolder{
//
//    public FootViewHolder(View itemView) {
//        super(itemView);
//    }
//}
