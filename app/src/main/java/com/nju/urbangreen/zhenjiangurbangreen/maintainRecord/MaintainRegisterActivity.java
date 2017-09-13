package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.attachments.AttachmentListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.ugo.UgoListActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ACache;
import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.widget.DropdownEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaintainRegisterActivity extends BaseActivity {

    @BindView(R.id.tv_maintainInfo_ID)
    TextView tvMaintainID;
    @BindView(R.id.et_maintainInfo_date)
    EditText etMaintainDate;
    @BindView(R.id.droplist_maintainInfo_type)
    DropdownEditText dropdownMaintainType;
    @BindView(R.id.et_maintainInfo_staff)
    EditText etMaintainStaff;
    @BindView(R.id.et_maintainInfo_content)
    EditText etMaintainContent;
    @BindView(R.id.ly_maintain_info_table)
    TableLayout lyMaintainInfoTable;
    @BindView(R.id.Toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_maintain_register_submit)
    AppCompatButton btnMaintainRegisterSubmit;


    public DatePickerDialog dtpckMaintainDate;
    private Maintain maintainObject;
    private int updateState;
    private String ugoIds;
    private String maintainId;

    public static int CLICK_BACK_BUTTON = 0;
    public static int CLICK_UPLOAD_BUTTON = 1;
    private static final String TAG = "MaintainRegisterActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent = getIntent();
//        if (intent.getSerializableExtra("MaintainInfo") == null) {
//            //todo add new Object
////            myObject=new Maintain("保存时自动生成","MR201701030002");
//        } else
//            myObject = (Maintain) intent.getSerializableExtra("MaintainInfo");
        setContentView(R.layout.activity_maintain_register);
        ButterKnife.bind(this);
//        tvMaintainID.setText(myObject.getID());
//        tvMaintainCode.setText(myObject.getCode());
        initToolbar();

        ArrayList<String> dropdownList = new ArrayList<>();
        dropdownList.addAll(Arrays.asList(getResources().getStringArray(R.array.maintainTypeDropList)));
        dropdownMaintainType.setDropdownList(dropdownList);
//        dropdownMaintainType.setText(myObject.getMaintainType());

        initDatePicker();
        getMaintainObject();
        upload();

//        etMaintainStaff.setText(myObject.getMaintainStaff());
//        etMaintainStaff.addTextChangedListener(new myTextWatcher(etMaintainStaff));
//        etMaintainCompany.setText(myObject.getCompanyID());
//        etMaintainContent.setText(myObject.getContent());
//        tvLoggerID.setText(myObject.getLoggerPID());
//        tvLogTime.setText(myObject.getLogTime());
//        tvLasEditorPID.setText(myObject.getLastEditorPID());

//        //初始化标题栏
//        titleBarLayout.setTitleText("养护记录登记");
//        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //隐藏输入键盘
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//                processBack();
//            }
//        });
//        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_up);
//        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateState = 1;
//            }
//        });
    }

    private void getMaintainObject() {
        Intent intent = getIntent();
        Serializable serializableObject = intent.getSerializableExtra("maintain_object");
        if (serializableObject != null) {
            maintainObject = (Maintain) serializableObject;
            tvMaintainID.setText(maintainObject.MR_ID);
            dropdownMaintainType.setText(maintainObject.MR_MaintainType);
            etMaintainDate.setText(maintainObject.MR_MaintainDate);
            etMaintainStaff.setText(maintainObject.MR_MaintainStaff);
            etMaintainContent.setText(maintainObject.MR_MaintainContent);
            maintainId = maintainObject.MR_ID;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_maintain, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.attachment:
                Intent intent = new Intent(MaintainRegisterActivity.this, AttachmentListActivity.class);
                if (maintainId != null)
                    intent.putExtra("id", maintainId);
                startActivity(intent);
                break;
            case R.id.greenObjects:
                Intent intent2 = new Intent(MaintainRegisterActivity.this, UgoListActivity.class);
                if (maintainId != null)
                    intent2.putExtra("id", maintainId);
                startActivity(intent2);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        validateEmpty(CLICK_BACK_BUTTON);
    }

    private void initToolbar() {
        toolbar.setTitle("管养记录登记");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initDatePicker() {
        Calendar currentCalendar;
        int year, month, day;
        currentCalendar = Calendar.getInstance();
        year = currentCalendar.get(Calendar.YEAR);
        month = currentCalendar.get(Calendar.MONTH) + 1;
        day = currentCalendar.get(Calendar.DAY_OF_MONTH);
        dtpckMaintainDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etMaintainDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        etMaintainDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dtpckMaintainDate.show();
            }
        });
        etMaintainDate.setText(year + "-" + (month + 1) + "-" + day);
    }

    //上传（提交）表单
    private void upload() {
        btnMaintainRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] errMsg = new String[1];
                if (validateEmpty(CLICK_UPLOAD_BUTTON)) {
                    outputObject();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Boolean res;
                            if (tvMaintainID.getText() == "") {
                                res = WebServiceUtils.AddMaintainRecord(errMsg, maintainObject);
                            } else {
                                res = WebServiceUtils.UpdateMaintainRecord(errMsg, maintainObject);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (res) {
                                        Toast.makeText(MaintainRegisterActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("upload_status",true);
                                        setResult(RESULT_OK,intent);
                                        finish();
                                    } else {
                                        Toast.makeText(MaintainRegisterActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    ).start();
                }
            }
        });
    }


    private void outputObject() {
        maintainObject = new Maintain();
        maintainObject.MR_MaintainType = dropdownMaintainType.getText();
        maintainObject.MR_MaintainDate = etMaintainDate.getText().toString();
        maintainObject.MR_MaintainStaff = etMaintainStaff.getText().toString();
        if (etMaintainContent.getText() != null) {
            maintainObject.MR_MaintainContent = etMaintainContent.getText().toString();
        }
        if(tvMaintainID.getText()!=null){
            maintainObject.MR_ID = tvMaintainID.getText().toString();
        }
        maintainObject.UGO_IDs = getUGOIDs();
    }


    //验证是否为空
    private boolean validateEmpty(int flag) {
        int emptyStatus = 0;
        //个位数为1代表type为空，十位数为1代表staff为空
        if (dropdownMaintainType.getText().equals(""))
            emptyStatus += 1;
        if (etMaintainStaff.getText().toString().equals(""))
            emptyStatus += 10;

        //保证必填项不为空
        if (emptyStatus != 0) {
            if (emptyStatus % 10 == 1)
                dropdownMaintainType.setEmptyWarning();
            else
                dropdownMaintainType.setCommonDrawable();
            if (emptyStatus >= 10)
                etMaintainStaff.setBackground(getResources().getDrawable(R.drawable.bkg_edittext_empty));
            else
                etMaintainStaff.setBackground(getResources().getDrawable(R.drawable.bkg_edittext));
            showPrompt(flag);
            return false;
        } else {
            return true;
        }
    }

    //必填项为空错误提示
    private void showPrompt(int flag) {
        if (flag == CLICK_BACK_BUTTON) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MaintainRegisterActivity.this);
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
        } else {
            Toast.makeText(this, "有必填项为空，无法提交", Toast.LENGTH_SHORT).show();
        }
    }

    //从缓存中读取相关对象
    private String getUGOIDs() {
//        ACache mCache = ACache.get(this);
//        List<GreenObject> ugoSelectedList = mCache.getAsObjectList("ugo_select");
        List<GreenObject> ugoSelectedList = CacheUtil.getRelatedUgos();
        StringBuilder builder = new StringBuilder();

        if (ugoSelectedList != null) {
            for (GreenObject o : ugoSelectedList) {
                if (builder.length() != 0) {
                    builder.append(",");
                }
                builder.append(o.UGO_ID);
            }
            Log.d("tag", builder.toString());
        }
        return builder.toString();
    }

}
