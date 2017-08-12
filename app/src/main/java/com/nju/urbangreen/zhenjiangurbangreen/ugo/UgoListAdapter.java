package com.nju.urbangreen.zhenjiangurbangreen.ugo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;

import java.util.List;

/**
 * Created by tommy on 2017/8/9.
 */

public class UgoListAdapter extends RecyclerView.Adapter<UgoListAdapter.ViewHolder> {
    private List<GreenObject> mUgoList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ugo_list_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        final ImageView tickImage = holder.tickImage;
        tickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                GreenObject greenObject = mUgoList.get(position);
                Toast.makeText(view.getContext(), "you select "+greenObject.UGO_Address, Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GreenObject ugObject = mUgoList.get(position);
        holder.ugoName.setText(ugObject.UGO_Name);
        holder.ugoAddress.setText(ugObject.UGO_Address);
    }

    @Override
    public int getItemCount() {
        return mUgoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ugoName;
        TextView ugoAddress;
        ImageView tickImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ugoName=(TextView)itemView.findViewById(R.id.ugo_name);
            ugoAddress=(TextView)itemView.findViewById(R.id.ugo_address);
            tickImage=(ImageView)itemView.findViewById(R.id.img_tick);
        }
    }

    public UgoListAdapter(List<GreenObject> ugObjectList){
        mUgoList = ugObjectList;
    }
}
