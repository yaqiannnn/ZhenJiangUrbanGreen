package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.Calendar;

public class MaintainInfoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private AppCompatTextView tvMaintainDate;
    private AppCompatSpinner spnMaintainType;
    private DatePickerDialog dtpckMaintainDate;
    private TextInputLayout etMaintainContent;

    private String[] typeArray;
    private ArrayAdapter<String> typeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_info);
        spnMaintainType =(AppCompatSpinner)findViewById(R.id.spn_maintainInfo_type);
        initSpinner();
        tvMaintainDate=(AppCompatTextView)findViewById(R.id.tv_maintainInfo_date);
        initDatePicker();
        etMaintainContent=(TextInputLayout)findViewById(R.id.et_maintainInfo_content);
        mToolbar=(Toolbar)findViewById(R.id.Toolbar);
        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_uploadinfo,menu);
        return true;
    }

    private void initSpinner()
    {
        typeArray=getResources().getStringArray(R.array.maintainTypeSpinner);
        typeAdapter=new ArrayAdapter<>(this,R.layout.spinner_item,typeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMaintainType.setAdapter(typeAdapter);
        spnMaintainType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spnMaintainType.setVisibility(View.VISIBLE);
    }

    private void initDatePicker()
    {
        Calendar currentCalendar;
        int year,month,day;
        currentCalendar=Calendar.getInstance();
        year=currentCalendar.get(Calendar.YEAR);
        month=currentCalendar.get(Calendar.MONTH)+1;
        day=currentCalendar.get(Calendar.DAY_OF_MONTH);
        dtpckMaintainDate=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvMaintainDate.setText(year+"-"+month+"-"+dayOfMonth);
            }
        },year,month,day);
        tvMaintainDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtpckMaintainDate.show();
            }
        });
        tvMaintainDate.setText(year+"-"+month+"-"+day);
    }

    void initToolbar()
    {
        mToolbar.setTitle("养护记录登记");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorBackground));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaintainInfoActivity.this.finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_toolbar_item_upload:
                        //todo upload function
                        Log.d("Upload info","maintain info");
                        break;
                }
                return true;
            }
        });
    }
}
