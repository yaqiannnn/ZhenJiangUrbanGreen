package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.map.ILayerSwitchListener;
import com.nju.urbangreen.zhenjiangurbangreen.map.LayerSwitchPopupWindow;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity{

    @BindView(R.id.Toolbar)
    public Toolbar toolbar;

    @BindView(R.id.material_search_view)
    public MaterialSearchView searchView;

    @BindView(R.id.recyclerView_searchResult)
    public RecyclerView searchResult_recyclerView;

    private Menu filterMenu;
    private LayerSwitchPopupWindow popupWindow;
    private boolean searchType[]; // 0: GreenLand, 1: AncientTree, 2: StreetTree

    private List<String> suggestionList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initPopupWindow();
        initToolbar();
        initSuggestionList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        filterMenu = menu;
        getMenuInflater().inflate(R.menu.menu_toolbar_search, menu);
        MenuItem item = menu.findItem(R.id.menu_toolbar_item_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void initPopupWindow() {
        searchType = new boolean[] {true, true, false};
        popupWindow = new LayerSwitchPopupWindow(this, new ILayerSwitchListener() {
            @Override
            public boolean[] getLayerState() {
                return searchType;
            }

            @Override
            public void changeLayerState(boolean[] layerState) {
                searchType = layerState;
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
            public boolean onQueryTextSubmit(String query) {
                Log.i("bar", "submit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("bar", "change");
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                Log.i("bar", "show");
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                Log.i("bar", "close");
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
        String list[] = new String[4];
        list[0] = "qwert";
        list[1] = "qweas";
        list[2] = "qwezxc";
        list[3] = "zxcasd";
        searchView.setSuggestions(list);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.menu_toolbar_item_search:
//                }
//            }
//        });
//        titleBarLayout.setTitleText("搜索绿化对象");
//        titleBarLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_search);
//        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //显示出TitleSearchView
//                titleBarLayout.setTsvSearchAvailable();
//            }
//        });
//        TitleSearchView searchView = titleBarLayout.getSearchView();
    }
    private void initSuggestionList()
    {
        suggestionList=new ArrayList<>();
        for(int i=0;i<5000;i++)
        {
            suggestionList.add(Math.random()*10000+"tree");
        }
        final ArrayAdapter<String> suggestionListAdapter=new ArrayAdapter<>(SearchActivity.this,
                android.R.layout.simple_list_item_1,suggestionList);
//        suggestionList_listView.setAdapter(suggestionListAdapter);
//        suggestionList_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("click","position"+position+"");
//                Log.d("click","id:"+id+"");
//                searchView.setQuery(suggestionListAdapter.getItem(position),true);
//            }
//        });
//        setSuggestionFilter(null);
    }

//    private void setSuggestionFilter(String filterText)
//    {
//        if(suggestionList_listView.getAdapter() instanceof Filterable)
//        {
//            Filter filter=((Filterable)suggestionList_listView.getAdapter()).getFilter();
//            if(filterText==null||filterText.length()==0)
//                filter.filter("-1");
//            else
//                filter.filter(filterText);
//        }
//    }
}
