package com.nju.urbangreen.zhenjiangurbangreen.basisClass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
