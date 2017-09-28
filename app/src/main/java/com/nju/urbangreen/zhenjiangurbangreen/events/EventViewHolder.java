package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.events.OneEvent;

/**
 * Created by ljy on 2017/9/27.
 */
public class EventViewHolder extends RecyclerView.ViewHolder{
    private AppCompatButton btnEventInfo;
    private AppCompatTextView eventID;
    private AppCompatTextView eventType;
    private AppCompatTextView eventDate;
    private OneEvent myObject;
    public EventViewHolder(final View itemView)
    {
        super(itemView);
        eventID =(AppCompatTextView) itemView.findViewById(R.id.tv_recyclerItem_eventID);
        eventType =(AppCompatTextView)itemView.findViewById(R.id.tv_recyclerItem_eventType);
        eventDate =(AppCompatTextView)itemView.findViewById(R.id.tv_recyclerItem_eventDate);
        btnEventInfo=(AppCompatButton)itemView.findViewById(R.id.btn_recyclerItem_eventInfo);
        btnEventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),EventRegisterActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("EventInfo",myObject);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void setEventData(OneEvent eventInfo)
    {
        this.myObject=eventInfo;
//        eventID.setText(myObject.getID());
//        eventType.setText(myObject.getEventType());
//        eventDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(myObject.getEventDate()));
    }
}
