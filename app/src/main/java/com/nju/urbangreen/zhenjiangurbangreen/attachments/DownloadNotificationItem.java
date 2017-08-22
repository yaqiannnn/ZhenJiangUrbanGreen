package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by lxs on 17-8-22.
 */

public class DownloadNotificationItem extends BaseNotificationItem{

    private PendingIntent pendingIntent;
    private NotificationCompat.Builder builder;
    private long startTime;
    private Context mContext;

    public DownloadNotificationItem(Context context, int id, String title, String desc) {
        super(id, title, desc);
        mContext = context;
        pendingIntent = PendingIntent.getActivity(context, 1,
                new Intent(context, AttachmentListActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

        builder = new NotificationCompat.
                Builder(FileDownloadHelper.getAppContext());

        builder.setDefaults(Notification.DEFAULT_LIGHTS)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentTitle(getTitle())
                .setContentText(desc)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_download_notification)
                .setColor(mContext.getResources().getColor(R.color.colorPrimary))
                .setGroup("download.demo");
    }

    public String getSpeedStr() {
        int speed = getSpeed();
        if(speed > 1024) {
            speed /= 1024;
            return speed + "MB/s  ";
        }
        return speed + "KB/s  ";
    }

    @Override
    public void show(boolean statusChanged, int status, boolean isShowProgress) {
        String desc = "";
        boolean auto_clear =false;
        switch (status) {
            case FileDownloadStatus.pending:
                desc += " 等待中...";
                isShowProgress = true;
                break;
            case FileDownloadStatus.started:
                desc += " 下载开始  ";
                startTime = System.currentTimeMillis();
                break;
            case FileDownloadStatus.progress:
                desc += " 下载中...";
                desc += getSpeedStr();
                isShowProgress = true;
                break;
            case FileDownloadStatus.retry:
                desc += " 重试中  ";
                isShowProgress = true;
                break;
            case FileDownloadStatus.error:
                desc += " 下载失败  ";
                auto_clear = true;
                break;
            case FileDownloadStatus.paused:
                desc += " 下载暂停  ";
                isShowProgress = true;
                break;
            case FileDownloadStatus.completed:
                desc += " 下载完成  ";
                auto_clear = true;
                break;
            case FileDownloadStatus.warn:
                desc += " 下载出错  ";
                auto_clear = true;
                break;
        }
        desc += (int)(((float)getSofar()) / getTotal() * 100.f) + "%";
        if(auto_clear) {
            Log.i("Download Notice", String.valueOf(getId()));
            getManager().cancel(getId());
            long useTime = (System.currentTimeMillis() - startTime) / 1000;
            NotificationCompat.Builder newBuilder= new NotificationCompat.Builder(mContext)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_download_notification)
                    .setColor(mContext.getResources().getColor(R.color.green_land_border))
                    .setContentTitle(getTitle())
                    .setContentText("下载完成" + "，耗费" +  useTime + "s")
                    .setAutoCancel(false)
                    .setOngoing(false)
                    .setGroup("download.demo");
            getManager().notify(getId() + 1, newBuilder.build());
        } else {
            builder.setContentTitle(getTitle())
                    .setContentText(desc)
                    .setProgress(getTotal(), getSofar(), !isShowProgress);
            getManager().notify(getId(), builder.build());
        }
    }
}
