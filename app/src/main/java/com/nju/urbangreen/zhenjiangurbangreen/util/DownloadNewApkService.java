package com.nju.urbangreen.zhenjiangurbangreen.util;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;

/**
 * Created by HCQIN on 2016/11/6.
 */
public class DownloadNewApkService extends Service{
    private String URL_PATH = "http://192.168.0.106:81/app-release.apk";
    DownloadManager downloadManager;
    Long downloadId;
    DownloadCompleteReceiver receiver;

    public DownloadNewApkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        receiver = new DownloadCompleteReceiver();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(URL_PATH));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("应用程序更新");
        request.setDescription("正在下载更新");
        request.setAllowedOverRoaming(false);
        request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + "NARUTO/","app-release.apk")));
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);

        registerReceiver(receiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intentInstall = new Intent();
            intentInstall.setAction(Intent.ACTION_VIEW);

            intentInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentInstall.setDataAndType(downloadManager.getUriForDownloadedFile(downloadId),"application/vnd.android.package-archive");
            startActivity(intentInstall);

            DownloadNewApkService.this.stopSelf();
        }
    }
}
