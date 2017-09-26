package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.io.Serializable;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InspectRegisterActivity extends AppCompatActivity {


    public DatePickerDialog dtpckInspectDate;
    @BindView(R.id.Toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    @BindView(R.id.material_search_view)
    MaterialSearchView materialSearchView;
    @BindView(R.id.tv_inspectInfo_ID)
    AppCompatTextView tvInspectID;
    @BindView(R.id.tv_inspectInfo_Code)
    AppCompatTextView tvInspectCode;
    @BindView(R.id.spn_inspectInfo_type)
    AppCompatSpinner spnInspectType;
    @BindView(R.id.tv_inspectInfo_date)
    AppCompatTextView tvInspectDate;
    @BindView(R.id.et_inspectInfo_inspector)
    TextInputLayout etInspector;
    @BindView(R.id.et_inspectInfo_score)
    TextInputLayout etInspectScore;
    @BindView(R.id.et_inspectInfo_content)
    TextInputLayout etInspectContent;
    @BindView(R.id.et_inspectInfo_opinion)
    TextInputLayout etEtInspectOpinion;
    @BindView(R.id.tv_inspectInfo_LoggerPID)
    AppCompatTextView tvInspectLoggerPID;
    @BindView(R.id.tv_inspectInfo_LogTime)
    AppCompatTextView tvInspectInfoLogTime;
    @BindView(R.id.tv_inspectInfo_LastEditorPID)
    AppCompatTextView tvInspectLastEditorPID;
    private String[] typeArray;
    private ArrayAdapter<String> typeAdapter;

    private Inspect myObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect_register);
        ButterKnife.bind(this);
        initToolbar();


        initSpinner();
        initDatePicker();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_uploadinfo, menu);
        return true;
    }

    private void getMaintainObject() {
        Intent intent = getIntent();
        Serializable serializableObject = intent.getSerializableExtra("inspect_object");
        if (serializableObject != null) {
            tvInspectID.setText(myObject.getIR_ID());
            tvInspectCode.setText(myObject.getIR_Code());
            etInspector.getEditText().setText(myObject.getIR_Inspector());
            etInspectScore.getEditText().setText(myObject.getIR_Score());
            etInspectContent.getEditText().setText(myObject.getIR_Content());
            etEtInspectOpinion.getEditText().setText(myObject.getIR_InspectOpinion());
        }
    }

    private void initSpinner() {
        typeArray = getResources().getStringArray(R.array.insepctTypeDropList);
        typeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, typeArray);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnInspectType.setAdapter(typeAdapter);
        spnInspectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        int typeIndex = typeAdapter.getPosition(myObject.getIR_Type());
        if (typeIndex != -1)
            spnInspectType.setSelection(typeIndex);
        spnInspectType.setVisibility(View.VISIBLE);
    }

    private void initDatePicker() {
        Calendar currentCalendar;
        int year, month, day;
        currentCalendar = Calendar.getInstance();
        year = currentCalendar.get(Calendar.YEAR);
        month = currentCalendar.get(Calendar.MONTH) + 1;
        day = currentCalendar.get(Calendar.DAY_OF_MONTH);
        dtpckInspectDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tvInspectDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        tvInspectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtpckInspectDate.show();
            }
        });
        tvInspectDate.setText(year + "-" + (month + 1) + "-" + day);
    }

    void initToolbar() {
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
                switch (item.getItemId()) {
                    case R.id.menu_toolbar_item_upload:
                        //todo upload function
                        Log.d("Upload info", "inspect info");
                        break;
                }
                return true;
            }
        });
    }

}
