package com.joy.cloudshare.adapter;

import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joy.cloudshare.R;
import com.joy.cloudshare.common.MediaInfo;

public class MediaMusicAdapter extends BaseAdapter {
    private List<MediaInfo> musicList;
    private LayoutInflater     inflater;
    private Context            context;
    private int                selectedIndex = -1;
    
    public MediaMusicAdapter(Context context, List<MediaInfo> audioList) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.musicList = audioList;
    }
    
    public void selected(int index) {
        this.selectedIndex = index;
        notifyDataSetChanged();
    }
    
    public int getCount() {
        // TODO Auto-generated method stub
        return musicList.size();
    }
    
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return musicList.get(position);
    }
    
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.media_music_list_item, null);
            holder.name_text_view = (TextView) convertView
                    .findViewById(R.id.media_music_list_item_name);
            holder.author_text_view = (TextView) convertView
                    .findViewById(R.id.media_music_list_item_author);
            holder.time_text_view = (TextView) convertView
                    .findViewById(R.id.media_music_list_item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.name_text_view.setText((position+1) + ". "
                + musicList.get(position).getName());
        holder.author_text_view.setText(musicList.get(position).getAuthor());
        holder.time_text_view.setText(musicList.get(position).getDuration());
        if(selectedIndex == position){
            holder.name_text_view.setTextColor(Color.BLACK);
            holder.author_text_view.setTextColor(Color.DKGRAY);
            holder.time_text_view.setTextColor(Color.DKGRAY);
            convertView.setBackgroundResource(R.drawable.ic_content_select);
        }else{
            holder.name_text_view.setTextColor(Color.WHITE);
            holder.author_text_view.setTextColor(Color.LTGRAY);
            holder.time_text_view.setTextColor(Color.LTGRAY);
            convertView.setBackgroundResource(R.drawable.media_list_selector);
        }
        return convertView;
    }
    
    static class ViewHolder {
        TextView name_text_view;
        TextView author_text_view;
        TextView time_text_view;
    }
    
}
