package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.attachments.AttachmentListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.widget.DropdownEditText;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MaintainInfoActivity extends AppCompatActivity {

    private TitleBarLayout titleBarLayout;//标题栏
    private TextView tvMaintainID;
    private TextView tvMaintainCode;
    private EditText etMaintainDate;
    private DropdownEditText dropdownMaintainType;
    private DatePickerDialog dtpckMaintainDate;
    private EditText etMaintainStaff;
    private EditText etMaintainCompany;
    private EditText etMaintainContent;
    private TextView tvLoggerID;
    private TextView tvLogTime;
    private TextView tvLasEditorPID;
    private AppCompatButton btnAttachment;


    private MaintainObject myObject;
    private int updateState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActivityCollector.addActivity(this);
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        if(intent.getSerializableExtra("MaintainInfo")==null)
        {
            //todo add new Object
            myObject=new MaintainObject("保存时自动生成","MR201701030002");
        }
        else
            myObject=(MaintainObject)intent.getSerializableExtra("MaintainInfo");

        setContentView(R.layout.activity_maintain_register);
        tvMaintainID=(TextView)findViewById(R.id.tv_maintainInfo_ID);
        tvMaintainID.setText(myObject.getID());
        tvMaintainCode=(TextView)findViewById(R.id.tv_maintainInfo_code);
        tvMaintainCode.setText(myObject.getCode());

        dropdownMaintainType =(DropdownEditText)findViewById(R.id.droplist_maintainInfo_type);
        ArrayList<String> dropdownList = new ArrayList<>();
        dropdownList.addAll(Arrays.asList(getResources().getStringArray(R.array.maintainTypeDropList)));
        dropdownMaintainType.setDropdownList(dropdownList);
        dropdownMaintainType.setText(myObject.getMaintainType());

        etMaintainDate =(EditText)findViewById(R.id.et_maintainInfo_date);
        initDatePicker();
        etMaintainStaff=(EditText) findViewById(R.id.et_maintainInfo_staff);
        etMaintainStaff.setText(myObject.getMaintainStaff());
        etMaintainStaff.addTextChangedListener(new myTextWatcher(etMaintainStaff));
        etMaintainCompany=(EditText) findViewById(R.id.et_maintainInfo_companyID);
        etMaintainCompany.setText(myObject.getCompanyID());
        etMaintainContent=(EditText) findViewById(R.id.et_maintainInfo_content);
        etMaintainContent.setText(myObject.getContent());
        tvLoggerID=(TextView)findViewById(R.id.tv_maintainInfo_LoggerPID);
        tvLoggerID.setText(myObject.getLoggerPID());
        tvLogTime=(TextView)findViewById(R.id.tv_maintainInfo_LogTime);
        tvLogTime.setText(myObject.getLogTime());
        tvLasEditorPID=(TextView)findViewById(R.id.tv_maintainInfo_LastEditorPID);
        tvLasEditorPID.setText(myObject.getLastEditorPID());

        //初始化标题栏
        titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_maintainInfo_title_bar);
        titleBarLayout.setTitleText("养护记录登记");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏输入键盘
                InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
                processBack();
            }
        });
        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_up);
        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateState = 1;
            }
        });

        btnAttachment=(AppCompatButton)findViewById(R.id.btn_maintainInfo_attachment);
        btnAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MaintainInfoActivity.this, AttachmentListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onBackPressed()
    {
        processBack();
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
                etMaintainDate.setText(year+"-"+(month + 1)+"-"+dayOfMonth);
            }
        },year,month,day);
        etMaintainDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtpckMaintainDate.show();
            }
        });
        etMaintainDate.setText(year+"-"+(month + 1)+"-"+day);
    }

    private void processBack()
    {
        int emptyStatus=0;
        //个位数为1代表type为空，十位数为1代表staff为空
        if(dropdownMaintainType.getText().equals(""))
            emptyStatus+=1;
        if(etMaintainStaff.getText().toString().equals(""))
            emptyStatus+=10;

        //保证必填项不为空
        if(emptyStatus!=0)
        {
            if(emptyStatus%10==1)
                dropdownMaintainType.setEmptyWarning();
            else
                dropdownMaintainType.setCommonDrawable();
            if(emptyStatus>=10)
                etMaintainStaff.setBackground(getResources().getDrawable(R.drawable.bkg_edittext_empty));
            else
                etMaintainStaff.setBackground(getResources().getDrawable(R.drawable.bkg_edittext));

            AlertDialog.Builder builder=new AlertDialog.Builder(MaintainInfoActivity.this);
            builder.setTitle("温馨提示");
            builder.setMessage("有必填项为空，返回后相关信息不会保存");
            builder.setCancelable(false);
            builder.setPositiveButton("仍然退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        else
            finish();
    }

}
