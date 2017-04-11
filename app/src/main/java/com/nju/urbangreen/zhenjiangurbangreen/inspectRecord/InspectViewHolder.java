package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.text.SimpleDateFormat;

/**
 * Created by lxs on 2016/11/20.
 */
public class InspectViewHolder extends RecyclerView.ViewHolder{
    private CardView mCardView;
    private InspectObject myObject;
    public InspectViewHolder(final View itemView)
    {
        super(itemView);
        mCardView=(CardView)itemView.findViewById(R.id.recyclerItem_inspectInfoCard);
        AppCompatButton btnInspectInfo=(AppCompatButton)itemView.findViewById(R.id.recyclerItem_btn_inspectInfo);
        btnInspectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),InspectInfoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("InspectInfo",myObject);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void setMyObject(InspectObject inspectInfo)
    {
        this.myObject=inspectInfo;
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_inspectID))
                .setText(myObject.getID());
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_inspectType))
                .setText(myObject.getInspectType());
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_inspectDate))
                .setText(new SimpleDateFormat("yyyy-MM-dd").format(myObject.getInspectDate()));
        ((AppCompatTextView)mCardView.findViewById(R.id.recyclerItem_inspectScore))
                .setText(myObject.getScore());
    }
}
