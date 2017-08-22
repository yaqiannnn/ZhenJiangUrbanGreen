package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.map.SimpleMapActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.TimeFormatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UGOInfoActivity extends BaseActivity {
    private GreenObject obj;

    @BindView(R.id.Toolbar_simple)
    public Toolbar toolbar;

    @BindView(R.id.floatingbtn_UGO_location)
    public FloatingActionButton floatingbtn_location;

    @BindView(R.id.tv_UGO_ID)
    public TextView tvUGOID;

    @BindView(R.id.tv_UGO_Code)
    public TextView tvUGOCode;

    @BindView(R.id.tv_UGO_Type)
    public TextView tvType;

    @BindView(R.id.tv_UGO_Name)
    public TextView tvName;

    @BindView(R.id.tv_UGO_Address)
    public TextView tvAddress;

    @BindView(R.id.tv_UGO_Area)
    public TextView tvArea;

    @BindView(R.id.tv_UGO_Owner)
    public TextView tvOwner;

    @BindView(R.id.tv_UGO_Logger)
    public TextView tvLogger;

    @BindView(R.id.tv_UGO_LoggerTime)
    public TextView tvLoggerTime;

    @BindView(R.id.tv_UGO_Editor)
    public TextView tvEditor;

    @BindView(R.id.tv_UGO_EditorTime)
    public TextView tvEditorTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_ugoinfo);
        ButterKnife.bind(this);
        initToolbar();
        setLocationButton();
        bindText();
    }

    private void initToolbar() {
        toolbar.setTitle("绿化对象详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void bindText() {
        tvUGOID.setText(obj.UGO_ID);
        tvUGOCode.setText(obj.UGO_Ucode);
        if(obj.UGO_ClassType_ID.equals("000"))
            tvType.setText("绿地");
        else if(obj.UGO_ClassType_ID.equals("001"))
            tvType.setText("古树名木");
        else
            tvType.setText("行道树");
        tvName.setText(obj.UGO_Name);
        tvAddress.setText(obj.UGO_Address);
        tvArea.setText(String.valueOf(obj.UGO_CurrentArea));
        tvOwner.setText(obj.UGO_CurrentOwner);
        tvLogger.setText(obj.UGO_LoggerPID);
        tvLoggerTime.setText(TimeFormatUtil.format(obj.UGO_LogTime));
        tvEditor.setText(obj.UGO_LastEditorPID);
        tvEditorTime.setText(TimeFormatUtil.format(obj.UGO_LastEditTime));
    }


    private void getIntentData() {
        Intent intent = this.getIntent();
        obj = (GreenObject)intent.getSerializableExtra("UGO");
    }

    private void setLocationButton() {
        floatingbtn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UGOInfoActivity.this, SimpleMapActivity.class);
                intent.putExtra("type", obj.UGO_ClassType_ID);
                intent.putExtra("location", obj.UGO_Geo_Location);
                intent.putExtra("name", obj.UGO_Name);
                startActivity(intent);
            }
        });
    }
}
