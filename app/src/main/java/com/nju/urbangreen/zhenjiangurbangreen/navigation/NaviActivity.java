package com.nju.urbangreen.zhenjiangurbangreen.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import butterknife.BindView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.inspectRecord.InspectListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.maintainRecord.MaintainListActivity2;
import com.nju.urbangreen.zhenjiangurbangreen.settings.SettingsActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

public class NaviActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.ly_navi_title_bar)
    public TitleBarLayout titleBarLayout;

    @BindView(R.id.imgbtn_settings)
    public ImageButton imgBtnSettings;

    @BindView(R.id.imgbtn_events_record)
    public ImageButton imgBtnEvents;

    @BindView(R.id.imgbtn_maintain_record)
    public ImageButton imgBtnMaintain;

    @BindView(R.id.imgbtn_inspection_record)
    public ImageButton imgBtnInspect;

    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        // imgBtnSettings = (ImageButton) findViewById(R.id.imgbtn_settings);
        imgBtnSettings.setOnClickListener(this);

        // imgBtnEvents = (ImageButton) findViewById(R.id.imgbtn_events_record);
        imgBtnEvents.setOnClickListener(this);

        // imgBtnMaintain = (ImageButton) findViewById(R.id.imgbtn_maintain_record);
        imgBtnMaintain.setOnClickListener(this);

        // imgBtnInspect = (ImageButton) findViewById(R.id.imgbtn_inspection_record);
        imgBtnInspect.setOnClickListener(this);

        // titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_navi_title_bar);
        titleBarLayout.setTitleText("功能选择");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbtn_settings:
                intent = new Intent(NaviActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.imgbtn_events_record:
                intent = new Intent(NaviActivity.this, EventListActivity.class);
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
