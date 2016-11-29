package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.List;

/**
 * Created by lxs on 2016/11/20.
 */
public class InsepectListAdapter extends RecyclerView.Adapter<InspectListHolder> {
    private List<InspectObject> inspectList;

    public InsepectListAdapter(List<InspectObject> list)
    {
        this.inspectList=list;
    }

    @Override
    public InspectListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.recycleritem_inspect,null);
        InspectListHolder holder=new InspectListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(InspectListHolder holder, int position) {
        holder.setMyObject(inspectList.get(position));
    }

    @Override
    public int getItemCount() {
        return inspectList.size();
    }
}
