package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private InspectObject myObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        if(intent.getSerializableExtra("InspectInfo")==null)
        {
            //todo add new Object
            myObject=new InspectObject("new ID","new Code");
        }
        else
            myObject=(InspectObject)intent.getSerializableExtra("InspectInfo");

        setContentView(R.layout.activity_inspect_info);
        spnInspectType =(AppCompatSpinner)findViewById(R.id.spn_inspectInfo_type);
        initSpinner();
        tvInspectDate=(AppCompatTextView)findViewById(R.id.tv_inspectInfo_date);
        initDatePicker();
        etInspectScore=(TextInputLayout)findViewById(R.id.et_inspectInfo_score);
        etInspectScore.getEditText().setText(myObject.getScore());
        etInspectContent=(TextInputLayout)findViewById(R.id.et_inspectInfo_content);
        etInspectContent.getEditText().setText(myObject.getContent());
        etEtInspectOpinion=(TextInputLayout)findViewById(R.id.et_inspectInfo_opinion);
        etEtInspectOpinion.getEditText().setText(myObject.getInspectOpinion());
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
        typeArray=getResources().getStringArray(R.array.insepctTypeSpinner);
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
        int typeIndex=typeAdapter.getPosition(myObject.getInspectType());
        if(typeIndex!=-1)
            spnInspectType.setSelection(typeIndex);
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
        Date objectDate=myObject.getInspectDate();
        if(objectDate.toString().equals(""))
            tvInspectDate.setText(year+"-"+month+"-"+day);
        else
            tvInspectDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(objectDate));
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
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.menu_toolbar_item_upload:
                        //todo upload function
                        Log.d("Upload info","inspect info");
                        break;
                }
                return true;
            }
        });
    }

}
