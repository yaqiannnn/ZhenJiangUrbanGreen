package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.Calendar;

public class InspectInfoActivity extends AppCompatActivity {

    private AppCompatTextView tvInspectDate;
    private AppCompatSpinner spnInspectType;
    private DatePickerDialog dtpckInspectDate;
    private TextInputLayout etInspectScore;
    private TextInputLayout etInspectContent;
    private TextInputLayout etEtInspectOpinion;
    private Toolbar mToolbar;

    private String[] typeArray;
    private ArrayAdapter<String> typeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_info);
        initSpinner();
        tvInspectDate=(AppCompatTextView)findViewById(R.id.tv_inspectInfo_date);
        initDatePicker();
        etInspectScore=(TextInputLayout)findViewById(R.id.et_inspectInfo_score);
        etInspectContent=(TextInputLayout)findViewById(R.id.et_inspectInfo_content);
        etEtInspectOpinion=(TextInputLayout)findViewById(R.id.et_inspectInfo_opinion);
        mToolbar=(Toolbar)findViewById(R.id.Toolbar);
        initToolbar();
    }

    private void initSpinner()
    {
        spnInspectType =(AppCompatSpinner)findViewById(R.id.spn_inspectInfo_type);
        typeArray=getResources().getStringArray(R.array.typeSpinner);
        typeAdapter=new ArrayAdapter<>(this,R.layout.spinner_item,typeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnInspectType.setAdapter(typeAdapter);
        spnInspectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spnInspectType.setVisibility(View.VISIBLE);
    }

    private void initDatePicker()
    {
        Calendar currentCalendar;
        int year,month,day;
        currentCalendar=Calendar.getInstance();
        year=currentCalendar.get(Calendar.YEAR);
        month=currentCalendar.get(Calendar.MONTH)+1;
        day=currentCalendar.get(Calendar.DAY_OF_MONTH);
        dtpckInspectDate=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               tvInspectDate.setText(year+"-"+month+"-"+dayOfMonth);
            }
        },year,month,day);
        tvInspectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtpckInspectDate.show();
            }
        });
        tvInspectDate.setText(year+"-"+month+"-"+day);
    }
    void initToolbar()
    {
        mToolbar.setTitle("巡查记录登记");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorBackground));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectInfoActivity.this.finish();
            }
        });
    }

}
