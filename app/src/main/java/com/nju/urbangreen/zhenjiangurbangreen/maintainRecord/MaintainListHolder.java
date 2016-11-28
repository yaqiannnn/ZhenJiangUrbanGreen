package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectInfoActivity;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectObject;

import java.text.SimpleDateFormat;

/**
 * Created by lxs on 2016/11/28.
 */
public class MaintainListHolder extends RecyclerView.ViewHolder{
    private CardView mCardView;
    private MaintainObject myObject;
    public MaintainListHolder(final View itemView)
    {
        super(itemView);
        mCardView=(CardView)itemView.findViewById(R.id.recyclerItem_maintainInfoCard);
        AppCompatButton btnInspectInfo=(AppCompatButton)itemView.findViewById(R.id.recyclerItem_btn_maintainInfo);
        btnInspectInfo.setOnClickListener(new View.OnClickListener() {
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

    public void setMyObject(MaintainObject maintainInfo)
    {
        this.myObject=maintainInfo;
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_maintainID))
                .setText(myObject.getID());
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_maintainType))
                .setText(myObject.getMaintainType());
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_maintainDate))
                .setText(new SimpleDateFormat("yyyy-MM-dd").format(myObject.getMaintainDate()));
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_maintainCompanyID))
                .setText(myObject.getCompanyID());
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_maintainStaff))
                .setText(myObject.getMaintainStaff());
    }
}
