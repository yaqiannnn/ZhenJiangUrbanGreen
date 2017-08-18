package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;

import net.gotev.uploadservice.Placeholders;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadNotificationStatusConfig;
import net.gotev.uploadservice.UploadService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lxs on 17-8-18.
 */

public class UploadAttachNotification {

    private static int Upload_Notification_ID = 1234; // Something unique
    private static Context mContext = MyApplication.getContext();
    private static NotificationManager notificationManager = (NotificationManager) MyApplication
            .getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    private static UploadNotificationConfig config = getNotificationConfig(mContext);

    private static Map<String, Integer> notificationIDMap = new HashMap<>();

    public static void createNotification(String uploadID) {
        UploadNotificationStatusConfig statusConfig = config.getProgress();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext)
                .setContentTitle("上传附件")
                .setContentIntent(statusConfig.clickIntent)
                .setSmallIcon(statusConfig.iconResourceID)
                .setLargeIcon(statusConfig.largeIcon)
                .setColor(statusConfig.iconColorResourceID)
                .setGroup(UploadService.NAMESPACE)
                .setProgress(100, 0, true)
                .setSortKey(String.valueOf(Upload_Notification_ID))
                .setOngoing(true);

        Notification builtNotification = notification.build();
//        builtNotification.when = 1000;
//        builtNotification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        notificationManager.notify(Upload_Notification_ID, builtNotification);
        notificationIDMap.put(uploadID, Upload_Notification_ID);
        Upload_Notification_ID += 2;
    }

    public static void updateNotificationProgress(UploadInfo uploadInfo) {
        UploadNotificationStatusConfig statusConfig = config.getProgress();

        NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext)
                .setContentTitle(Placeholders.replace(statusConfig.title, uploadInfo))
                .setContentText(Placeholders.replace(statusConfig.message, uploadInfo))
                .setContentIntent(statusConfig.clickIntent)
                .setSmallIcon(statusConfig.iconResourceID)
                .setLargeIcon(statusConfig.largeIcon)
                .setColor(statusConfig.iconColorResourceID)
                .setProgress((int)uploadInfo.getTotalBytes(), (int)uploadInfo.getUploadedBytes(), false)
                .setOngoing(true);

        Notification builtNotification = notification.build();

        notificationManager.notify(notificationIDMap.get(uploadInfo.getUploadId()), builtNotification);
    }

    public static void updateNotification(UploadInfo uploadInfo) {
        int notificationId = notificationIDMap.get(uploadInfo.getUploadId());
        notificationManager.cancel(notificationId);
        UploadNotificationStatusConfig statusConfig = config.getProgress();

        if (statusConfig.message == null) return;

        if (!statusConfig.autoClear) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(mContext)
                    .setContentTitle(Placeholders.replace(statusConfig.title, uploadInfo))
                    .setContentText(Placeholders.replace(statusConfig.message, uploadInfo))
                    .setContentIntent(statusConfig.clickIntent)
                    .setAutoCancel(statusConfig.clearOnAction)
                    .setSmallIcon(statusConfig.iconResourceID)
                    .setLargeIcon(statusConfig.largeIcon)
                    .setColor(statusConfig.iconColorResourceID)
                    .setProgress(0, 0, false)
                    .setOngoing(false);

            setRingtone(notification);

            // this is needed because the main notification used to show progress is ongoing
            // and a new one has to be created to allow the user to dismiss it
//            uploadInfo.setNotificationID(notificationId + 1);
            notificationManager.notify(notificationId + 1, notification.build());
        }
    }

    private static void setRingtone(NotificationCompat.Builder notification) {

        if(config.isRingToneEnabled()) {
            notification.setSound(RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_NOTIFICATION));
            notification.setOnlyAlertOnce(false);
        }

    }

    private static UploadNotificationConfig getNotificationConfig(Context context) {
        UploadNotificationConfig config = new UploadNotificationConfig();

        PendingIntent clickIntent = PendingIntent.getActivity(context, 1,
                new Intent(context, AttachmentListActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        config.setTitleForAllStatuses("附件上传")
                .setRingToneEnabled(true)
                .setClickIntentForAllStatuses(clickIntent)
                .setClearOnActionForAllStatuses(true);

        config.getProgress().message = context.getResources().getString(R.string.uploading);

        config.getCompleted().message = context.getResources().getString(R.string.upload_success);
        config.getCompleted().iconColorResourceID = context.getResources().getColor(R.color.colorPrimary);

        config.getError().message = context.getResources().getString(R.string.upload_error);
        config.getError().iconColorResourceID = context.getResources().getColor(R.color.colorAccent);

        return config;
    }
}
