package com.nju.urbangreen.zhenjiangurbangreen.attachments;

/**
 * Created by Liwei on 2016/12/2.
 */
public class OneAttachmentRecord {

    private String fileName;
    private String fileSize;

    public OneAttachmentRecord(String fileName, String fileSize){
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
