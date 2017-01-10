package com.nju.urbangreen.zhenjiangurbangreen.maintainRecord;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by lxs on 2017/1/10.
 */
public class myTextWatcher implements TextWatcher {
    EditText textView;
    public myTextWatcher(EditText view)
    {
       this.textView=view;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!textView.getText().toString().equals(""))
            this.textView.setBackground(textView.getResources().getDrawable(R.drawable.bkg_edittext));
    }
}
