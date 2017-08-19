package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.FileUtil;

import java.io.File;
import java.util.ArrayList;

public class AttachmentListActivity extends BaseActivity {

    @BindView(R.id.Toolbar_simple)
    public Toolbar toolbar;

    @BindView(R.id.rcv_attachments_list)
    public RecyclerView rcvAttachmentRecords;
    private AttachmentAdapter adapter;

    @BindView(R.id.floatingbtn_add_attach)
    public FloatingActionButton floatingbtnAddAttach;

    private String recordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_list);
        ButterKnife.bind(this);

        initToolbar();
        setUploadButton();

        ArrayList<AttachmentRecord> list = new ArrayList<>();
        rcvAttachmentRecords.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AttachmentAdapter(AttachmentListActivity.this, "0420b1b8-5b20-4213-9b5f-586dbca94ef7", list);
        rcvAttachmentRecords.setAdapter(adapter);
    }

    private void setUploadButton() {
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
    }

    //选择完文件后的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            Uri uri = data.getData();
            String path = FileUtil.getPath(uri);
            if(path != null) {
                File file = new File(path);
                adapter.addItem(new AttachmentRecord(file));
            } else {
                Toast.makeText(AttachmentListActivity.this, "文件损坏，请重新添加", Toast.LENGTH_SHORT).show();
            }
        }
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
