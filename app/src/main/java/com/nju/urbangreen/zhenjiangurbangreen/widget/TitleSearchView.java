package com.nju.urbangreen.zhenjiangurbangreen.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by Liwei on 2016/12/9.
 */
public class TitleSearchView extends LinearLayout{

    private EditText etSearch;
    private ImageButton imgbtnCancel;
    private Context mContext;
    public TitleSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_searchview,this);
        mContext = context;
        etSearch = (EditText) findViewById(R.id.et_title_searchview);
        imgbtnCancel = (ImageButton) findViewById(R.id.imgbtn_title_searchview_cancel);
    }

    public void setOnCloseListener(final OnCloseListener listener){
        imgbtnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClose();
                if(etSearch.getText().toString().equals("")){
                    Intent intent = new Intent(TitleBarLayout.ACTION_SHOW_TITLE_LAYOUT);
                    mContext.sendBroadcast(intent);
                    //TitleSearchView.this.setVisibility(GONE);
                }else {
                    etSearch.setText("");
                }
            }
        });
    }

    public void setOnQueryTextListener(final OnQueryTextListener listener){
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(),0);
                    }
                    listener.onQueryTextSubmit(etSearch.getText().toString());

                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                listener.onQueryTextChange(etSearch.getText().toString());
            }
        });
    }

    public void setEtSearchOnFocus(){

        etSearch.setFocusable(true);
        etSearch.setFocusableInTouchMode(true);
        etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public interface OnQueryTextListener{
        public boolean onQueryTextSubmit(String query);
        public boolean onQueryTextChange(String newText);
    }

    public interface OnCloseListener{
        public boolean onClose();
    }
}
