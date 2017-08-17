package com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 17-8-17.
 */

class ActionSheetAdapter extends RecyclerView.Adapter<ActionSheetAdapter.ActionSheetHolder> {
    private OnItemClickListener itemClickListener;
    private List<String> titles;
    private List<Drawable> icons;

    public ActionSheetAdapter(String buttonTitles[], List<Drawable> buttonIcons) {
        titles = Arrays.asList(buttonTitles);
        icons = new ArrayList<>(buttonIcons);
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
        holder.ivIcon.setImageDrawable(icons.get(position));
        holder.tvTitle.setText(titles.get(position));
        if(itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view , int position);
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
