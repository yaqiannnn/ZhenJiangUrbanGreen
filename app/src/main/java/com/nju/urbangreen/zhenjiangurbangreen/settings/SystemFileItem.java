package com.nju.urbangreen.zhenjiangurbangreen.settings;

import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.util.UUID;

import io.realm.RealmObject;

/**
 * Created by lxs on 17-8-24.
 */

public class SystemFileItem extends RealmObject{
    public int ID;
    public String name;
    public String remotePath;
    public String savePath;

    public SystemFileItem() {}

    public SystemFileItem(String fileName, String fileRemotePath, String fileSavePath) {
        name = fileName;
        remotePath = fileRemotePath;
        savePath = fileSavePath;
        ID = FileDownloadUtils.generateId(remotePath, savePath);
    }
}
