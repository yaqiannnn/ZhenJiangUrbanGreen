package com.nju.urbangreen.zhenjiangurbangreen.inspectRecord;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.BaseListAdapter;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventListFragment;
import com.nju.urbangreen.zhenjiangurbangreen.events.EventRegisterActivity;
import com.nju.urbangreen.zhenjiangurbangreen.util.ActivityCollector;
import com.nju.urbangreen.zhenjiangurbangreen.widget.PagerSlidingTabStrip;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleBarLayout;
import com.nju.urbangreen.zhenjiangurbangreen.widget.TitleSearchView;

public class InspectListActivity extends AppCompatActivity {

    @BindView(R.id.ly_inspect_list_title_bar)
    public TitleBarLayout titleBarLayout;//标题栏

    @BindView(R.id.psts_inspect)
    public PagerSlidingTabStrip tabs;//顶部选项卡

    @BindView(R.id.vp_inspect_content)
    public ViewPager pager;

    @BindView(R.id.floatingbtn_add_inspect)
    public FloatingActionButton fbtnAddInspect;//悬浮按钮

    public InspectPagerAdapter adapter;
    private String[] tabTitles={"待上传","已上传"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_inspect_list);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        Log.i("巡查活动","onDestroy");
        if(titleBarLayout.recoverReceiver != null){
            unregisterReceiver(titleBarLayout.recoverReceiver);
        }
    }

    //pager的适配器
    public class InspectPagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES;
        private InspectListFragment currFragment;
        public InspectPagerAdapter(FragmentManager fm, String[] tabTitles){
            super(fm);
            this.TITLES=tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            return InspectListFragment.newInstance(position);
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
            currFragment = (InspectListFragment) object;
        }


        public InspectListFragment getCurrFragment(){
            return currFragment;
        }

    }

    //初始化控件
    public void initViews(){
        ButterKnife.bind(this);
        setTitleBarLayout();
        fbtnAddInspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InspectListActivity.this,EventRegisterActivity.class);
                startActivity(intent);
            }
        });
        adapter = new InspectPagerAdapter(getSupportFragmentManager(),tabTitles);
        pager.setAdapter(adapter);

        tabs.setViewPager(pager);
        tabs.setTextSize(40);

    }
    public void setTitleBarLayout(){
        //初始化TitleBarLayout
        titleBarLayout.setTitleText("巡查记录");
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
                InspectListFragment fragment = (InspectListFragment) getSupportFragmentManager().
                        findFragmentByTag("android:switcher:" + R.id.vp_inspect_content + ":" + pager.getCurrentItem());

                ((BaseListAdapter)(fragment.getRcyvInspectList().getAdapter())).getFilter().filter("");
                return false;
            }
        });

        searchView.setOnQueryTextListener(new TitleSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //获取当前的fragment
                InspectListFragment fragment = (InspectListFragment) getSupportFragmentManager().
                        findFragmentByTag("android:switcher:" + R.id.vp_inspect_content + ":" + pager.getCurrentItem());

                if(query.equals(""))
                    ((BaseListAdapter)(fragment.getRcyvInspectList().getAdapter())).getFilter().filter("");
                else
                    ((BaseListAdapter)(fragment.getRcyvInspectList().getAdapter())).getFilter().filter(query);

                Log.i("Nomad", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //获取当前的fragment
                InspectListFragment fragment = (InspectListFragment) getSupportFragmentManager().
                        findFragmentByTag("android:switcher:" + R.id.vp_inspect_content + ":" + pager.getCurrentItem());

                if(newText.equals(""))
                    ((BaseListAdapter)(fragment.getRcyvInspectList().getAdapter())).getFilter().filter("");

                Log.i("Nomad", "onQueryTextChange");
                return false;
            }
        });
    }
    public TitleSearchView getSearchView(){
        return titleBarLayout.getSearchView();
    }
}
