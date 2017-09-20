package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InspectRegisterActivity extends BaseActivity {

    @BindView(R.id.tv_inspectInfo_ID)
    public AppCompatTextView tvInspectID;

    @BindView(R.id.tv_inspectInfo_Code)
    public AppCompatTextView tvInspectCode;

    @BindView(R.id.tv_inspectInfo_date)
    public AppCompatTextView tvInspectDate;

    @BindView(R.id.spn_inspectInfo_type)
    public AppCompatSpinner spnInspectType;

    @BindView(R.id.et_inspectInfo_inspector)    
    public TextInputLayout etInspector;

    @BindView(R.id.et_inspectInfo_score)
    public TextInputLayout etInspectScore;

    @BindView(R.id.et_inspectInfo_content)
    public TextInputLayout etInspectContent;

    @BindView(R.id.et_inspectInfo_opinion)
    public TextInputLayout etEtInspectOpinion;

    @BindView(R.id.Toolbar)
    public Toolbar mToolbar;

    public DatePickerDialog dtpckInspectDate;
    private String[] typeArray;
    private ArrayAdapter<String> typeAdapter;

    private Inspect myObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(intent.getSerializableExtra("InspectInfo")==null)
        {
            //todo add new Object
            myObject=new Inspect("new ID","new Code");
        }
        else
            myObject=(Inspect)intent.getSerializableExtra("InspectInfo");
        ButterKnife.bind(this);

        setContentView(R.layout.activity_inspect_register);
        tvInspectID.setText(myObject.getID());
        tvInspectCode.setText(myObject.getCode());
        initSpinner();
        initDatePicker();
        etInspector.getEditText().setText(myObject.getInspector());
        etInspectScore.getEditText().setText(myObject.getScore());
        etInspectContent.getEditText().setText(myObject.getContent());
        etEtInspectOpinion.getEditText().setText(myObject.getInspectOpinion());
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
        typeArray=getResources().getStringArray(R.array.insepctTypeDropList);
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
               tvInspectDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
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
                InspectRegisterActivity.this.finish();
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
