package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;
import com.nju.urbangreen.zhenjiangurbangreen.util.TimeFormatUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.widget.ActionSheet.ActionItem;
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
    private FragmentActivity mContext;

    private static String actionTitles[] = {"上传", "下载", "查看", "删除", "重命名"};
    private static int[] iconIDs = {R.drawable.ic_attach_upload, R.drawable.ic_attach_download,
            R.drawable.ic_attach_view, R.drawable.ic_attach_delete, R.drawable.ic_attach_rename};
    private static Drawable[] iconDrawables;


    public AttachmentAdapter(FragmentActivity fragmentActivity, String parentRecordID) {
        mContext = fragmentActivity;
        parentID = parentRecordID;
        attachList = new ArrayList<>();
        List<Drawable> icons = new ArrayList<>();
        for(int id : iconIDs) {
            icons.add(mContext.getResources().getDrawable(id));
        }
        iconDrawables = new Drawable[icons.size()];
        icons.toArray(iconDrawables);
    }

    public void updateItem(final int recordIndex) {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(recordIndex);
            }
        });
    }

    public void addItem(AttachmentRecord record) {
        attachList.add(record);
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void removeItem(final int recordIndex) {
        attachList.remove(recordIndex);
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(recordIndex);
                notifyItemRangeChanged(recordIndex, getItemCount());
            }
        });
    }

    public void refreshItems(final AttachmentAdapter.Callback cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String errorMsg[] = new String[1];
                List<AttachmentRecord.AttachmentRecordInDB> res = WebServiceUtils
                        .getRecordAttachmentInfo(parentID, errorMsg);
                attachList.clear();
                for(AttachmentRecord.AttachmentRecordInDB recordInDB : res) {
                    attachList.add(new AttachmentRecord(recordInDB));
                }
                cb.refreshDone();
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
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
                                doAttachAction(position, AttachAction.values()[actionID]);
                        }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachList.size();
    }

    private void doAttachAction(final int attachIndex, AttachAction action) {
        final AttachmentRecord attachmentRecord = attachList.get(attachIndex);
        switch (action) {
            case Upload:
                AttachmentService.uploadAttach(mContext, this.parentID, attachmentRecord, new AttachmentService.Callback() {
                    @Override
                    public void success() {
                        Toast.makeText(mContext, attachmentRecord.fileName + "上传成功", Toast.LENGTH_SHORT).show();
                        updateItem(attachIndex);
                    }

                    @Override
                    public void failed(String msg) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case View:
                AttachmentService.viewAttach(mContext, attachmentRecord, new AttachmentService.Callback() {
                    @Override
                    public void success() {
                    }
                    @Override
                    public void failed(String msg) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        CacheUtil.removeFileLocalPath(attachmentRecord.fileID);
                    }
                });
                break;
            case Rename:
                AttachmentService.renameAttach(mContext, attachmentRecord, new AttachmentService.Callback() {
                    @Override
                    public void success() {
                        updateItem(attachIndex);
                    }
                    @Override
                    public void failed(String msg) {}
                });
                break;
            case Delete:
                if(attachmentRecord.atLocal && !attachmentRecord.hasUpload) {
                    removeItem(attachIndex);
                } else {
                    AttachmentService.removeAttach(mContext, attachmentRecord, new AttachmentService.Callback() {
                        @Override
                        public void success() {
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            removeItem(attachIndex);
                        }
                        @Override
                        public void failed(String msg) {
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
        }
    }

    public interface Callback {
        void refreshDone();
    }

    public static List<ActionItem> getActionItemFromAttachmentRecord(AttachmentRecord attachmentRecord) {
        List<ActionItem> actions = new ArrayList<>();
        if(attachmentRecord.atLocal && !attachmentRecord.hasUpload) {
            actions.add(new ActionItem(AttachAction.Upload.ordinal(), actionTitles[AttachAction.Upload.ordinal()],
                    iconDrawables[AttachAction.Upload.ordinal()]));
            actions.add(new ActionItem(AttachAction.View.ordinal(), actionTitles[AttachAction.View.ordinal()],
                    iconDrawables[AttachAction.View.ordinal()]));
            actions.add(new ActionItem(AttachAction.Delete.ordinal(), actionTitles[AttachAction.Delete.ordinal()],
                    iconDrawables[AttachAction.Delete.ordinal()]));
            actions.add(new ActionItem(AttachAction.Rename.ordinal(), actionTitles[AttachAction.Rename.ordinal()],
                    iconDrawables[AttachAction.Rename.ordinal()]));
        } else if(attachmentRecord.atLocal && attachmentRecord.hasUpload) {
            actions.add(new ActionItem(AttachAction.View.ordinal(), actionTitles[AttachAction.View.ordinal()],
                    iconDrawables[AttachAction.View.ordinal()]));
            actions.add(new ActionItem(AttachAction.Delete.ordinal(), actionTitles[AttachAction.Delete.ordinal()],
                    iconDrawables[AttachAction.Delete.ordinal()]));
        } else {
            actions.add(new ActionItem(AttachAction.Download.ordinal(), actionTitles[AttachAction.Download.ordinal()],
                    iconDrawables[AttachAction.Download.ordinal()]));
            actions.add(new ActionItem(AttachAction.Delete.ordinal(), actionTitles[AttachAction.Delete.ordinal()],
                    iconDrawables[AttachAction.Delete.ordinal()]));
        }
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
            tvUpdateTime.setText(TimeFormatUtil.format(record.uploadTime));
            tvFileSize.setText(FileUtil.getSizeStr(record.fileSize));
            if(record.atLocal && !record.hasUpload) {
                tvFileStatus.setText("未上传");
                tvFileStatus.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }
            else if(record.atLocal && record.hasUpload) {
                tvFileStatus.setText("已上传");
                tvFileStatus.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            }
            else {
                tvFileStatus.setText("未下载");
                tvFileStatus.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }
        }
    }

    enum AttachAction {
        Upload, Download, View, Delete, Rename
    }
}
