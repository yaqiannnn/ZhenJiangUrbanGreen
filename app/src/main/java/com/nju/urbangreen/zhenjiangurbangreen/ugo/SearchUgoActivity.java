package com.nju.urbangreen.zhenjiangurbangreen.ugo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObjectSug;
import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.PermissionsUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchUgoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.recycler_ugo_search_result)
    RecyclerView recyclerUgoSearchResult;

    private String sugIDs[];
    private String sugAddresses[];
    private ProgressDialog loadingDialog;
    private boolean isSearchOptionsSelected = false;
    private UgoListAdapter adapter;
    private List<GreenObject> searchResult = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionsUtil.setPermissions(this);
        setContentView(R.layout.activity_search_ugo);
        ButterKnife.bind(this);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("请稍候...");
        initToolbar();
        initSearchView();
        initSuggestionList();
        initSnackbar();
//        searchView.setSuggestions(sugIDs);
    }

    private void initSnackbar() {
        Snackbar.make(findViewById(R.id.toolbar),"请在右上角菜单栏选择搜索选项",Snackbar.LENGTH_INDEFINITE)
                .setAction("关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                }).show();
    }

    private void initToolbar() {
        toolbar.setTitle("搜索相关对象");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initSearchView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String errorMsg[] = new String[1];
                        try {
                            searchResult = WebServiceUtils.searchUGOInfo_1(errorMsg, query);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismiss();
                                Toast.makeText(SearchUgoActivity.this, searchResult.get(0).UGO_Address, Toast.LENGTH_SHORT).show();
                                recyclerUgoSearchResult.setVisibility(View.VISIBLE);
                                initRecyclerView();
                            }
                        });
                    }

                }).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
//                searchView.setSuggestions(sugIDs);
                if (!isSearchOptionsSelected) {
                    searchView.closeSearch();
                    Toast.makeText(SearchUgoActivity.this, "请选择搜索条件", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSearchViewClosed() {
//                recyclerUgoSearchResult.setVisibility(View.INVISIBLE);
//                searchHintTextview.setVisibility(View.VISIBLE);

            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void initSuggestionList() {
        if (CacheUtil.hasUGOSug()) {
            sugIDs = CacheUtil.getUGOSug("UGO_ID");
            Log.d("tag", "size=" + sugIDs.length);
            sugAddresses = CacheUtil.getUGOSug("UGO_Address");
        } else {
            loadingDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String errorMsg[] = new String[1];
                    List<GreenObjectSug> res = WebServiceUtils.getUGOSug(errorMsg);
                    loadingDialog.dismiss();
                    if (res != null) {
                        CacheUtil.putUGOSug(res);
                        sugIDs = CacheUtil.getUGOSug("UGO_ID");
                        sugAddresses = CacheUtil.getUGOSug("UGO_Address");
                    } else if (errorMsg[0] != null && !errorMsg[0].equals("")) {
                        Looper.prepare();
                        Toast.makeText(SearchUgoActivity.this, errorMsg[0], Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(SearchUgoActivity.this, "网络连接断开，请稍后再试", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }).start();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerUgoSearchResult.setLayoutManager(linearLayoutManager);
        adapter = new UgoListAdapter(searchResult);
        recyclerUgoSearchResult.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_ugo_search, menu);
        MenuItem item = menu.findItem(R.id.menu_toolbar_item_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        isSearchOptionsSelected = true;
        if (item.getItemId() == R.id.menu_toolbar_item_uid) {
            searchView.setSuggestions(sugIDs);
        } else {
            searchView.setSuggestions(sugAddresses);
        }
        return super.onOptionsItemSelected(item);
    }
}
