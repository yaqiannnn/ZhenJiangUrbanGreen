package com.nju.urbangreen.zhenjiangurbangreen.settings;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lxs on 17-8-23.
 */

public class SystemFileAdapter extends RecyclerView.Adapter<SystemFileAdapter.SystemFileHolder> {

    private FragmentActivity mContext;
    private DownloadTaskManager taskManager;

    public SystemFileAdapter(FragmentActivity activity, DownloadTaskManager downloadTaskManager) {
        mContext = activity;
        taskManager = downloadTaskManager;
    }

    @Override
    public SystemFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.system_file_download_item, parent, false);
        final SystemFileHolder holder = new SystemFileHolder(view);
        holder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemFileItem file = taskManager.get(holder.getAdapterPosition());
                BaseDownloadTask task = FileDownloader.getImpl().create(file.remotePath)
                        .setPath(file.savePath)
                        .setCallbackProgressTimes(100)
                        .setListener(downloadListener);
                taskManager.addTaskForViewHolder(task);
                taskManager.updateViewHolder(holder.ID, holder);
                task.start();
            }
        });
        holder.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileDownloader.getImpl().pause(holder.ID);
            }
        });
        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.deleteFile(taskManager.get(holder.getAdapterPosition()).savePath);
                holder.updateNotDownloaded(FileDownloadStatus.INVALID_STATUS, 0, 0);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(SystemFileHolder holder, int position) {
        SystemFileItem file = taskManager.get(position);
        holder.bindObj(file);
        int status = taskManager.getStatus(file.ID, file.savePath);
        if (status == FileDownloadStatus.pending || status == FileDownloadStatus.started ||
                status == FileDownloadStatus.connected) {
            // start task, but file not created yet
            holder.updateDownloadingStatus(status, taskManager.getSoFar(file.ID), taskManager.getTotal(file.ID), 0);
        } else if (!new File(file.savePath).exists() &&
                !new File(FileDownloadUtils.getTempPath(file.savePath)).exists()) {
            // not exist file
            holder.updateNotDownloaded(status, 0, 0);
        } else if (taskManager.isDownloaded(status)) {
            // already downloaded and exist
            holder.updateDownloadedStatus();
        } else if (status == FileDownloadStatus.progress) {
            // downloading
            holder.updateDownloadingStatus(status, taskManager.getSoFar(file.ID), taskManager.getTotal(file.ID), 0);
        } else {
            // not start
            holder.updateNotDownloaded(status, taskManager.getSoFar(file.ID), taskManager.getTotal(file.ID));
        }
    }

    @Override
    public int getItemCount() {
        return taskManager.getTaskCounts();
    }

    private FileDownloadListener downloadListener = new FileDownloadSampleListener() {
        private SystemFileHolder checkCurrentHolder(final BaseDownloadTask task) {
            final SystemFileHolder tag = (SystemFileHolder) task.getTag();
            if (tag.ID != task.getId()) {
                return null;
            }
            return tag;
        }
        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            final SystemFileHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloadingStatus(FileDownloadStatus.pending, soFarBytes, totalBytes, 0);
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            final SystemFileHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            final SystemFileHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloadingStatus(FileDownloadStatus.connected, soFarBytes, totalBytes, 0);
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            final SystemFileHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloadingStatus(FileDownloadStatus.progress, soFarBytes, totalBytes, task.getSpeed());
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            final SystemFileHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes(), task.getLargeFileTotalBytes());
            DownloadTaskManager.instance().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            final SystemFileHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }
            tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
            DownloadTaskManager.instance().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            final SystemFileHolder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }
            tag.updateDownloadedStatus();
            DownloadTaskManager.instance().removeTaskForViewHolder(task.getId());
        }
    };

    public class SystemFileHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_system_file_name)
        public TextView tvName;

        @BindView(R.id.tv_system_file_status)
        public TextView tvStatus;

        @BindView(R.id.tv_system_file_download_info)
        public TextView tvDownloadInfo;

        @BindView(R.id.btn_system_file_start)
        public AppCompatButton btnStart;

        @BindView(R.id.btn_system_file_pause)
        public AppCompatButton btnPause;

        @BindView(R.id.btn_system_file_del)
        public AppCompatButton btnDel;

        @BindView(R.id.progress_system_file)
        public ProgressBar progressBar;

        public int ID;

        public SystemFileHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindObj(SystemFileItem file) {
            ID = file.ID;
            tvName.setText(file.name);
        }

        public void updateDownloadedStatus() {
            progressBar.setMax(1);
            progressBar.setProgress(1);

            tvStatus.setText("下载完成");
            tvStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.green_land_border));
            tvDownloadInfo.setVisibility(View.INVISIBLE);
        }

        public void updateNotDownloaded(int status, long sofar, long total) {
            if (sofar > 0 && total > 0) {
                int progress = (int)((float)sofar / total * 100.f);
                progressBar.setMax(100);
                progressBar.setProgress(progress);
            } else {
                progressBar.setMax(1);
                progressBar.setProgress(0);
            }
            switch (status) {
                case FileDownloadStatus.error:
                    tvStatus.setText("下载失败");
                    tvStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorAccent));
                    break;
                case FileDownloadStatus.paused:
                    String downloadInfo = FileUtil.byte2SizeStr(sofar) + "/" + FileUtil.byte2SizeStr(total);
                    tvDownloadInfo.setText(downloadInfo);
                    tvDownloadInfo.setVisibility(View.VISIBLE);
                    tvStatus.setText("下载暂停");
                    tvStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
                    break;
                default:
                    tvStatus.setText("未下载");
                    tvStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.primary_text));
                    break;
            }
        }

        public void updateDownloadingStatus(int status, long sofar, long total, int speed) {
            int progress = (int)((float)sofar / total * 100.f);
            progressBar.setMax(100);
            progressBar.setProgress(progress);
            String downloadInfo = FileUtil.byte2SizeStr(sofar) + "/" + FileUtil.byte2SizeStr(total) + "    ";
            if(speed > 1024)
                downloadInfo += (speed / 1024) + " MB/s";
            else
                downloadInfo += speed + " KB/s";
            tvDownloadInfo.setText(downloadInfo);
            tvDownloadInfo.setVisibility(View.VISIBLE);
            tvStatus.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
            switch (status) {
                case FileDownloadStatus.pending:
                    tvStatus.setText("准备中...");
                    break;
                case FileDownloadStatus.started:
                    tvStatus.setText("开始下载...");
                    break;
                case FileDownloadStatus.connected:
                    tvStatus.setText("连接中...");
                    break;
                case FileDownloadStatus.progress:
                    tvStatus.setText("下载中");
                    break;
                default:
                    tvStatus.setText("下载");
                    break;
            }
        }

    }
}
