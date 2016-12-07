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

//        btnBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((Activity)getContext()).finish();
//            }
//        });
    }

    /**
     * 以下为标题栏布局暴露的接口，提供给使用者
     */

    //设置左侧返回按钮的点击事件,由于返回时可能除了销毁当前活动外，还要进行一些其他操作，因此这里改动一下

    /**
     * 如果只是想要实现返回的功能，则只需要finish()掉当前活动即可
     * 如果要保存一些数据，可以根据需要进行修改
     */
    public void setBtnBackClickListener(OnClickListener listener){
        btnBack.setOnClickListener(listener);
    }

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
