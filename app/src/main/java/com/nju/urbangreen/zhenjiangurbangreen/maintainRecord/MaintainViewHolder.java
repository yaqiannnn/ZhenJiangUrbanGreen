package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.text.SimpleDateFormat;

/**
 * Created by lxs on 2016/11/28.
 */
public class MaintainViewHolder extends RecyclerView.ViewHolder{
    private AppCompatButton btnMaintainInfo;
    private AppCompatTextView maintainID;
    private AppCompatTextView maintainType;
    private AppCompatTextView maintainDate;
    private AppCompatTextView maintainStaff;
    private Maintain myObject;
    public MaintainViewHolder(final View itemView)
    {
        super(itemView);
        maintainID =(AppCompatTextView) itemView.findViewById(R.id.tv_recyclerItem_maintainID);
        maintainType =(AppCompatTextView)itemView.findViewById(R.id.tv_recyclerItem_maintainType);
        maintainDate =(AppCompatTextView)itemView.findViewById(R.id.tv_recyclerItem_maintainDate);
        maintainStaff =(AppCompatTextView)itemView.findViewById(R.id.tv_recyclerItem_maintainStaff);
        btnMaintainInfo=(AppCompatButton)itemView.findViewById(R.id.btn_recyclerItem_maintainInfo);
        btnMaintainInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),MaintainInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("MaintainInfo",myObject);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void setMaintainData(Maintain maintainInfo)
    {
        this.myObject=maintainInfo;
//        maintainID.setText(myObject.getID());
//        maintainType.setText(myObject.getMaintainType());
//        maintainDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(myObject.getMaintainDate()));
//        maintainStaff.setText(myObject.getMaintainStaff());
    }
}
