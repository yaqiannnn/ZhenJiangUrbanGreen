package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by lxs on 17-8-17.
 */

public class AttachmentRecord {
    public String localPath;
    public String fileName;
    public long fileSize; // 单位为字节
    public String fileSizeStr;
    public String updateTime;
    public int status; // 0代表未上传

    public AttachmentRecord(File file) {
        this.localPath = file.getPath();
        this.fileName = FileUtil.getFileName(localPath);
        this.fileSize = file.length();
        this.fileSizeStr = FileUtil.getSizeStr(this.fileSize);
        this.updateTime = new Date().toString();
    }
}
