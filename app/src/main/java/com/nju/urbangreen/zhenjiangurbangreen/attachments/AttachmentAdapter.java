package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;
import com.nju.urbangreen.zhenjiangurbangreen.util.TimeFormatUtil;
import com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet.ActionItem;
import com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet.ActionSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 17-8-17.
 */

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentHolder> {
    private List<AttachmentRecord> attachList;
    private String parentID;

    private static String actionTitles[] = {"上传", "下载", "查看", "删除", "重命名"};
    private static int[] iconIDs = {R.drawable.ic_btn_location, R.drawable.ic_btn_location,
            R.drawable.ic_btn_location, R.drawable.ic_btn_location, R.drawable.ic_btn_location};
    private static Drawable[] iconDrawables;

    public AttachmentAdapter(List<AttachmentRecord> list) {
        attachList = list;
        List<Drawable> icons = new ArrayList<>();
        Context context = MyApplication.getContext();
        for(int id : iconIDs) {
            icons.add(context.getResources().getDrawable(id));
        }
        iconDrawables = (Drawable[]) icons.toArray();
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
                new ActionSheet(view.getContext(), getActionItemFromAttachmentRecord(attachList.get(position)),
                        new ActionSheet.OnClickListener() {
                            @Override
                            public void onClick(View view, int actionID) {
                            Log.i("ActionSheet", position + "  " + actionID + "press");
                        }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachList.size();
    }

    public static List<ActionItem> getActionItemFromAttachmentRecord(AttachmentRecord attachmentRecord) {
        List<ActionItem> actions = new ArrayList<>();
        actions.add(new ActionItem(AttachAction.Upload.ordinal(), actionTitles[AttachAction.Upload.ordinal()],
                iconDrawables[AttachAction.Upload.ordinal()]));
        actions.add(new ActionItem(AttachAction.Download.ordinal(), actionTitles[AttachAction.Download.ordinal()],
                iconDrawables[AttachAction.Download.ordinal()]));
        actions.add(new ActionItem(AttachAction.View.ordinal(), actionTitles[AttachAction.View.ordinal()],
                iconDrawables[AttachAction.View.ordinal()]));
        actions.add(new ActionItem(AttachAction.Delete.ordinal(), actionTitles[AttachAction.Delete.ordinal()],
                iconDrawables[AttachAction.Delete.ordinal()]));
        return actions;
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

    enum AttachAction {
        Upload, Download, View, Delete, Rename
    }
}
