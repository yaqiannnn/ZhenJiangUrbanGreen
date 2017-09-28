package com.nju.urbangreen.zhenjiangurbangreen.events;

import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;

public class EventRegisterActivity extends BaseActivity {

<<<<<<< HEAD
    private int state;

    @BindView(R.id.btn_event_register_add_ugo)
    AppCompatButton btnEventRegisterAddUgo;
    @BindView(R.id.edit_event_register_code)
    public TextView etCode;
    @BindView(R.id.edit_event_register_name)
    public EditText etName;
    @BindView(R.id.edit_event_register_location)
    public EditText etLocation;
    @BindView(R.id.edit_event_register_damage_degree)
    public EditText etDamageDegree;
    @BindView(R.id.edit_event_register_lost_fee)
    public EditText etLostFee;
    @BindView(R.id.edit_event_register_compensation)
    public EditText etCompensation;
    @BindView(R.id.edit_event_register_relevant_person)
    public EditText etRelevantPerson;
    @BindView(R.id.edit_event_register_relevant_license_plate)
    public EditText etRelevantLicensePlate;
    @BindView(R.id.edit_event_register_relevant_contact)
    public EditText etRelevantContact;
    @BindView(R.id.edit_event_register_relevant_company)
    public EditText etRelevantCompany;
    @BindView(R.id.edit_event_register_relevant_address)
    public EditText etRelevantAddress;
    @BindView(R.id.edit_event_register_description)
    public EditText etDescription;
    @BindView(R.id.edit_event_register_reason)
    public EditText etReason;
    @BindView(R.id.edit_event_register_relevant_description)
    public EditText etRelevantDescription;

    @BindView(R.id.edit_event_register_time)
    public EditText etDateSelect;//日期选择编辑框
    private DatePickerDialog dtpckDateSelect;//日期选择dialog
    @BindView(R.id.ly_events_register_title_bar)
    public TitleBarLayout titleBarLayout;//标题栏
    @BindView(R.id.droplist_event_register_type)
    public DropdownEditText dropdownEditText;//事件类型选择的可编辑下拉列表，（已封装，可复用）
    @BindView(R.id.btn_event_register_add_files)
    public Button btnAddAttachment;
    private OneEvent oneEvent;//如果是从详情按钮启动的本活动，则需要获取一些事件的信息
    private List<GreenObject> ugoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);
        initViews();

    }

    public void initViews() {

        Intent intent = getIntent();
        oneEvent = (OneEvent) intent.getSerializableExtra("event");
        ButterKnife.bind(this);
        titleBarLayout.setTitleText("事件登记");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //隐藏软件盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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
        ArrayList<String> dropdownList = new ArrayList<>();
        dropdownList.addAll(Arrays.asList(getResources().getStringArray(R.array.eventTypeDropList)));
        dropdownEditText.setDropdownList(dropdownList);

        //初始化日期选择框
        Calendar calendar = Calendar.getInstance();
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dtpckDateSelect = new DatePickerDialog(EventRegisterActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etDateSelect.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);

        etDateSelect.setText(year + "-" + (month + 1) + "-" + day);
        etDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dtpckDateSelect.show();
            }
        });
        if (oneEvent != null) {
            addDataToViews(oneEvent);
        }
        //初始化添加附件按钮
        btnAddAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventRegisterActivity.this, AttachmentListActivity.class);
                startActivity(intent);
            }
        });
        //添加相关养护对象按钮点击事件
        btnEventRegisterAddUgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventRegisterActivity.this,UgoListActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode){
//            case 1:
//                if (resultCode == RESULT_OK) {
//                     ugoList = (List<GreenObject>) data.getSerializableExtra("selectUgoList");
//                }
//                break;
//            default:
//        }
//    }

    @Override
    public void onBackPressed() {
        processBack();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //如果用户在登记了一些信息后没有上传就返回了，需要将其数据保存到数据库中
    public void saveTempViewData() {
        OneEvent tempEvent = new OneEvent();
        tempEvent.setUGE_Code(etCode.getText().toString());
        tempEvent.setUGE_Name(etName.getText().toString());
        tempEvent.setUGE_Type(dropdownEditText.getText());
        tempEvent.setUGE_Location(etLocation.getText().toString());
        tempEvent.setUGE_Time(etDateSelect.getText().toString());
        tempEvent.setUGE_DamageDegree(etDamageDegree.getText().toString());

        tempEvent.setUGE_LostFee(etLostFee.getText().toString());
        tempEvent.setUGE_Compensation(etCompensation.getText().toString());
        tempEvent.setUGE_RelevantPerson(etRelevantPerson.getText().toString());
        tempEvent.setUGE_RelevantLicensePlate(etRelevantLicensePlate.getText().toString());
        tempEvent.setUGE_RelevantContact(etRelevantContact.getText().toString());
        tempEvent.setUGE_RelevantCompany(etRelevantCompany.getText().toString());
        tempEvent.setUGE_RelevantAddress(etRelevantAddress.getText().toString());
        tempEvent.setUGE_RelevantDescription(etRelevantDescription.getText().toString());
        tempEvent.setUGE_Description(etDescription.getText().toString());
        tempEvent.setUGE_Reason(etReason.getText().toString());
        tempEvent.setRegistrar("xk");
        tempEvent.setState(state);
        UrbanGreenDB urbanGreenDB = UrbanGreenDB.getInstance(EventRegisterActivity.this);
        urbanGreenDB.saveEvent(tempEvent);
    }

    private void addDataToViews(OneEvent oneEvent) {
        etCode.setText(oneEvent.getUGE_Code());
        etName.setText(oneEvent.getUGE_Name());
        dropdownEditText.setText(oneEvent.getUGE_Type());
        etLocation.setText(oneEvent.getUGE_Location());
        etDateSelect.setText(oneEvent.getUGE_Time().toString());
        etDamageDegree.setText(oneEvent.getUGE_DamageDegree());
        etLostFee.setText(oneEvent.getUGE_LostFee());
        etCompensation.setText(oneEvent.getUGE_Compensation());
        etRelevantPerson.setText(oneEvent.getUGE_RelevantPerson());
        etRelevantLicensePlate.setText(oneEvent.getUGE_RelevantLicensePlate());
        etRelevantContact.setText(oneEvent.getUGE_RelevantContact());
        etRelevantCompany.setText(oneEvent.getUGE_RelevantCompany());
        etRelevantAddress.setText(oneEvent.getUGE_RelevantAddress());
        etDescription.setText(oneEvent.getUGE_Description());
        etReason.setText(oneEvent.getUGE_Reason());
        etRelevantDescription.setText(oneEvent.getUGE_RelevantDescription());
    }
=======
>>>>>>> 38eaaad13923791142ee2eda7047286e04332708

}
