package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

import java.util.ArrayList;

public class AttachmentListActivity extends BaseActivity {

    @BindView(R.id.ly_attachments_title_bar)
    public TitleBarLayout titleBarLayout;
    @BindView(R.id.lv_attachments_list)
    public ListView lvAttachmentRecords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment_list);

        ButterKnife.bind(this);
        titleBarLayout.setTitleText("附件列表");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_add);
        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
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
}
