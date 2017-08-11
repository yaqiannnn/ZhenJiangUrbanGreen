package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;

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
                startActivityForResult(Intent.createChooser(intent,"请选择待上传的文件"),1);
            }
        });
        //以下是用来测试附件列表的数据
        ArrayList<OneAttachmentRecord> list = new ArrayList<OneAttachmentRecord>();
        list.add(new OneAttachmentRecord("what doesn't kill you","0kb"));
        AttachmentRecordAdapter adapter = new AttachmentRecordAdapter(this,R.layout.attachment_list_item,list);
        lvAttachmentRecords.setAdapter(adapter);
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
