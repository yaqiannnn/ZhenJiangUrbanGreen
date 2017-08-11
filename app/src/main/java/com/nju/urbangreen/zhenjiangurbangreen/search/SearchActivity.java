package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObjectSug;
import com.nju.urbangreen.zhenjiangurbangreen.map.ILayerSwitchListener;
import com.nju.urbangreen.zhenjiangurbangreen.map.LayerSwitchPopupWindow;
import com.nju.urbangreen.zhenjiangurbangreen.util.CacheUtil;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;

import java.util.List;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.Toolbar)
    public Toolbar toolbar;

    @BindView(R.id.material_search_view)
    public MaterialSearchView searchView;

    @BindView(R.id.rcv_search_UGO)
    public RecyclerView rcvSearchUGO;
    private SearchResultAdapter m_adapter;

    private ProgressDialog loadingDialog;

    private LayerSwitchPopupWindow popupWindow;
    private boolean searchType[]; // 0: GreenLand, 1: AncientTree, 2: StreetTree

    private String sugIDs[] = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        loadingDialog = new ProgressDialog(SearchActivity.this);
        loadingDialog.setMessage("请稍候...");

        rcvSearchUGO.setLayoutManager(new LinearLayoutManager(this));
        m_adapter = new SearchResultAdapter();
        rcvSearchUGO.setAdapter(m_adapter);
        rcvSearchUGO.getAdapter().notifyDataSetChanged();

        initSuggestionList();
        initPopupWindow();
        initToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_search, menu);
        MenuItem item = menu.findItem(R.id.menu_toolbar_item_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    private void initPopupWindow() {
        searchType = new boolean[] {true, true, true};
        popupWindow = new LayerSwitchPopupWindow(this, new ILayerSwitchListener() {
            @Override
            public boolean[] getLayerState() {
                return searchType;
            }

            @Override
            public void changeLayerState(boolean[] layerState) {
                searchType[0] = layerState[0];
                searchType[1] = layerState[1];
                searchType[2] = layerState[2];
            }
        });
    }

    private void initToolbar()
    {
        toolbar.setTitle("搜索绿化对象");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                loadingDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String errorMsg[] = new String[1];
                        List<GreenObject> res = WebServiceUtils.searchUGOByID(query, searchType, errorMsg);
                        loadingDialog.dismiss();
                        if(res != null) {
                            m_adapter.addUGOs(res);
                            SearchActivity.this.runOnUiThread(updateUI);
                        } else if(errorMsg[0] != null && !errorMsg[0].equals("")) {
                            Looper.prepare();
                            Toast.makeText(SearchActivity.this, errorMsg[0], Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(SearchActivity.this, "没有匹配的搜索结果", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                }).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("bar", "change");
                return false;
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_toolbar_item_filter) {
                    popupWindow.showAsDropDown(toolbar, 0, 12, Gravity.RIGHT | Gravity.BOTTOM);
                }
                return false;
            }
        });
        searchView.setSuggestions(sugIDs);
    }
    private void initSuggestionList()
    {
        loadingDialog.show();
        if(CacheUtil.hasUGOSug()) {
            sugIDs = CacheUtil.getUGOSug("UGO_ID");
            loadingDialog.dismiss();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String errorMsg[] = new String[1];
                    List<GreenObjectSug> res = WebServiceUtils.getUGOSug(errorMsg);
                    if(res != null) {
                        CacheUtil.putUGOSug(res);
                        sugIDs = CacheUtil.getUGOSug("UGO_ID");
                    } else if(errorMsg[0] != null && !errorMsg[0].equals("")) {
                        Looper.prepare();
                        Toast.makeText(SearchActivity.this, errorMsg[0], Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(SearchActivity.this, "网络连接断开，请稍后再试", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    loadingDialog.dismiss();
                }
            }).start();
        }
    }

    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            rcvSearchUGO.getAdapter().notifyDataSetChanged();
        }
    };

}
