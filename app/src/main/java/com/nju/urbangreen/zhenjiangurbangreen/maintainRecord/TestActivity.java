package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.util.WebServiceUtils;
import com.nju.urbangreen.zhenjiangurbangreen.widget.LoadMoreFooterView;
import com.nju.urbangreen.zhenjiangurbangreen.widget.RefreshHeaderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tommy on 2017/8/31.
 */

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.swipe_target)
    RecyclerView recyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    MaintainListAdapter2 adapter;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView swipeRefreshHeader;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView swipeLoadMoreFooter;
    private List<Maintain> maintainList = new ArrayList<>();
    private int page = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_loadmore_view);
        ButterKnife.bind(this);
        getMaintainList();
        initViews();

    }

    private void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MaintainListAdapter2(maintainList);
        recyclerView.setAdapter(adapter);

        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();
                //设置下拉刷新结束
                swipeToLoadLayout.setRefreshing(false);
            }
        });

        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getMaintainList(page, 8);
                        //page++;
                    }
                }).start();
            }
        });
    }
    private void getMaintainList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> query = new HashMap<>();
                String[] errMsg = new String[1];
                query.put("page", 1);
                query.put("limit", 8);
                maintainList = WebServiceUtils.getMaintainRecord(query, errMsg);
//                Log.d("tag",maintainList.size()+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initViews();
                    }
                });
            }
        }).start();
    }

    private void getMaintainList(final int page, final int limit) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> query = new HashMap<>();
                String[] errMsg = new String[1];
                query.put("page", page);
                query.put("limit", limit);
                List newMaintainList = WebServiceUtils.getMaintainRecord(query, errMsg);
                maintainList.addAll(newMaintainList);
                Log.d("tag",maintainList.size()+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TestActivity.this.page++;
                        adapter.notifyDataSetChanged();
                        //设置加载更多结束
                        swipeToLoadLayout.setLoadingMore(false);
                    }
                });
            }
        });
    }
}
