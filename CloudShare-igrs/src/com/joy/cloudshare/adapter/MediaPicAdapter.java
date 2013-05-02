package com.joy.cloudshare.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.joy.cloudshare.R;
import com.joy.cloudshare.common.MediaInfo;

public class MediaPicAdapter extends BaseAdapter {
    private List<MediaInfo> resourceList;
    private LayoutInflater     inflater;
    private Context            context;
    private int                selectedIndex = -1;
    
    public MediaPicAdapter(Context context, List<MediaInfo> resourceList) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resourceList = resourceList;
    }
    
    public void selected(int index) {
        this.selectedIndex = index;
        notifyDataSetChanged();
    }
    
    public int getCount() {
        return resourceList.size();
    }
    
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return resourceList.get(position);
    }
    
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        Bitmap bitmap;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.grid_row, null);
            holder.pic_img = (ImageView) convertView
                    .findViewById(R.id.imageItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bitmap = Thumbnails.getThumbnail(context.getContentResolver(),
                Integer.valueOf(resourceList.get(position).getId()),
                Thumbnails.MICRO_KIND, new BitmapFactory.Options());
        if (selectedIndex == position) {
            holder.pic_img.setPadding(12, 12, 12, 12);
            holder.pic_img.setScaleType(ScaleType.FIT_XY);
        } else {
            holder.pic_img.setPadding(0, 0, 0, 0);
            holder.pic_img.setScaleType(ScaleType.FIT_XY);
        }
        holder.pic_img.setImageBitmap(bitmap);
        return convertView;
    }
    
    
    static class ViewHolder {
        ImageView pic_img;
    }
}
