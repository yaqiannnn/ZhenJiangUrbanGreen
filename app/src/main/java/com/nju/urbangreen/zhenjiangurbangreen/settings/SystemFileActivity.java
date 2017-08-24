package com.nju.urbangreen.zhenjiangurbangreen.settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemFileActivity extends BaseActivity {

    @BindView(R.id.Toolbar_simple)
    public Toolbar toolbar;

    @BindView(R.id.rcv_system_files)
    public RecyclerView rcvFiles;

    private SystemFileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_file);
        ButterKnife.bind(this);
        initToolbar();

        rcvFiles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SystemFileAdapter(this);
        rcvFiles.setAdapter(adapter);
    }

    private void initToolbar() {
        toolbar.setTitle("系统文件下载");
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
