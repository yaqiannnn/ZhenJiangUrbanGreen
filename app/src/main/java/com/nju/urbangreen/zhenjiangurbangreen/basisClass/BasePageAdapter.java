package com.nju.urbangreen.zhenjiangurbangreen.basisClass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by lxs on 2017/1/12.
 */
public class BasePageAdapter extends FragmentPagerAdapter {
    private String[] tabTitles;
    private BaseListFragment currFragment;
    public BasePageAdapter(FragmentManager fragmentManager, String[] tabString)
    {
        super(fragmentManager);
        this.tabTitles=tabString;
    }
    @Override
    public Fragment getItem(int position) {
        return BaseListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        currFragment = (BaseListFragment) object;
    }

    public BaseListFragment getCurrFragment(){
        return currFragment;
    }
}
