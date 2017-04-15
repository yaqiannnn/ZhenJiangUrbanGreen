package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseListAdapter;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.widget.PagerSlidingTabStrip;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleSearchView;

public class EventListActivity extends FragmentActivity {

    private TitleBarLayout titleBarLayout;//标题栏
    private PagerSlidingTabStrip tabs;//顶部选项卡
    private ViewPager pager;
    public EventPagerAdapter adapter;
    private FloatingActionButton fbtnAddEvent;//悬浮按钮

    private String[] tabTitles={"待上传","已上传"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_event_list);
        initViews();

    }

    //初始化控件
    public void initViews(){
        setTitleBarLayout();


        tabs = (PagerSlidingTabStrip) findViewById(R.id.pagetab_event);

        pager = (ViewPager) findViewById(R.id.pager_event_content);
        fbtnAddEvent = (FloatingActionButton) findViewById(R.id.floatingbtn_add_event);
        fbtnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventListActivity.this,EventRegisterActivity.class);
                startActivity(intent);
            }
        });
        adapter = new EventPagerAdapter(getSupportFragmentManager(),tabTitles);
        pager.setAdapter(adapter);

        tabs.setViewPager(pager);
        tabs.setTextSize(40);

    }

    //pager的适配器
    public class EventPagerAdapter extends FragmentPagerAdapter{

        private String[] TITLES;
        private EventListFragment currFragment;
        public EventPagerAdapter(FragmentManager fm, String[] tabTitles){
            super(fm);
            this.TITLES=tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return EventListFragment.newInstance(position);
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
            currFragment = (EventListFragment) object;
        }


        public EventListFragment getCurrFragment(){
            return currFragment;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        Log.i("事件活动","onDestroy");
        if(titleBarLayout.recoverReceiver != null){
            unregisterReceiver(titleBarLayout.recoverReceiver);
        }
    }

    public void setTitleBarLayout(){
        //初始化TitleBarLayout
        titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_events_list_title_bar);
        titleBarLayout.setTitleText("事件记录");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //右侧的自定义按钮此时为搜索按钮，点击是会显示出TitleSearchView
        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_search);
        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示出TitleSearchView
                titleBarLayout.setTsvSearchAvailable();
            }
        });
        //获取TitleSearchView
        TitleSearchView searchView = titleBarLayout.getSearchView();

        //设置TitleSearchView的监听事件
        searchView.setOnCloseListener(new TitleSearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //获取当前的fragment
                EventListFragment fragment = (EventListFragment) getSupportFragmentManager().
                        findFragmentByTag("android:switcher:" + R.id.pager_event_content + ":" + pager.getCurrentItem());

                ((BaseListAdapter)(fragment.getRcyvEventList().getAdapter())).getFilter().filter("");
                return false;
            }
        });

        searchView.setOnQueryTextListener(new TitleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //获取当前的fragment
                EventListFragment fragment = (EventListFragment) getSupportFragmentManager().
                        findFragmentByTag("android:switcher:" + R.id.pager_event_content + ":" + pager.getCurrentItem());

                if(query.equals(""))
                    ((BaseListAdapter)(fragment.getRcyvEventList().getAdapter())).getFilter().filter("");
                else
                    ((BaseListAdapter)(fragment.getRcyvEventList().getAdapter())).getFilter().filter(query);

                Log.i("Nomad", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //获取当前的fragment
                EventListFragment fragment = (EventListFragment) getSupportFragmentManager().
                        findFragmentByTag("android:switcher:" + R.id.pager_event_content + ":" + pager.getCurrentItem());

                if(newText.equals(""))
                    ((BaseListAdapter)(fragment.getRcyvEventList().getAdapter())).getFilter().filter("");

                Log.i("Nomad", "onQueryTextChange");
                return false;
            }
        });
    }

    public TitleSearchView getSearchView(){
        return titleBarLayout.getSearchView();
    }
}
