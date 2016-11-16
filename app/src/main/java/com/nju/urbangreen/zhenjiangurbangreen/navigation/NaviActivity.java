package com.nju.urbangreen.zhenjiangurbangreen.navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.settings.SettingsActivity;

public class NaviActivity extends AppCompatActivity {

    private ImageButton imgBtnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);

        imgBtnSettings = (ImageButton) findViewById(R.id.imgbtn_settings);
        imgBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NaviActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
