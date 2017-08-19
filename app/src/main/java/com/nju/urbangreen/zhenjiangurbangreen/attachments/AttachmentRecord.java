package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.TimeFormatUtil;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lxs on 17-8-17.
 */

public class AttachmentRecord {
    public String fileID;
    public String localPath;
    public String fileName;
    public long fileSize; // 单位为字节
    public String uploadTime;
    public String remotePath;
    public boolean atLocal; // 是否在本地
    public boolean hasUpload; // 是否上传到服务器

    public AttachmentRecord(File file) {
        fileID = UUID.randomUUID().toString();
        localPath = file.getPath();
        fileName = FileUtil.getFileName(localPath);
        fileSize = file.length();
        uploadTime = new Date().toString();
        CacheUtil.putFileLocalPath(fileID, localPath);
        atLocal = true;
        hasUpload = false;
    }

    public AttachmentRecord(AttachmentRecordInDB recordInDB) {
        fileID = recordInDB.File_ID;
        fileName = recordInDB.File_DigitalFileName;
        fileSize = recordInDB.File_DigitalFileSize;
        remotePath = recordInDB.File_RemotePath;
        uploadTime = TimeFormatUtil.format(recordInDB.File_RegistryTime);
        localPath = CacheUtil.getFileLocalPath(fileID);
        if(localPath == null) {
            atLocal = false;
            hasUpload = true;
        } else {
            atLocal = true;
            hasUpload = true;
        }
    }

    public class AttachmentRecordInDB {
        public String File_ID;
        public String File_DigitalFileName;
        public long File_DigitalFileSize; // 单位为字节
        public String File_RegistryTime;
        public String File_RemotePath;
    }
}

