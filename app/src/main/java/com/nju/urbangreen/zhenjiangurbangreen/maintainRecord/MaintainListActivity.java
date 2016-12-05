package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MaintainListActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView recyclerMaintainList;
    private SearchView searchView;
    private FloatingActionButton floatingbtnAddMaintain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_maintain_list);
        super.onCreate(savedInstanceState);
        recyclerMaintainList=(RecyclerView)findViewById(R.id.recyclerView_maintain_list);
        mToolbar=(Toolbar)findViewById(R.id.Toolbar);
        floatingbtnAddMaintain=(FloatingActionButton)findViewById(R.id.floatingbtn_addMaintain);
        floatingbtnAddMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MaintainListActivity.this,MaintainInfoActivity.class);
                startActivity(intent);
            }
        });
        initToolbar();
        initMaintainList();
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
                ((MaintainListAdapter)recyclerMaintainList.getAdapter()).getFilter().filter("");
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ((MaintainListAdapter)recyclerMaintainList.getAdapter()).getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                    ((MaintainListAdapter)recyclerMaintainList.getAdapter()).getFilter().filter("");
                return true;
            }
        });
        searchView.setIconified(true);
        return true;
    }

    private void initMaintainList()
    {
        List<MaintainObject> maintainList=new ArrayList<>();
        maintainList.add(new MaintainObject("82301","00000003","镇江养护公司（ID）","浇水排水","张三",
                new Date(116,10,7), "维护"));
        maintainList.add(new MaintainObject("07702","00000013","镇江养护公司（ID）","安全施工","张三",
                new Date(116,9,3), "维护"));
        maintainList.add(new MaintainObject("82453","00000023","镇江养护公司（ID）","松土除草","张三",
                new Date(116,11,15), "维护"));
        maintainList.add(new MaintainObject("82705","00000083","镇江养护公司（ID）","松土除草","张三",
                new Date(115,10,25), "维护"));
        recyclerMaintainList.setLayoutManager(new LinearLayoutManager(this));
        recyclerMaintainList.setAdapter(new MaintainListAdapter(maintainList));
    }

    private void initToolbar()
    {
        mToolbar.setTitle("养护记录");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorBackground));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaintainListActivity.this.finish();
            }
        });
    }
}
