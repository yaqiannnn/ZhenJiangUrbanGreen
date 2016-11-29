package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by lxs on 2016/9/30.
 */
public class SearchResultHolder extends RecyclerView.ViewHolder{
    private CardView mCardView;
    public SearchResultHolder(View itemView) {
        super(itemView);
        mCardView=(CardView) itemView.findViewById(R.id.cardView_searchResult);
        AppCompatButton maintainRecord_button=(AppCompatButton)mCardView.findViewById(R.id.recyclerItem_btn_toMaintainRecord);
        AppCompatButton event_button=(AppCompatButton)mCardView.findViewById(R.id.recyclerItem_btn_toEvent);
        AppCompatButton inspectRecord_button=(AppCompatButton)mCardView.findViewById(R.id.recyclerItem_btn_toInspectRecord);
        maintainRecord_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","event_button");
            }
        });
        inspectRecord_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
