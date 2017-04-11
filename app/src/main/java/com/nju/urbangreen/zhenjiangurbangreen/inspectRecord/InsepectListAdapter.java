package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxs on 2016/11/20.
 */
public class InsepectListAdapter extends RecyclerView.Adapter<InspectViewHolder> implements Filterable{
    private List<InspectObject> inspectList;
    private InspectFilter inspectFilter;

    public InsepectListAdapter(List<InspectObject> list)
    {
        this.inspectList=list;
        this.inspectFilter=new InspectFilter(this,inspectList);
    }

    @Override
    public InspectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.recycleritem_inspect,null);
        InspectViewHolder holder=new InspectViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(InspectViewHolder holder, int position) {
        holder.setMyObject(inspectList.get(position));
    }

    @Override
    public int getItemCount() {
        return inspectList.size();
    }

    @Override
    public Filter getFilter()
    {
        return this.inspectFilter;
    }

    private static class InspectFilter extends Filter
    {
        private final InsepectListAdapter m_adapter;
        private final List<InspectObject> m_originList;
        private final List<InspectObject> m_filteredList;

        public InspectFilter(InsepectListAdapter adapter,List<InspectObject> originList)
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
                for(InspectObject object:m_originList)
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
            m_adapter.inspectList.clear();
            m_adapter.inspectList.addAll((ArrayList<InspectObject>)results.values);
            m_adapter.notifyDataSetChanged();
        }
    }
}


