package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseListAdapter;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.widget.LoadMoreFooterView;
import com.nju.urbangreen.zhenjiangurbangreen.widget.RefreshHeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListActivity extends BaseActivity {

    @BindView(R.id.Toolbar)
    Toolbar toolbar;
    @BindView(R.id.material_search_view)
    MaterialSearchView searchView;
    @BindView(R.id.spinner1)
    Spinner spinner1;
    @BindView(R.id.spinner2)
    Spinner spinner2;
    @BindView(R.id.spinner3)
    Spinner spinner3;
//    @BindView(R.id.spinner4)
//    Spinner spinner4;
    public ViewPager pager;
    @BindView(R.id.floatingbtn_add_event)
    public FloatingActionButton fbtnAddEvent;//悬浮按钮
    @BindView(R.id.swipe_target)
    RecyclerView recyclerEventList;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;

    public static final int GET_REGISTER_RESULT = 1;
    public EventListAdapter2 adapter2;//eventList的适配器
    private List<OneEvent> eventList = new ArrayList<>();
    final Map<String, Object> multiQuery = new HashMap<>();

    private String id;
    private int page = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        initViews();
        initRecyclerView();

        //获取MapActivity传入的UGO_Ucode
        Intent intent = getIntent();
        id = intent.getStringExtra("UGO_Ucode");

        getEvent(multiQuery);

    }

    @Override
    protected void onStart() {
        super.onStart();
        CacheUtil.removeRelatedUgos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case GET_REGISTER_RESULT:
                if(resultCode == RESULT_OK){
                    getEvent(multiQuery);
                }
                break;
            default:
        }

    }


    //初始化控件
    public void initViews(){
        ButterKnife.bind(this);
        toolbar.setTitle("事件记录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //底部红色加号点击事件，点击红色加号弹出菜单
        fbtnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(EventListActivity.this,EventRegisterActivity.class);
//                startActivity(intent);
                showPopupMenu(fbtnAddEvent);
            }
        });


        //spinner选择点击事件
        //spinner1为类型选择
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.eventType);

                if(pos>0){
                        multiQuery.put("eventType", languages[pos]);
                }
                if(pos==0&&multiQuery.containsKey("eventType")){
                    multiQuery.remove("eventType");

                }
                getEvent(multiQuery);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //spinner2为时间选择
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.eventDate);
                if(pos>0){
                    multiQuery.put("date",languages[pos]);
                }
                if(pos==0&&multiQuery.containsKey("date")){

                    multiQuery.remove("date");
                }
                getEvent(multiQuery);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //spinner3为是否完结选择
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                String[] languages = getResources().getStringArray(R.array.eventStatus);
                if(pos==1){
                    multiQuery.put("is_conclude",true);
                }else if(pos==2){
                    multiQuery.put("is_conclude",false);
                }
                if(pos==0&&multiQuery.containsKey("is_conclude")){
                    multiQuery.remove("is_conclude");

                }
                getEvent(multiQuery);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

//        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int pos, long id) {
//                String[] languages = getResources().getStringArray(R.array.is_activity);
//                if(pos==1){
//                    multiQuery.put("is_activity",false);
//                }else if(pos==2){
//                    multiQuery.put("is_activity",true);
//                }
//                if(pos==0&&multiQuery.containsKey("is_activity")){
//                    multiQuery.remove("is_activity");
//
//                }
//                getEvent(multiQuery);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Another interface callback
//            }
//        });

        //下拉刷新
        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEvent(multiQuery);
            }
        });

        //上拉刷新
        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getEvent(multiQuery,page, 8);
                page++;
            }
        });

    }

    //顶部工具栏添加spinner和搜索
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //添加搜索
        getMenuInflater().inflate(R.menu.menu_toolbar_event_search, menu);
        MenuItem item = menu.findItem(R.id.menu_toolbar_item_search);

        //添加spinner
        MenuItem spinnerItem = menu.findItem(R.id.event_spinner);
        Spinner event_spinner = (Spinner) MenuItemCompat.getActionView(spinnerItem);
        String[] data = {"事件", "活动"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        event_spinner.setAdapter(adapter);
        //spinner点击事件
        event_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    multiQuery.put("is_activity", false);

                } else {
                    multiQuery.put("is_activity", true);
                }
                getEvent(multiQuery);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }


    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerEventList.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerEventList.getContext(),
                linearLayoutManager.getOrientation());
        recyclerEventList.addItemDecoration(dividerItemDecoration);
        adapter2 = new EventListAdapter2(eventList);
        recyclerEventList.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    //获得列表第一页数据
    private void getEvent(final Map<String, Object> query) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("加载数据中，请稍候...");
        loading.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                String[] errMsg = new String[1];
                query.put("page", 1);
                query.put("limit", 8);

                if(id != null){
                    query.put("UGO_ID", id);
                }

                final List<OneEvent> tempList = WebServiceUtils.getEvent(query, errMsg);

                if (tempList != null) {
                    eventList.clear();
                    eventList.addAll(tempList);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(tempList!=null){

                            findViewById(R.id.task_list_emptyview).setVisibility(View.INVISIBLE);
                            findViewById(R.id.floatingbtn_add_event).setVisibility(View.VISIBLE);
                        }
                        if(tempList==null){

                            eventList.clear();
                            findViewById(R.id.floatingbtn_add_event).setVisibility(View.INVISIBLE);
                            findViewById(R.id.task_list_emptyview).setVisibility(View.VISIBLE);
                        }
                        loading.dismiss();
                        adapter2.notifyDataSetChanged();
                        swipeToLoadLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private List<OneEvent> getEvent(final Map<String, Object> query, final int page, final int limit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] errMsg = new String[1];
                query.put("page", page);
                query.put("limit", limit);
                if(id != null){
                    query.put("UGO_ID", id);
                }
                final List<OneEvent> newEventList = WebServiceUtils.getEvent(query, errMsg);
                if (newEventList != null) {
                    eventList.addAll(newEventList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter2.notifyDataSetChanged();
                            swipeToLoadLayout.setLoadingMore(false);
                            if (newEventList.size() < limit) {
                                swipeToLoadLayout.setLoadMoreEnabled(false);
                                Toast.makeText(EventListActivity.this, "加载完毕", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
        return eventList;
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = new Intent(EventListActivity.this , EventRegisterActivity.class);
                startActivity(i);

                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });

        popupMenu.show();
    }
}


