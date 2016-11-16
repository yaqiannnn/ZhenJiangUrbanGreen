package com.nju.urbangreen.zhenjiangurbangreen.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by HCQIN on 2016/11/4.
 */
public class TitleBarLayout extends LinearLayout {
    private Button btnBack;
    private TextView tvTitle;
    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_bar,this);
        btnBack = (Button) findViewById(R.id.btn_title_back);
        tvTitle = (TextView) findViewById(R.id.tv_title_text);

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });
    }

    public void setTitleText(String title){
        tvTitle.setText(title);
    }
}
