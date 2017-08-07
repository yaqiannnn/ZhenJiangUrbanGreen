package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObjects;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 2016/9/30.
 */
public class SearchResultHolder extends RecyclerView.ViewHolder{
    private UGObject obj;
    private CardView mCardView;

    @BindView(R.id.recyclerItem_btn_toMaintainRecord)
    public AppCompatButton btnMaintainRecord;

    @BindView(R.id.recyclerItem_btn_toEvent)
    public AppCompatButton btnEvent;

    @BindView(R.id.recyclerItem_btn_toInspectRecord)
    public AppCompatButton btnInspectRecord;

    public SearchResultHolder(View itemView) {
        super(itemView);
        mCardView=(CardView) itemView.findViewById(R.id.cardView_searchResult);
        ButterKnife.bind(this, itemView);
        btnMaintainRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MaintainListActivity.class);
//                intent.putExtra("UGO_ID", obj.getID());
                v.getContext().startActivity(intent);
            }
        });
        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventListActivity.class);
//                intent.putExtra("UGO_ID", obj.getID());
                v.getContext().startActivity(intent);
            }
        });
        btnInspectRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InspectListActivity.class);
//                intent.putExtra("UGO_ID", obj.getID());
                v.getContext().startActivity(intent);
            }
        });
    }
    public void setAddress(String address) {
        ((TextView)mCardView.findViewById(R.id.recyclerItem_textView_address)).setText(address);
    }

    public void setClassType_ID(String classType_ID) {
        ((TextView)mCardView.findViewById(R.id.recyclerItem_textView_classType)).setText(classType_ID);
    }

    public void setID(String ID) {
        ((TextView)mCardView.findViewById(R.id.recyclerItem_textView_ID)).setText(ID);
    }

    public void setName(String name) {
        ((TextView)mCardView.findViewById(R.id.recyclerItem_textView_name)).setText(name);
    }

}
