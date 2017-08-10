package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 2016/9/30.
 */
public class SearchResultHolder extends RecyclerView.ViewHolder{
    private GreenObject obj;
    private CardView mCardView;

    @BindView(R.id.btn_search_item_MR)
    public AppCompatButton btnMaintainRecord;

    @BindView(R.id.btn_search_item_Event)
    public AppCompatButton btnEvent;

    @BindView(R.id.btn_search_item_IR)
    public AppCompatButton btnInspectRecord;

    @BindView(R.id.iv_search_item_detail)
    public AppCompatImageView ivDetail;

    public SearchResultHolder(View itemView) {
        super(itemView);
        mCardView=(CardView) itemView.findViewById(R.id.cardView_searchResult);
        ButterKnife.bind(this, itemView);
        ivDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MaintainListActivity.class);
            }
        });
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

    public void bindObj(GreenObject object) {
        this.obj = object;
        if(obj.UGO_Address != null) {
            ((TextView)mCardView.findViewById(R.id.tv_search_item_address)).setText("地址: " + obj.UGO_Address);
        }
        String type = "类型: 行道树";
        if(obj.UGO_ClassType_ID.equals("000"))
            type = "类型: 绿地";
        else if(obj.UGO_ClassType_ID.equals("001"))
            type = "类型: 古树名木";
        ((TextView)mCardView.findViewById(R.id.tv_search_item_type)).setText(type);
        ((TextView)mCardView.findViewById(R.id.tv_search_item_id)).setText(obj.UGO_ID);
        ((TextView)mCardView.findViewById(R.id.tv_search_item_name)).setText("名字: " + obj.UGO_Name);
    }


}
