package com.nju.urbangreen.zhenjiangurbangreen.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventsActivity;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.settings.SettingsActivity;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

public class NaviActivity extends AppCompatActivity implements View.OnClickListener{

    private TitleBarLayout titleBarLayout;
    private ImageButton imgBtnSettings;
    private ImageButton imgBtnEvents;
    private ImageButton imgBtnMaintain;
    private ImageButton imgBtnInspect;

    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        imgBtnSettings = (ImageButton) findViewById(R.id.imgbtn_settings);
        imgBtnSettings.setOnClickListener(this);

        imgBtnEvents = (ImageButton) findViewById(R.id.imgbtn_events_record);
        imgBtnEvents.setOnClickListener(this);

        imgBtnMaintain = (ImageButton) findViewById(R.id.imgbtn_maintain_record);
        imgBtnMaintain.setOnClickListener(this);

        imgBtnInspect = (ImageButton) findViewById(R.id.imgbtn_inspection_record);
        imgBtnInspect.setOnClickListener(this);

        titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_navi_title_bar);
        titleBarLayout.setTitleText("功能选择");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbtn_settings:
                intent = new Intent(NaviActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.imgbtn_events_record:
                intent = new Intent(NaviActivity.this, EventsActivity.class);
                startActivity(intent);
                break;
            case R.id.imgbtn_maintain_record:
                intent = new Intent(NaviActivity.this, MaintainListActivity.class);
                startActivity(intent);
                break;
            case R.id.imgbtn_inspection_record:
                intent = new Intent(NaviActivity.this, InspectListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
