package com.nju.urbangreen.zhenjiangurbangreen.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Liwei on 2016/12/1.
 */

/**
 * 可编辑下拉框的布局，继承ListView,重写onMeasure可以改变宽度
 */

public class DropListView extends ListView {
    private int width = 0;

    public DropListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();

        for(int i = 0;i < getChildCount();i++){
            int childWidth = getChildAt(i).getMeasuredWidth();
            width = Math.max(width,childWidth);
        }

        setMeasuredDimension(width,height);
    }

    protected void setListWidth(int mWidth){
        width = mWidth;
    }
}
