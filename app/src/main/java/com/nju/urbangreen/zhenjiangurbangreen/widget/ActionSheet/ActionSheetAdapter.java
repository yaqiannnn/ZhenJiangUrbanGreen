package com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 17-8-17.
 */

class ActionSheetAdapter extends RecyclerView.Adapter<ActionSheetAdapter.ActionSheetHolder> {
    private OnItemClickListener itemClickListener;
    private List<ActionItem> actionItems;

    public ActionSheetAdapter(List<ActionItem> actions) {
        actionItems = actions;
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        itemClickListener = li;
    }

    @Override
    public ActionSheetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.action_sheet_item, parent, false);
        ActionSheetHolder holder = new ActionSheetHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ActionSheetHolder holder, final int position) {
        holder.ivIcon.setImageDrawable(actionItems.get(position).icon);
        holder.tvTitle.setText(actionItems.get(position).title);
        if(itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, actionItems.get(position).actionID);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return actionItems.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view , int actionID);
    }

    public class ActionSheetHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_action_sheet_icon)
        public ImageView ivIcon;

        @BindView(R.id.tv_action_sheet_title)
        public TextView tvTitle;

        public ActionSheetHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
