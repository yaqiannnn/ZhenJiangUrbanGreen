package com.nju.urbangreen.zhenjiangurbangreen.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.nju.urbangreen.zhenjiangurbangreen.R;
import com.nju.urbangreen.zhenjiangurbangreen.navigation.NaviActivity;
import com.nju.urbangreen.zhenjiangurbangreen.search.SearchActivity;

/**
 * Created by HCQIN on 2016/9/29.
 */
public class MapTopBarLayout extends LinearLayout {
    private ImageButton imgBtnMenu;
    private EditText etSearch;
    final Context mContext = null;

    public MapTopBarLayout(Context context, AttributeSet attrs) {

        ///((Activity)getContext()).getIntent()
        super(context, attrs);
        final Context mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.map_top_bar,this);
        imgBtnMenu = (ImageButton) findViewById(R.id.btn_map_menu);
        etSearch = (EditText) findViewById(R.id.et_search);
        imgBtnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NaviActivity.class);
                mContext.startActivity(intent);
            }
        });
        etSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,SearchActivity.class);
                mContext.startActivity(intent);
            }
        });


    }
}
