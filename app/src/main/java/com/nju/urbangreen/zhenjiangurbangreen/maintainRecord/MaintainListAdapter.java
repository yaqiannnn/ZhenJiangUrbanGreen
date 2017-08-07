package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

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
public class MaintainListAdapter extends RecyclerView.Adapter<MaintainViewHolder> implements Filterable{
    private List<Maintain> maintainList;
    private MaintainFilter maintainFilter;
    private int position;//用来记录事件活动当前处在哪个fragment上

    public MaintainListAdapter(List<Maintain> list)
    {
        this.maintainList=list;
    }

    @Override
    public MaintainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.recycleritem_maintain,null);
        MaintainViewHolder holder=new MaintainViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MaintainViewHolder holder, int position) {
        holder.setMaintainData(maintainList.get(position));
    }

    @Override
    public int getItemCount() {
        return maintainList.size();
    }

    @Override
    public Filter getFilter(){
        if(this.maintainFilter == null){
            this.maintainFilter = new MaintainFilter(this,maintainList);
        }
        return this.maintainFilter;
    }

    public void updateDataFromDB()
    {
        //从数据库查询得到最新的事件列表
//        List<Maintain> updateList=new ArrayList<>(UrbanGreenDB.getInstance(MyApplication.getContext())
//                .loadEventsWithDiffState(this.position));
//        //更新源事件列表
//        this.maintainFilter.freshOriginList(updateList);
    }

    public void updateDataFromWeb()
    {
        //todo get data from web
    }

    private class MaintainFilter extends Filter
    {
        private final MaintainListAdapter m_adapter;
        private List<Maintain> m_originList;//源数据，所有的养护记录
        private List<Maintain> m_filteredList;//过滤后返回的养护记录

        public MaintainFilter(MaintainListAdapter adapter,List<Maintain> originList)
        {
            this.m_adapter=adapter;
            this.m_originList=new ArrayList<>(originList);
            this.m_filteredList=new ArrayList<>();
        }
        public void freshOriginList(List<Maintain> originList)
        {
            this.m_originList=new ArrayList<>(originList);
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //从本地数据库获得最新的养护记录列表
            updateDataFromDB();

            String filterStr=constraint.toString().toLowerCase().trim();
            FilterResults results=new FilterResults();
            m_filteredList.clear();
            if(TextUtils.isEmpty(filterStr))
                m_filteredList.addAll(m_originList);
            else {
//                for(Maintain object:m_originList)
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
            m_adapter.maintainList.clear();
            m_adapter.maintainList.addAll((ArrayList<Maintain>)results.values);
            m_adapter.notifyDataSetChanged();
        }
    }
}
