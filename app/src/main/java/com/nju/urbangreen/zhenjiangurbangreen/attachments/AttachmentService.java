package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.urbangreen.zhenjiangurbangreen.util.MyApplication;
import com.nju.urbangreen.zhenjiangurbangreen.util.SPUtils;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;

import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by lxs on 17-8-18.
 */

public class AttachmentService {

    public static void renameAttach(Context context, final AttachmentRecord attach,
                                    final AttachmentService.Callback cb) {
        new MaterialDialog.Builder(context)
                .title("重命名文件")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("文件名", attach.fileName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        attach.fileName = input.toString();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        cb.success();
                    }
                })
                .show();
    }

    public static void uploadAttach(Context context, String parentID, final AttachmentRecord attach,
                                    final AttachmentService.Callback cb) {
        try {
            String uploadId = UUID.randomUUID().toString();
            HashMap<String, Object> params = new HashMap<>();
            params.put("ParentID", parentID);
            params.put("FileName", attach.fileName);
            params.put("FAID", attach.fileID);
            params.put("FileSize", attach.fileSize);
            String serverUrl = WebServiceUtils.getFileUploadUrl(params);

            BinaryUploadRequest request = new BinaryUploadRequest(context, uploadId, serverUrl)
                    .setFileToUpload(attach.localPath)
                    .setMethod("POST")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(SPUtils.getInt("MAX_RETRIES", 2))
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            
                        }
                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            attach.hasUpload = false;
                            cb.failed();
                        }
                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            attach.hasUpload = true;
                            cb.success();
                        }
                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            attach.hasUpload = false;
                            cb.failed();
                        }
                    });
            request.startUpload();

        } catch (Exception exc) {
            Toast.makeText(context, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface Callback {
        public abstract void success();
        public abstract void failed();
    }
}
