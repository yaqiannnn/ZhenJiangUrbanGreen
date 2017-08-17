package com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxs on 17-8-17.
 */

public class ActionSheet extends NestedScrollView {

    private RecyclerView rcvActions;
    private ActionSheetAdapter adapter;
    private OnClickListener clickListener;

    private BottomSheetDialog dialog;

    public ActionSheet(Context context, List<ActionItem> actions, OnClickListener li) {
        super(context);
        clickListener = li;
        View view = LayoutInflater.from(context).inflate(R.layout.action_sheet, this);
        rcvActions = (RecyclerView)findViewById(R.id.rcv_action_sheet);

        rcvActions.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ActionSheetAdapter(actions);
        rcvActions.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        adapter.setOnItemClickListener(new ActionSheetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int actionID) {
                clickListener.onClick(view, actionID);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface OnClickListener {
        void onClick(View view, int actionID);
    }
}
