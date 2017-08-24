package com.nju.urbangreen.zhenjiangurbangreen.settings;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lxs on 17-8-23.
 */

public class SystemFileAdapter extends RecyclerView.Adapter<SystemFileAdapter.SystemFileHolder> {

    private FragmentActivity mContext;
    private List<SystemFileItem> files;

    private static final String systemFileNames[] = {};
    private static final String systemFilePaths[] = {};

    public SystemFileAdapter(FragmentActivity activity) {
        mContext = activity;
    }

    @Override
    public SystemFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SystemFileHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SystemFileHolder extends RecyclerView.ViewHolder {

        public SystemFileHolder(View itemView) {
            super(itemView);
        }
    }

    public class SystemFileItem {
        public String downloadID;
        public String name;
        public String remotePath;
        public String savePath;
    }
}
