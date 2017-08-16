package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseActivity;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventRegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaintainListActivity extends BaseActivity {

//    @BindView(R.id.ly_maintain_list_title_bar)
//    public TitleBarLayout titleBarLayout;//标题栏

    // @BindView(R.id.psts_maintain)
    // public PagerSlidingTabStrip tabs;//顶部选项卡

    // @BindView(R.id.vp_maintain_content)
    // public ViewPager pager;


    @BindView(R.id.floatingbtn_add_maintain)
    public FloatingActionButton fbtnAddMaintain;//悬浮按钮
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
    @BindView(R.id.spinner4)
    Spinner spinner4;
    @BindView(R.id.recycler_maintain_list)
    RecyclerView recyclerMaintainList;

    // public MaintainPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_list);
        ButterKnife.bind(this);
        initViews();
        initRecyclerView();
    }

    private void initRecyclerView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (titleBarLayout.recoverReceiver != null) {
//            unregisterReceiver(titleBarLayout.recoverReceiver);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_search, menu);
        MenuItem item = menu.findItem(R.id.menu_toolbar_item_search);
        searchView.setMenuItem(item);
        return super.onCreateOptionsMenu(menu);
    }

    //pager的适配器
    public class MaintainPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES;
        private MaintainListFragment currFragment;

        public MaintainPagerAdapter(FragmentManager fm, String[] tabTitles) {
            super(fm);
            this.TITLES = tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return MaintainListFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            currFragment = (MaintainListFragment) object;
        }


        public MaintainListFragment getCurrFragment() {
            return currFragment;
        }

    }

    //初始化控件
    public void initViews() {
        ButterKnife.bind(this);
//        setTitleBarLayout()
        toolbar.setTitle("管养对象列表");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fbtnAddMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MaintainListActivity.this, EventRegisterActivity.class);
                startActivity(intent);
            }
        });

        //adapter = new MaintainPagerAdapter(getSupportFragmentManagber(), tabTitles);
        //pager.setAdapter(adapter);

        //tabs.setViewPager(pager);
        //tabs.setTextSize(40);

    }

//    public void setTitleBarLayout() {
//        //初始化TitleBarLayout
//        // titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_maintain_list_title_bar);
//        titleBarLayout.setTitleText("管养记录");
//        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        //右侧的自定义按钮此时为搜索按钮，点击是会显示出TitleSearchView
//        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_search);
//        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //显示出TitleSearchView
//                titleBarLayout.setTsvSearchAvailable();
//            }
//        });
//        //获取TitleSearchView
//        TitleSearchView searchView = titleBarLayout.getSearchView();
//
//        //设置TitleSearchView的监听事件
//        searchView.setOnCloseListener(new TitleSearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                //获取当前的fragment
//                MaintainListFragment fragment = (MaintainListFragment) getSupportFragmentManager().
//                        findFragmentByTag("android:switcher:" + R.id.vp_maintain_content + ":" + pager.getCurrentItem());
//
//                ((BaseListAdapter) (fragment.getRcyvMaintainList().getAdapter())).getFilter().filter("");
//                return false;
//            }
//        });
//
//        searchView.setOnQueryTextListener(new TitleSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //获取当前的fragment
//                MaintainListFragment fragment = (MaintainListFragment) getSupportFragmentManager().
//                        findFragmentByTag("android:switcher:" + R.id.vp_maintain_content + ":" + pager.getCurrentItem());
//
//                if (query.equals(""))
//                    ((BaseListAdapter) (fragment.getRcyvMaintainList().getAdapter())).getFilter().filter("");
//                else
//                    ((BaseListAdapter) (fragment.getRcyvMaintainList().getAdapter())).getFilter().filter(query);
//
//                Log.i("Nomad", "onQueryTextSubmit");
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //获取当前的fragment
//                MaintainListFragment fragment = (MaintainListFragment) getSupportFragmentManager().
//                        findFragmentByTag("android:switcher:" + R.id.vp_maintain_content + ":" + pager.getCurrentItem());
//
//                if (newText.equals(""))
//                    ((BaseListAdapter) (fragment.getRcyvMaintainList().getAdapter())).getFilter().filter("");
//
//                Log.i("Nomad", "onQueryTextChange");
//                return false;
//            }
//        });
//    }

//    public TitleSearchView getSearchView() {
//        return titleBarLayout.getSearchView();
//    }

}
