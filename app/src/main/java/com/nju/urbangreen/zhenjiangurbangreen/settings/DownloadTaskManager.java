package com.nju.urbangreen.zhenjiangurbangreen.settings;

import android.content.Context;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lxs on 17-8-24.
 */

public class DownloadTaskManager {

    private List<SystemFileItem> systemFiles;
    private Map<Integer, BaseDownloadTask> taskMap;
    private FileDownloadConnectListener connectListener;

    private static DownloadTaskManager mInstance = null;

    public static DownloadTaskManager instance() {
        if(mInstance == null) {
            mInstance = new DownloadTaskManager();
        }
        return mInstance;
    }

    public DownloadTaskManager() {
        systemFiles = CacheUtil.getSystemFiles();
        taskMap = new HashMap<>();

        String saveDir = FileUtil.getAppFileDir() + "tpk" + File.separator;
        if(systemFiles.size() <= 0) {
            // 本地没有缓存相关的系统文件信息
            String fileNames[] = WebServiceUtils.BaseMapFileNames;
            String fileURLs[] = {
                    WebServiceUtils.RESOURCE_ADDRESS + "TPK" + File.separator + fileNames[0],
                    WebServiceUtils.RESOURCE_ADDRESS + "TPK" + File.separator + fileNames[1]
            };
            for(int i = 0; i < fileNames.length; i++) {
                systemFiles.add(new SystemFileItem(fileNames[i], fileURLs[i], saveDir + fileNames[i]));
            }
            CacheUtil.saveSystemFiles(systemFiles);
        }
    }

    public void addTaskForViewHolder(final BaseDownloadTask task) {
        taskMap.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(final int id) {
        taskMap.remove(id);
    }

    public void updateViewHolder(final int id, final SystemFileAdapter.SystemFileHolder holder) {
        final BaseDownloadTask task = taskMap.get(id);
        if (task == null) {
            return;
        }
        task.setTag(holder);
    }

    public void releaseTask() {
        taskMap.clear();
    }

    public void onCreate(final ConnectCallback cb) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            // Register ServiceConnectionListener
            if (connectListener != null) {
                FileDownloader.getImpl().removeServiceConnectListener(connectListener);
            }

            connectListener = new FileDownloadConnectListener() {
                @Override
                public void connected() {
                    cb.connect();
                }
                @Override
                public void disconnected() {
                    cb.disconnect();
                }
            };
            FileDownloader.getImpl().addServiceConnectListener(connectListener);
        }
    }

    public void onDestroy() {
        // Unregister ServiceConnectionListener
        FileDownloader.getImpl().removeServiceConnectListener(connectListener);
        connectListener = null;
        releaseTask();
    }

    public boolean isDownloaded(final int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCounts() {
        return systemFiles.size();
    }

    public SystemFileItem get(final int position) {
        return systemFiles.get(position);
    }

    public interface ConnectCallback {
        void connect();
        void disconnect();
    }
}
