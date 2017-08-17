package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.List;

/**
 * Created by tommy on 2017/8/17.
 */

public class MaintainListAdapter2 extends RecyclerView.Adapter<MaintainListAdapter2.ViewHolder> {
    private List<Maintain> maintainList;

    public MaintainListAdapter2(List<Maintain> maintainList) {
        this.maintainList = maintainList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintain_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Maintain maintain = maintainList.get(i);
        viewHolder.maintainCodeType.setText(maintain.MR_Code + "/" + maintain.MR_MaintainType);
        viewHolder.maintainContent.setText(maintain.MR_MaintainContent);
    }

    @Override
    public int getItemCount() {
        return maintainList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View maintainView;
        EditText maintainCodeType;
        EditText maintainContent;

        public ViewHolder(View itemView) {
            super(itemView);
            maintainView = itemView;
            maintainCodeType = (EditText) itemView.findViewById(R.id.maintain_code_type);
            maintainContent = (EditText) itemView.findViewById(R.id.maintain_content);
        }
    }
}
