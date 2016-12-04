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
 * Created by Liwei on 2016/11/4.
 */
public class TitleBarLayout extends LinearLayout {
    private Button btnBack;
    private TextView tvTitle;
    private Button btnSelfDef;//自定义功能按钮，可以根据需求设置功能

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_bar,this);
        btnBack = (Button) findViewById(R.id.btn_title_back);
        tvTitle = (TextView) findViewById(R.id.tv_title_text);
        btnSelfDef = (Button) findViewById(R.id.btn_title_self_def);

        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });
    }

    /**
     * 以下为标题栏布局暴露的接口，提供给使用者
     */

    //设置标题名称
    public void setTitleText(String title){
        tvTitle.setText(title);
    }

    //设置右侧按钮背景
    public void setBtnSelfDefBkg(int resId){
        btnSelfDef.setBackgroundResource(resId);
    }

    //设置右侧按钮点击事件
    public void setBtnSelfDefClickListener(OnClickListener listener){
        btnSelfDef.setOnClickListener(listener);
    }
}
