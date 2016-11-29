package com.nju.urbangreen.zhenjiangurbangreen.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventsActivity;
import com.nju.urbangreen.zhenjiangurbangreen.settings.SettingsActivity;

public class NaviActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton imgBtnSettings;
    private ImageButton imgBtnEvents;
    Intent intent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        imgBtnSettings = (ImageButton) findViewById(R.id.imgbtn_settings);
        imgBtnSettings.setOnClickListener(this);

        imgBtnEvents = (ImageButton) findViewById(R.id.imgbtn_events_record);
        imgBtnEvents.setOnClickListener(this);

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
            default:
                break;
        }
    }
}
