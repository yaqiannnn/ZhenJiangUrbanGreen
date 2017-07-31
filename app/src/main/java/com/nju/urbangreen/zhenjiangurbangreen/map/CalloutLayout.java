package com.nju.urbangreen.zhenjiangurbangreen.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by HCQIN on 2016/9/28.
 */
public class CalloutLayout extends LinearLayout implements View.OnClickListener{

    @BindView(R.id.btn_add_maintan_record)
    public Button btnAddMaintainRecord;

    @BindView(R.id.btn_add_inspect_record)
    public Button btnAddInspectRecord;

    @BindView(R.id.btn_add_event_record)
    public Button btnAddEventRecord;

    @BindView(R.id.btn_basic_info)
    public Button btnBasicInfo;

    public CalloutLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.map_callout,this);
        ButterKnife.bind(this);

        // btnAddMaintainRecord = (Button) findViewById(R.id.btn_add_maintan_record);
        // btnAddInspectRecord = (Button) findViewById(R.id.btn_add_inspect_record);
        // btnAddEventRecord = (Button) findViewById(R.id.btn_add_event_record);
        // btnBasicInfo = (Button) findViewById(R.id.btn_basic_info);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_maintan_record:
                break;
            case R.id.btn_add_inspect_record:
                break;
            case R.id.btn_add_event_record:
                break;
            case R.id.btn_basic_info:
                break;
            default:
                break;
        }
    }
}
