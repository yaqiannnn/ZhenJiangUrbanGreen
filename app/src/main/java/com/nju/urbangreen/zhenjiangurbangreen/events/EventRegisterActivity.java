package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.attachments.AttachmentListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.util.SPUtils;
import com.nju.urbangreen.zhenjiangurbangreen.util.UrbanGreenDB;
import com.nju.urbangreen.zhenjiangurbangreen.widget.DropdownEditText;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class EventRegisterActivity extends AppCompatActivity {

    private int state;
    private EditText etCode;
    private EditText etName;
    private EditText etLocation;
    private EditText etDamageDegree;
    private EditText etLostFee;
    private EditText etCompensation;
    private EditText etRelevantPerson;
    private EditText etRelevantLicensePlate;
    private EditText etRelevantContact;
    private EditText etRelevantCompany;
    private EditText etRelevantAddress;
    private EditText etDescription;
    private EditText etReason;
    private EditText etRelevantDescription;


    private EditText etDateSelect;//日期选择编辑框
    private TitleBarLayout titleBarLayout;//标题栏
    private DropdownEditText dropdownEditText;//事件类型选择的可编辑下拉列表，（已封装，可复用）
    private int year, month, day;
    private Button btnAddAttachment;
    private OneEvent oneEvent;//如果是从详情按钮启动的本活动，则需要获取一些事件的信息
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
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_event_register);
        initViews();

    }

    public void initViews() {

        Intent intent = getIntent();
        oneEvent = (OneEvent) intent.getSerializableExtra("event");
        //初始化一些简单的编辑框
        etCode = (EditText) findViewById(R.id.edit_event_register_code);
        etName = (EditText) findViewById(R.id.edit_event_register_name);
        etLocation = (EditText) findViewById(R.id.edit_event_register_location);
        etDamageDegree = (EditText) findViewById(R.id.edit_event_register_damage_degree);
        etLostFee = (EditText) findViewById(R.id.edit_event_register_lost_fee);
        etCompensation = (EditText) findViewById(R.id.edit_event_register_compensation);
        etRelevantPerson = (EditText) findViewById(R.id.edit_event_register_relevant_person);
        etRelevantLicensePlate = (EditText) findViewById(R.id.edit_event_register_relevant_license_plate);
        etRelevantContact = (EditText) findViewById(R.id.edit_event_register_relevant_contact);
        etRelevantCompany = (EditText) findViewById(R.id.edit_event_register_relevant_company);
        etRelevantAddress = (EditText) findViewById(R.id.edit_event_register_relevant_address);
        etDescription = (EditText) findViewById(R.id.edit_event_register_description);
        etReason = (EditText) findViewById(R.id.edit_event_register_reason);
        etRelevantDescription = (EditText) findViewById(R.id.edit_event_register_relevant_description);


        //初始化标题栏
        titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_events_register_title_bar);
        titleBarLayout.setTitleText("事件登记");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //隐藏软件盘
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()){
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
                }
                processBack();
            }
        });
        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_up);
        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 1;
            }
        });

        //初始化可编辑下拉框
        dropdownEditText = (DropdownEditText) findViewById(R.id.droplist_register_type);
        ArrayList<String> drpList = new ArrayList<String>();
        drpList.add("自然灾害");
        drpList.add("病虫灾害");
        drpList.add("人为事故");
        drpList.add("交通事故");
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
        if(oneEvent != null){
            addDataToViews(oneEvent);
        }
        //初始化添加附件按钮
        btnAddAttachment = (Button) findViewById(R.id.btn_event_register_add_files);
        btnAddAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventRegisterActivity.this, AttachmentListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        processBack();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        Log.i("注册活动", "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //如果用户在登记了一些信息后没有上传就返回了，需要将其数据保存到数据库中
    public void saveTempViewData() {
        OneEvent oneEvent = new OneEvent();
        oneEvent.setCode(etCode.getText().toString());
        oneEvent.setName(etName.getText().toString());
        oneEvent.setType(dropdownEditText.getText());
        oneEvent.setLocation(etLocation.getText().toString());
        oneEvent.setDate_time(etDateSelect.getText().toString());
        oneEvent.setDamageDegree(etDamageDegree.getText().toString());

        oneEvent.setLostFee(etLostFee.getText().toString());
        oneEvent.setCompensation(etCompensation.getText().toString());
        oneEvent.setRelevantPerson(etRelevantPerson.getText().toString());
        oneEvent.setRelevantLicensePlate(etRelevantLicensePlate.getText().toString());
        oneEvent.setRelevantContact(etRelevantContact.getText().toString());
        oneEvent.setRelevantCompany(etRelevantCompany.getText().toString());
        oneEvent.setRelevantAddress(etRelevantAddress.getText().toString());
        oneEvent.setRelevantDescription(etRelevantDescription.getText().toString());
        oneEvent.setDescription(etDescription.getText().toString());
        oneEvent.setReason(etReason.getText().toString());
        //oneEvent.setRegistrar(SPUtils.get(this,"username","xk").toString());
        oneEvent.setRegistrar("xk");
        oneEvent.setState(state);
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(EventRegisterActivity.this);
        urbanGreenDB.saveEvent(oneEvent);
    }

    private void addDataToViews(OneEvent oneEvent) {
        etCode.setText(oneEvent.getCode());
        etName.setText(oneEvent.getName());
        dropdownEditText.setText(oneEvent.getType());
        etLocation.setText(oneEvent.getLocation());
        etDateSelect.setText(oneEvent.getDate_time());
        etDamageDegree.setText(oneEvent.getDamageDegree());
        etLostFee.setText(oneEvent.getLostFee());
        etCompensation.setText(oneEvent.getCompensation());
        etRelevantPerson.setText(oneEvent.getRelevantPerson());
        etRelevantLicensePlate.setText(oneEvent.getRelevantLicensePlate());
        etRelevantContact.setText(oneEvent.getRelevantContact());
        etRelevantCompany.setText(oneEvent.getRelevantCompany());
        etRelevantAddress.setText(oneEvent.getRelevantAddress());
        etDescription.setText(oneEvent.getDescription());
        etReason.setText(oneEvent.getReason());
        etRelevantDescription.setText(oneEvent.getRelevantDescription());
    }

    /**
     * 处理一些退出登记界面的事情
     */
    private void processBack(){
        //如果用户没有填写编号，就会弹框提示一下
        if(etCode.getText().toString().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示");
            builder.setMessage("您没有填写事件编号，返回后相关信息不会保存");
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
        else{
            if(oneEvent == null){//oneEvent为null，说明不是从详情按钮过来的
                Toast.makeText(this,"事件登记成功~",Toast.LENGTH_SHORT).show();
                saveTempViewData();

            }
            finish();
        }
    }
}
