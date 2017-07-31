package com.nju.urbangreen.zhenjiangurbangreen.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liwei on 2016/11/30.
 */
public class DropdownEditText extends LinearLayout {

    //DropdownEditText这个控件是由EditText，CheckBox共同构成的
    @BindView(R.id.et_dropdown_content)
    public EditText etContent;

    @BindView(R.id.cb_dropdown_btn)
    public CheckBox ivShowDropdown;//控制PopupWindow的弹出


    private PopupWindow popupWindow;

    private DropListView lvDropDownView = null;//PopupWindow的布局，是一个继承自ListView的列表
    private Context mContext;
    private ArrayList<String> dropdownList = null;

    public DropdownEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.dropdown_edittext,this);

        //初始化布局和控件
        lvDropDownView = (DropListView) LayoutInflater.from(context).inflate(R.layout.dropdown_popupwindow_list,null);
        ButterKnife.bind(this);

        ivShowDropdown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupWindow(etContent);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed && lvDropDownView != null){
            //在布局改变时，设置PopupWindow的宽度
            lvDropDownView.setListWidth(getMeasuredWidth());
        }
    }

    //初始化PopupWindow
    public void initPopupWindow(View parent){

        //如果在使用DropdownEditText的时候，没有对其添加列表项，就要首先给下拉列表new一个对象
        if(dropdownList == null)
            dropdownList = new ArrayList<String>();

        //为下拉列表设置适配器
        DropDownListAdapter mAdapter = new DropDownListAdapter(mContext,R.layout.dropdown_popupwindow_list_item,dropdownList);
        lvDropDownView.setAdapter(mAdapter);

        //设置下拉列表项的点击事件，将选中的项显示到etContent中
        lvDropDownView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etContent.setText(lvDropDownView.getAdapter().getItem(i).toString());
                setCommonDrawable();
                popupWindow.dismiss();
                ivShowDropdown.setChecked(false);
            }
        });

        //初始化PopupWindow
        popupWindow = new PopupWindow(lvDropDownView, getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ivShowDropdown.setChecked(false);
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),(Bitmap)null));

        //设置PopupWindow的显示位置
        popupWindow.getContentView().measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
        popupWindow.showAsDropDown(parent,0,5);
    }

    //DropdownEditText暴露的接口，用以设置下拉列表中的列表项
    public void setDropdownList(ArrayList<String> dropdownListToShow){
        dropdownList = dropdownListToShow;
    }

    public String getText(){
        return etContent.getText().toString();
    }

    public void setText(String string){
        etContent.setText(string);
    }

    public void setEmptyWarning()
    {
        etContent.setBackground(getResources().getDrawable(R.drawable.bkg_edittext_empty));
    }

    public void setCommonDrawable()
    {
        etContent.setBackground(getResources().getDrawable(R.drawable.bkg_edittext));
    }

    //下拉列表的适配器
    class DropDownListAdapter extends ArrayAdapter<String>{

        private int resourceId;

        public DropDownListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            String item = getItem(position);
            View view;
            ViewHolder viewHolder;
            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId,null);
                viewHolder = new ViewHolder();
                viewHolder.itemName = (TextView) view.findViewById(R.id.tv_dropdown_list_item_name);
                view.setTag(viewHolder);
            }else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.itemName.setText(item);
            return view;
        }

        class ViewHolder{
            TextView itemName;
        }
    }

}
