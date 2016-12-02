package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.widget.DropdownEditText;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class EventRegisterActivity extends AppCompatActivity {

    private EditText etDateSelect;//日期选择编辑框
    private TitleBarLayout titleBarLayout;//标题栏
    private DropdownEditText dropdownEditText;//事件类型选择的可编辑下拉列表，（已封装，可复用）
    private int year, month, day;
    //日期变化监听
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1 + 1;
            day = i2;
            updateDate();
        }

        //更新日期选择编辑框的显示
        private void updateDate() {
            etDateSelect.setText(year + "-" + month + "-" + day);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

        initViews();

    }

    public void initViews() {
        //初始化标题栏
        titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_events_register_title_bar);
        titleBarLayout.setTitleText("事件登记");

        //初始化可编辑下拉框
        dropdownEditText = (DropdownEditText) findViewById(R.id.droplist_register_type);
        ArrayList<String> drpList = new ArrayList<String>();
        drpList.add("naruto");
        drpList.add("sakura");
        drpList.add("gara");
        dropdownEditText.setDropdownList(drpList);

        //初始化日期选择框
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        etDateSelect = (EditText) findViewById(R.id.edit_event_register_time);
        etDateSelect.setText(year + "-" + month + "-" + day);

        etDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dateSelectDlg = new DatePickerDialog(EventRegisterActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month - 1, day);

                dateSelectDlg.show();
            }
        });
    }

}
