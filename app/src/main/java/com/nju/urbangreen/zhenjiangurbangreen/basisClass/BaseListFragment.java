package com.nju.urbangreen.zhenjiangurbangreen.basisClass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.nju.urbangreen.zhenjiangurbangreen.events.OneEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxs on 2017/1/11./////////////Fragment这一个层次不抽象出来
 */
public class BaseListFragment extends Fragment {
    protected SwipeRefreshLayout refreshLayout;
    protected RecyclerView recyclerList;
    protected RecyclerView.Adapter<RecyclerView.ViewHolder> listAdapter;
    private int position;//当前页面位置
    private List<OneEvent> baseRecordList = new ArrayList<>();
    private BaseListAdapter baseListAdapter;

    public static BaseListFragment newInstance(int position)
    {
        BaseListFragment fragment=new BaseListFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("PAGE_POSITION",position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position=getArguments().getInt("PAGE_POSITION");
    }


}
