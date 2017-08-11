package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.map.SimpleMapActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UGOInfoActivity extends BaseActivity {
    private GreenObject obj;

    @BindView(R.id.floatingbtn_UGO_location)
    public FloatingActionButton floatingbtn_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_ugoinfo);
        ButterKnife.bind(this);
        setLocationButton();
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
