package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;

import java.util.ArrayList;

public class AttachmentListActivity extends BaseActivity {

    @BindView(R.id.Toolbar_simple)
    public Toolbar toolbar;

    @BindView(R.id.lv_attachments_list)
    public ListView lvAttachmentRecords;

    @BindView(R.id.floatingbtn_add_attach)
    public FloatingActionButton floatingbtnAddAttach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_list);

        ButterKnife.bind(this);
        initToolbar();
        floatingbtnAddAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加选择文件的操作
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent,"请选择待上传的文件"),1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AttachmentListActivity.this, "请安装文件管理APP", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //以下是用来测试附件列表的数据
        ArrayList<OneAttachmentRecord> list = new ArrayList<OneAttachmentRecord>();
        list.add(new OneAttachmentRecord("what doesn't kill you","0kb"));
        AttachmentRecordAdapter adapter = new AttachmentRecordAdapter(this,R.layout.attachment_list_item,list);
        lvAttachmentRecords.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            Uri uri = data.getData();
            String path = FileUtil.getPath(uri);
            String filename = path.substring(path.lastIndexOf('/') + 1);
            fileNameInputDialog(filename);
        }
    }

    private void fileNameInputDialog(String filename) {
        new MaterialDialog.Builder(this)
                .title("重命名文件")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("文件名", filename, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        Toast.makeText(AttachmentListActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void initToolbar() {
        toolbar.setTitle("附件列表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
