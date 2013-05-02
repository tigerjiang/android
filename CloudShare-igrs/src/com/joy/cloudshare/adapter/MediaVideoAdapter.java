package com.joy.cloudshare.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joy.cloudshare.R;
import com.joy.cloudshare.common.MediaInfo;

public class MediaVideoAdapter extends BaseAdapter {
    private List<MediaInfo>   videoList;
    private LayoutInflater inflater;
    private Context context;
    private int selectedIndex = -1;
    public MediaVideoAdapter(Context context,List<MediaInfo> audioList) {
        super();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.videoList = audioList;
    }
    
    public void selected(int index){
        this.selectedIndex = index;
        notifyDataSetChanged();
    }
    
    public int getCount() {
        // TODO Auto-generated method stub
        return videoList.size();
    }

    
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return videoList.get(position);
    }

    
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

  
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.media_video_list_item, null);
            holder.name_text_view = (TextView) convertView
                    .findViewById(R.id.media_video_list_item_name);
//            holder.author_text_view = (TextView) convertView
//                    .findViewById(R.id.media_video_list_item_author);
            holder.time_text_view = (TextView) convertView
                    .findViewById(R.id.media_video_list_item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       holder.name_text_view.setText((position+1)+". "+videoList.get(position).getName());
//        holder.author_text_view.setText(videoList.get(position).getAuthor());
        holder.time_text_view.setText(videoList.get(position).getDuration());
        if(selectedIndex == position){
            convertView.setBackgroundResource(R.drawable.ic_content_focus);
        }else{
            convertView.setBackgroundResource(R.drawable.media_list_selector);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView name_text_view;
//        TextView author_text_view;
        TextView time_text_view;
    }
   
    
}
