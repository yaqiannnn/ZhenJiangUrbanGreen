package com.nju.urbangreen.zhenjiangurbangreen.widget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by Liwei on 2016/11/4.
 */
public class TitleBarLayout extends LinearLayout {
    public static final String ACTION_SHOW_TITLE_LAYOUT = "com.nju.urbangreen.zhenjiangurbangreen." +
            "widget.TitleBarLayout.TitleRecoverReceiver";

    @BindView(R.id.btn_title_back)
    public Button btnBack;
    @BindView(R.id.tv_title_text)
    public TextView tvTitle;
    @BindView(R.id.btn_title_self_def)
    public Button btnSelfDef;//自定义功能按钮，可以根据需求设置功能
    @BindView(R.id.tsv_title_search)
    public TitleSearchView tsvSearch;

    private Context mContext;
    public TitleRecoverReceiver recoverReceiver;

    public TitleBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.title_bar,this);
//        btnBack = (Button) findViewById(R.id.btn_title_back);
//        tvTitle = (TextView) findViewById(R.id.tv_title_text);
//        btnSelfDef = (Button) findViewById(R.id.btn_title_self_def);
//        tsvSearch = (TitleSearchView) findViewById(R.id.tsv_title_search);

        TitleRecoverReceiver recoverReceiver = new TitleRecoverReceiver();
        context.registerReceiver(recoverReceiver,new IntentFilter(ACTION_SHOW_TITLE_LAYOUT));
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(visibility == VISIBLE){

        }
    }
    /*****************************以下为标题栏布局暴露的接口，提供给使用者********************/

    /**
     * 设置左侧返回按钮的点击事件,由于返回时可能除了销毁当前活动外，还要进行一些其他操作，因此这里改动一下
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

    public void setTsvSearchAvailable(){
        btnBack.setVisibility(View.GONE);
        tvTitle.setVisibility(GONE);
        btnSelfDef.setVisibility(GONE);
        tsvSearch.setVisibility(VISIBLE);
        tsvSearch.setEtSearchOnFocus();
        recoverReceiver = new TitleRecoverReceiver();
        mContext.registerReceiver(recoverReceiver,new IntentFilter(ACTION_SHOW_TITLE_LAYOUT));
    }

    public TitleSearchView getSearchView(){
        return tsvSearch;
    }

    public class TitleRecoverReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction() == ACTION_SHOW_TITLE_LAYOUT){
                btnBack.setVisibility(VISIBLE);
                tvTitle.setVisibility(VISIBLE);
                btnSelfDef.setVisibility(VISIBLE);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()){
                    imm.hideSoftInputFromWindow(getApplicationWindowToken(),0);
                }
                tsvSearch.setVisibility(GONE);
                //mContext.unregisterReceiver(recoverReceiver);
            }
        }
    }
}
