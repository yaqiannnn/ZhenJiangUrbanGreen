package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectListHolder;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectObject;

import java.util.List;

/**
 * Created by lxs on 2016/11/28.
 */
public class MaintainListAdapter extends RecyclerView.Adapter<MaintainListHolder> {
    private List<MaintainObject> maintainList;

    public MaintainListAdapter(List<MaintainObject> list)
    {
        this.maintainList=list;
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
}
