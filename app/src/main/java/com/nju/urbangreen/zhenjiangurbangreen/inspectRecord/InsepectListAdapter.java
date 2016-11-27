package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.List;

/**
 * Created by lxs on 2016/11/24.
 */
public class InsepectListAdapter extends RecyclerView.Adapter<InspectInfoHolder> {
    private List<InspectObject> inspectList;

    public InsepectListAdapter(List<InspectObject> list)
    {
        this.inspectList=list;
    }

    @Override
    public InspectInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.recycleritem_inspect,null);
        InspectInfoHolder holder=new InspectInfoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(InspectInfoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return inspectList.size();
    }
}
