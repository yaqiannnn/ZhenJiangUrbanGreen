package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectListHolder;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxs on 2016/11/28.
 */
public class MaintainListAdapter extends RecyclerView.Adapter<MaintainListHolder> implements Filterable{
    private List<MaintainObject> maintainList;
    private MaintainFilter maintainFilter;

    public MaintainListAdapter(List<MaintainObject> list)
    {
        this.maintainList=list;
        this.maintainFilter=new MaintainFilter(this,maintainList);
    }

    @Override
    public MaintainListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.recycleritem_maintain,null);
        MaintainListHolder holder=new MaintainListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MaintainListHolder holder, int position) {
        holder.setMyObject(maintainList.get(position));
    }

    @Override
    public int getItemCount() {
        return maintainList.size();
    }

    @Override
    public Filter getFilter()
    {
        return this.maintainFilter;
    }

    private static class MaintainFilter extends Filter
    {
        private final MaintainListAdapter m_adapter;
        private final List<MaintainObject> m_originList;
        private final List<MaintainObject> m_filteredList;

        public MaintainFilter(MaintainListAdapter adapter,List<MaintainObject> originList)
        {
            this.m_adapter=adapter;
            this.m_originList=new ArrayList<>(originList);
            this.m_filteredList=new ArrayList<>();
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            m_filteredList.clear();
            String filterStr=constraint.toString().toLowerCase().trim();
            FilterResults results=new FilterResults();
            if(filterStr.equals(""))
                m_filteredList.addAll(m_originList);
            else {
                for(MaintainObject object:m_originList)
                {
                    if(object.getID().contains(filterStr))
                        m_filteredList.add(object);
                }
            }
            results.values=m_filteredList;
            results.count=m_filteredList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            m_adapter.maintainList.clear();
            m_adapter.maintainList.addAll((ArrayList<MaintainObject>)results.values);
            m_adapter.notifyDataSetChanged();
        }
    }
}
