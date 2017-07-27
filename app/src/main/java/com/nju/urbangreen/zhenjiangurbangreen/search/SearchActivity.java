package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import butterknife.BindView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    
    @BindView(R.id.Toolbar)
    public Toolbar mToolbar;
    public SearchView searchView;

    @BindView(R.id.listView_suggestionList)
    public ListView suggestionList_listView;

    @BindView(R.id.recyclerView_searchResult)
    public RecyclerView searchResult_recyclerView;
    private List<String> suggestionList;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initViews();
        initSuggestionList();
        initSearchResultList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_search,menu);
        MenuItem search_menuItem=menu.findItem(R.id.menu_toolbar_item_search);
        searchView=(SearchView) MenuItemCompat.getActionView(search_menuItem);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("search","click");
                setSuggestionFilter(null);
                searchResult_recyclerView.setVisibility(View.INVISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d("search","close");
                searchResult_recyclerView.setVisibility(View.VISIBLE);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        Log.d("search",query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String nextText)
    {
        Log.d("search","change_"+nextText);
        setSuggestionFilter(nextText);
        return true;
    }

    private void initViews()
    {
        setContentView(R.layout.activity_search);
        // mToolbar=(Toolbar)findViewById(R.id.Toolbar);
        // suggestionList_listView=(ListView)findViewById(R.id.listView_suggestionList);
        // searchResult_recyclerView=(RecyclerView)findViewById(R.id.recyclerView_searchResult);
        initToolbar();
    }
    private void initToolbar()
    {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_toolbar_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
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
        suggestionList_listView.setAdapter(suggestionListAdapter);
        suggestionList_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("click","position"+position+"");
                Log.d("click","id:"+id+"");
                searchView.setQuery(suggestionListAdapter.getItem(position),true);
            }
        });
        setSuggestionFilter(null);
    }
    private void initSearchResultList()
    {
        List<UGObject> ugList=new ArrayList<>();
        ugList.add(new UGObject("00000001","00000003","古树名木","梧桐树","镇江市","镇江市",15,"null"));
        ugList.add(new UGObject("00001111","00000333","绿地","公园","镇江市","镇江市",100,"null"));
        SearchResultAdapter searchResultAdapter=new SearchResultAdapter(ugList);
        searchResult_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResult_recyclerView.setAdapter(searchResultAdapter);
    }
    private void setSuggestionFilter(String filterText)
    {
        if(suggestionList_listView.getAdapter() instanceof Filterable)
        {
            Filter filter=((Filterable)suggestionList_listView.getAdapter()).getFilter();
            if(filterText==null||filterText.length()==0)
                filter.filter("-1");
            else
                filter.filter(filterText);
        }
    }
}
