package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lxs on 2016/11/20.
 */
public class InspectListActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private RecyclerView recyclerInspectList;
    private SearchView searchView;
    private FloatingActionButton floatingbtnAddInspect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_inspect_list);
        super.onCreate(savedInstanceState);
        recyclerInspectList=(RecyclerView)findViewById(R.id.recyclerView_inspect_list);
        mToolbar=(Toolbar)findViewById(R.id.Toolbar);
        floatingbtnAddInspect=(FloatingActionButton)findViewById(R.id.floatingbtn_addInspect);
        floatingbtnAddInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InspectListActivity.this,InspectInfoActivity.class);
                startActivity(intent);
            }
        });
        initToolbar();
        initInspectList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_search,menu);
        MenuItem searchMenuItem=menu.findItem(R.id.menu_toolbar_item_search);
        searchView=(SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.onActionViewCollapsed();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ((InsepectListAdapter)recyclerInspectList.getAdapter()).getFilter().filter("");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((InsepectListAdapter)recyclerInspectList.getAdapter()).getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                    ((InsepectListAdapter)recyclerInspectList.getAdapter()).getFilter().filter("");
                return true;
            }
        });
        searchView.setIconified(true);
        return true;
    }

    private void initInspectList()
    {
        List<InspectObject> inspectList=new ArrayList<>();
        inspectList.add(new InspectObject("00001","00000003","日常巡查",new Date(116,10,7),"镇江养护公司（ID）",
                "张三","90","维护","好"));
        inspectList.add(new InspectObject("00002","00000013","管养考核",new Date(116,11,17),"镇江养护公司（ID）",
                "张三","60","维护树木","不好"));
        inspectList.add(new InspectObject("00003","00000023","保洁考核",new Date(116,10,29),"镇江养护公司（ID）",
                "张三","75","打农药","不好"));
        recyclerInspectList.setLayoutManager(new LinearLayoutManager(this));
        recyclerInspectList.setAdapter(new InsepectListAdapter(inspectList));
    }

    private void initToolbar()
    {
        mToolbar.setTitle("巡查记录");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorBackground));
        setSupportActionBar(mToolbar);
        mToolbar.setMinimumHeight(50);
        mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectListActivity.this.finish();
            }
        });
    }

}
