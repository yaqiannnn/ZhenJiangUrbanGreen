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
import com.nju.urbangreen.zhenjiangurbangreen.util.UrbanGreenDB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/12/18.
 */
public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{

    public static final int ITEM_TYPE_CONTENT = 0;
    public static final int ITEM_TYPE_BOTTOM = 1;

    private Context context;
    private int position;//用来记录事件活动当前处在哪个fragment上
    private List<OneEvent> originData;//相当于一个指针，指向了adapter中展示的事件列表
    private List<OneEvent> returnData;//过滤后返回的时间列表
    private List<OneEvent> backupData;//用来保存事件列表

    private EventsFilter eventsFilter;

    public EventListAdapter(Context context, int position, List<OneEvent> objects) {
        this.position = position;
        this.context = context;
        originData = objects;//指向adapter中展示的事件列表
        returnData = objects;//指向adapter中展示的事件列表
        //backupData = new ArrayList<OneEvent>(objects);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE_BOTTOM){
            return new FootViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_footer,parent,false));
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_fragment_list_item2,parent,false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof FootViewHolder){
            return;
        }
        final OneEvent oneEvent = originData.get(position);
        ((EventViewHolder)holder).eventName.setText("事件名称: " + oneEvent.getName());
        ((EventViewHolder)holder).eventRegistrar.setText("登记人员: " + oneEvent.getRegistrar());
        ((EventViewHolder)holder).eventLocation.setText("事件位置: " + oneEvent.getLocation());
        ((EventViewHolder)holder).eventDT.setText("发生时间: " + oneEvent.getDate_time());

        ((EventViewHolder)holder).imgbtnEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((EventViewHolder)holder).imgbtnEventDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EventRegisterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event",oneEvent);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return originData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position >= originData.size()){
            return ITEM_TYPE_BOTTOM;
        }else {
            return ITEM_TYPE_CONTENT;
        }

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

    class FootViewHolder extends RecyclerView.ViewHolder{

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    class EventsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            //在程序实际的运行过程中，returnData和originData中的事件列表有可能和数据库中的列表不一致，这是由于adapter中的事件列表和数据库中的事件列表不一致造成的
            // 因此，需要从数据库更新一下originData的数据
            int size = returnData.size();
            for(int i = 0;i < size;i++){
                originData.remove(returnData.size()-1);
            }
            //从数据库查询得到最新的事件列表
            backupData = new ArrayList<OneEvent>(UrbanGreenDB.getInstance(context).loadEventsWithDiffState(position));
            //更新originData所指向的adapter的事件列表
            for(int i = 0;i < backupData.size();i++){
                originData.add(backupData.get(i));
            }
            FilterResults results = new FilterResults();
            List<OneEvent> list;
            //如果查询内容为空，就直接返回originData
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

            List<OneEvent> backupList = new ArrayList<OneEvent>((ArrayList<OneEvent>)(filterResults.values));
            //将returnData中的数据清空（此时originData中的数据其实也被清空了，因为他们指向的地址是相同的）
            int size = returnData.size();
            for(int i = 0;i < size;i++){
                returnData.remove(returnData.size()-1);
            }
            //把performFiltering过滤后的数据添加进去
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
