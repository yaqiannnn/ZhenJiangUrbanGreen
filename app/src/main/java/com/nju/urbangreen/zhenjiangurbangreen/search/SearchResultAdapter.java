package com.nju.urbangreen.zhenjiangurbangreen.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.basisClass.GreenObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lxs on 2016/9/30.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultHolder>{
    private List<GreenObject> UGOList;

    public SearchResultAdapter()
    {
        UGOList = new ArrayList<>();
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(parent.getContext(), R.layout.recycleritem_search_result,null);
        SearchResultHolder holder=new SearchResultHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchResultHolder holder, int position) {
        holder.bindObj(UGOList.get(position));
    }

    @Override
    public int getItemCount() {
        return UGOList.size();
    }

    public void addUGOs(List<GreenObject> data) {
        UGOList.clear();
        UGOList.addAll(data);
    }

}
