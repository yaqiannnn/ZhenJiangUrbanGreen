package com.nju.urbangreen.zhenjiangurbangreen.ugo;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.map.SimpleMapActivity;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by tommy on 2017/8/9.
 */

public class UgoListAdapter extends RecyclerView.Adapter<UgoListAdapter.ViewHolder> implements View.OnClickListener {
    private List<GreenObject> mUgoList;
    private ViewHolder holder;

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);
        holder = new ViewHolder(view);
        holder.ugoView.setOnClickListener(this);
        holder.ugoIdName.setOnClickListener(this);
        holder.ugoAddress.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GreenObject ugObject = mUgoList.get(position);
        holder.ugoIdName.setText(ugObject.UGO_Ucode + "/" + ugObject.UGO_Name);
        holder.ugoAddress.setText(ugObject.UGO_Address);
    }

    @Override
    public int getItemCount() {
        return mUgoList.size();
    }

    @Override
    public void onClick(View view) {
        int position = holder.getAdapterPosition();
        GreenObject object = mUgoList.get(position);
        Intent intent = new Intent(view.getContext(), SimpleMapActivity.class);
        intent.putExtra("type", object.UGO_ClassType_ID);
        intent.putExtra("name", object.UGO_Name);
        intent.putExtra("location", object.UGO_Geo_Location);
        try {
            view.getContext().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View ugoView;
        EditText ugoIdName;
        EditText ugoAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ugoView = itemView;
            ugoIdName = (EditText) itemView.findViewById(R.id.recycler_title);
            ugoAddress = (EditText) itemView.findViewById(R.id.recycler_content);
        }
    }

    public UgoListAdapter(List<GreenObject> ugObjectList) {
        mUgoList = ugObjectList;
    }
}
