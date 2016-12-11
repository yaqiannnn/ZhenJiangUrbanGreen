package com.nju.urbangreen.zhenjiangurbangreen.events;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.widget.PagerSlidingTabStrip;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleSearchView;

public class EventsActivity extends FragmentActivity {

    private TitleBarLayout titleBarLayout;//标题栏
    private PagerSlidingTabStrip tabs;//顶部选项卡
    private ViewPager pager;
    public EventPagerAdapter adapter;
    private FloatingActionButton fbtnAddEvent;//悬浮按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        initViews();

    }

    //初始化控件
    public void initViews(){

        titleBarLayout = (TitleBarLayout) findViewById(R.id.ly_events_title_bar);
        titleBarLayout.setTitleText("事件记录");
        titleBarLayout.setBtnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleBarLayout.setBtnSelfDefBkg(R.drawable.ic_btn_self_def_search);
        titleBarLayout.setBtnSelfDefClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleBarLayout.setTsvSearchAvailable();
            }
        });
        TitleSearchView searchView = titleBarLayout.getSearchView();

        tabs = (PagerSlidingTabStrip) findViewById(R.id.psts_tabs);

        pager = (ViewPager) findViewById(R.id.vp_content);
        fbtnAddEvent = (FloatingActionButton) findViewById(R.id.fbtn_add_event);
        fbtnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this,EventRegisterActivity.class);
                startActivity(intent);
            }
        });
        adapter = new EventPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabs.setViewPager(pager);

        searchView.setOnCloseListener(new TitleSearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                EventListFragment fragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_content + ":" + pager.getCurrentItem());
                //EventListFragment fragment = ((EventPagerAdapter)(pager.getAdapter())).getCurrFragment();
                //if(fragment.getLvEventList() != null)
                ((EventAdapter)(fragment.getLvEventList().getAdapter())).getFilter().filter("");
                return false;
            }
        });

        searchView.setOnQueryTextListener(new TitleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EventListFragment fragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_content + ":" + pager.getCurrentItem());
                //EventListFragment fragment = ((EventPagerAdapter)(pager.getAdapter())).getCurrFragment();
                //if(fragment.getLvEventList() != null)
                if(query.equals(""))
                    ((EventAdapter)(fragment.getLvEventList().getAdapter())).getFilter().filter("");
                else
                    ((EventAdapter)(fragment.getLvEventList().getAdapter())).getFilter().filter(query);
                    //fragment.getLvEventList().setFilterText(query);
                Log.i("Nomad", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                EventListFragment fragment = (EventListFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_content + ":" + pager.getCurrentItem());
                //EventListFragment fragment = ((EventPagerAdapter)(pager.getAdapter())).getCurrFragment();
                if(newText.equals(""))
                    ((EventAdapter)(fragment.getLvEventList().getAdapter())).getFilter().filter("");
                    //fragment.getLvEventList().clearTextFilter();
                Log.i("Nomad", "onQueryTextChange");
                return false;
            }
        });
    }

    //pager的适配器
    public class EventPagerAdapter extends FragmentPagerAdapter{

        //private final String[] TITLES = {"自然灾害","病虫灾害","交通事故","人为事故","其它"};
        private final String[] TITLES = {"待上传","已上传"};
        private EventListFragment currFragment;
        public EventPagerAdapter(FragmentManager fm){
            super(fm);
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
        public void setPrimaryItem(View container, int position, Object object) {
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
        Log.i("事件活动","onDestroy");
        if(titleBarLayout.recoverReceiver != null){
            unregisterReceiver(titleBarLayout.recoverReceiver);
        }
    }
}
