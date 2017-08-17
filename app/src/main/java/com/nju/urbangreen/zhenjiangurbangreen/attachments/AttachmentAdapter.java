package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.TimeFormatUtil;
import com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet.ActionSheet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 17-8-17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentHolder> {
    private List<AttachmentRecord> attachList;
    private String parentID;

    private static String[] actionTitles= {"上传", "查看", "删除", "重命名"};
    private static int[] iconIDs = {R.drawable.ic_btn_location, R.drawable.ic_btn_location,
            R.drawable.ic_btn_location, R.drawable.ic_btn_location};

    public AttachmentAdapter() {
        attachList = new ArrayList<>();
    }

    public AttachmentAdapter(List<AttachmentRecord> list) {
        attachList = list;
    }

    public AttachmentAdapter(String id) {
        parentID = id;
    }

    public void addItem(AttachmentRecord record) {
        attachList.add(record);
        notifyDataSetChanged();
    }

    @Override
    public AttachmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attachment_list_item, parent, false);
        return new AttachmentHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentHolder holder, final int position) {
        holder.setText(attachList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActionSheet(view.getContext(), position, actionTitles, iconIDs, new ActionSheet.OnClickListener() {
                    @Override
                    public void onClick(View view, int attachIndex, int actionIndex) {
                        Log.i("ActionSheet", attachIndex + "  " + actionIndex + "press");
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachList.size();
    }

    class AttachmentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_attach_item_name)
        public TextView tvFileName;

        @BindView(R.id.tv_attach_item_time)
        public TextView tvUpdateTime;

        @BindView(R.id.tv_attach_item_size)
        public TextView tvFileSize;

        @BindView(R.id.tv_attach_item_status)
        public TextView tvFileStatus;

        public AttachmentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setText(AttachmentRecord record) {
            tvFileName.setText(record.fileName);
            tvUpdateTime.setText(TimeFormatUtil.format(record.updateTime));
            tvFileSize.setText(record.fileSizeStr);
            tvFileStatus.setText("未上传");
        }
    }
}
