package com.nju.urbangreen.zhenjiangurbangreen.attachments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nju.urbangreen.zhenjiangurbangreen.R;

import java.util.List;

/**
 * Created by Liwei on 2016/12/2.
 */
public class AttachmentRecordAdapter extends ArrayAdapter<OneAttachmentRecord> {
    int resourceId;

    public AttachmentRecordAdapter(Context context, int resource, List<OneAttachmentRecord> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OneAttachmentRecord record = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder.tvFileName = (TextView) view.findViewById(R.id.tv_attachment_name);
            viewHolder.tvFileSize = (TextView) view.findViewById(R.id.tv_attachment_size);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvFileName.setText(record.getFileName());
        viewHolder.tvFileSize.setText(record.getFileSize());
        return view;
    }

    class ViewHolder{
        TextView tvFileName;
        TextView tvFileSize;
    }
}
