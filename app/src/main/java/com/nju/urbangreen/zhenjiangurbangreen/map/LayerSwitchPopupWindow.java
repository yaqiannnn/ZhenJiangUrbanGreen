package com.nju.urbangreen.zhenjiangurbangreen.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PictureDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

/**
 * Created by HCQIN on 2016/9/26.
 */
public class LayerSwitchPopupWindow extends PopupWindow {

    private ListView lvLayerList;
    private SwitchListAdapter switchListAdapter;
    private ILayerSwitchListener layerSwitchListener = null;
    public LayerSwitchPopupWindow(Context context,ILayerSwitchListener layerSwitchListener){

        setContentView(LayoutInflater.from(context).inflate(R.layout.map_layer_switch_popup,null));

        this.layerSwitchListener = layerSwitchListener;

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(new BitmapDrawable(context.getResources(),(Bitmap)null));

        lvLayerList = (ListView) getContentView().findViewById(R.id.lv_layer_list);
        switchListAdapter = new SwitchListAdapter(context);
        lvLayerList.setAdapter(switchListAdapter);


    }

    private class SwitchListAdapter extends BaseAdapter{
        private String[] layerName = new String[]{"绿地","古树名木","行道树"};
        private int[] iconId = new int[]{
                R.drawable.green_land, R.drawable.ancient_tree, R.drawable.street_tree};
        private boolean[] layerState;
        private Context mContext;
        public SwitchListAdapter(Context context){
            mContext = context;
            layerState = layerSwitchListener.getLayerState();
        }

        @Override
        public int getCount() {
            return layerName.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            final int position = i;
            if(view == null){
                viewHolder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.map_layer_switch_popup_item,viewGroup,false);
                viewHolder.layerIcon = (ImageView) view.findViewById(R.id.iv_layer_icon);
                viewHolder.layerName = (TextView) view.findViewById(R.id.tv_layer_name);
                viewHolder.layerSwitch = (CheckBox) view.findViewById(R.id.cb_layer_switch);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.layerIcon.setImageResource(iconId[i]);
            viewHolder.layerName.setText(layerName[i]);


            viewHolder.layerSwitch.setOnCheckedChangeListener(null);
            viewHolder.layerSwitch.setChecked(layerState[i]);
            viewHolder.layerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    layerState[position] = b;
                    layerSwitchListener.changeLayerState(layerState);
                }
            });
            return view;

        }

        private class ViewHolder{
            ImageView layerIcon;
            TextView layerName;
            CheckBox layerSwitch;
        }

    }

    public void show(View view){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        showAtLocation(view, Gravity.NO_GRAVITY,location[0]+view.getMeasuredWidth()/2,location[1]+view.getMeasuredHeight()/2);
    }
}
