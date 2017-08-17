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
    private int attachIndex;

    private RecyclerView rcvActions;
    private ActionSheetAdapter adapter;
    private OnClickListener clickListener;

    private BottomSheetDialog dialog;

    public ActionSheet(Context context, int attach_index, String titles[], int icons[], OnClickListener li) {
        super(context);
        attachIndex =attach_index;
        clickListener = li;
        View view = LayoutInflater.from(context).inflate(R.layout.action_sheet, this);
        rcvActions = (RecyclerView)findViewById(R.id.rcv_action_sheet);

        rcvActions.setLayoutManager(new LinearLayoutManager(context));
        List<Drawable> iconsDrawable = new ArrayList<>();
        for(int icon_id : icons) {
            iconsDrawable.add(getResources().getDrawable(icon_id));
        }
        adapter = new ActionSheetAdapter(titles, iconsDrawable);
        rcvActions.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        adapter.setOnItemClickListener(new ActionSheetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickListener.onClick(view, attachIndex, position);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public interface OnClickListener {
        void onClick(View view, int attachIndex, int actionIndex);
    }
}
