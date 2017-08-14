package com.nju.urbangreen.zhenjiangurbangreen.ugo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.goyourfly.multiple.adapter.MultipleAdapter;
import com.goyourfly.multiple.adapter.MultipleSelect;
import com.goyourfly.multiple.adapter.StateChangeListener;
import com.goyourfly.multiple.adapter.menu.SimpleDeleteMenuBar;
import com.goyourfly.multiple.adapter.viewholder.view.CheckBoxFactory;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.search.SearchActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ACache;
import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UgoListActivity extends BaseActivity {

    @BindView(R.id.add_ugo_title_bar)
    TitleBarLayout addUgoTitleBarLayout;
    @BindView(R.id.recycler_ugo_list)
    RecyclerView recyclerUgoList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    private List<GreenObject> ugObjectList = new ArrayList<>();
    private UgoListAdapter adapter;
    private ProgressDialog progressDialog;
    private MultipleAdapter multipleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ugo_list);
        ButterKnife.bind(this);

        addUgoTitleBarLayout.setTitleText("养护对象列表");
        addUgoTitleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.putExtra("selectUgoList", (Serializable) ugObjectList);
//                setResult(RESULT_OK, intent);
                writeToCache();
//                CacheUtil.putUGOs(ugObjectList);
                finish();
            }
        });
        addUgoTitleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_add);
        addUgoTitleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UgoListActivity.this, SearchUgoActivity.class);
                startActivityForResult(intent, 1);
            }
        });
//        initUgos();
        initRecyclerView();
        readFromCache();
//        CacheUtil.getUGOs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addUgoTitleBarLayout.recoverReceiver != null) {
            unregisterReceiver(addUgoTitleBarLayout.recoverReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
//                    String returnData = data.getStringExtra("selectUgos");
                    List<GreenObject> tempList = (List<GreenObject>) data.getSerializableExtra("selectUgo");
                    ugObjectList.addAll(tempList);
                    multipleAdapter.notifyDataSetChanged();
                }
                break;
            default:
        }
    }

    //刷新养护对象数据
    private void refreshUgos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //本函数在从服务端获取数据时有用
    private void initUgos() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载列表");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] errMsg = new String[1];
                try {
                    ugObjectList = WebServiceUtils.getUGOInfoExceptST(errMsg);
                    Log.d("test", ugObjectList + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initRecyclerView();
                    }
                });
            }
        }).start();

    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUgoList.setLayoutManager(linearLayoutManager);
        adapter = new UgoListAdapter(ugObjectList);
        multipleAdapter = MultipleSelect.with(this)
                .adapter(adapter)
                .decorateFactory(new CheckBoxFactory(Color.BLUE))
                .linkList(ugObjectList)
                .customMenu(new SimpleDeleteMenuBar(this, R.color.colorPrimary, Gravity.BOTTOM))
                .build();
        recyclerUgoList.setAdapter(multipleAdapter);

        //列表项分隔线
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerUgoList.getContext(),
                linearLayoutManager.getOrientation());
        recyclerUgoList.addItemDecoration(dividerItemDecoration);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUgos();
            }
        });
    }

    private void writeToCache() {
        ACache mCache = ACache.get(this);
        mCache.put("ugo_select", ugObjectList.toArray());
    }

    private void readFromCache() {
        ACache mCache = ACache.get(this);
        List<GreenObject> tempList = mCache.getAsObjectList("ugo_select");
        if (tempList != null) {
            ugObjectList.addAll(tempList);
            multipleAdapter.notifyDataSetChanged();
        }
    }
}
