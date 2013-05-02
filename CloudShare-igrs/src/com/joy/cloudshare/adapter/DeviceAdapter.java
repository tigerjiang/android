package com.joy.cloudshare.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joy.cloudshare.R;
import com.joy.cloudshare.common.DeviceInfo;

public class DeviceAdapter extends BaseAdapter {
    
    private List<DeviceInfo> devicesList;
    private LayoutInflater layoutInflater;
    private int selectedIndex = -1;
    public DeviceAdapter(Context context,
            List<DeviceInfo> devicesList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.devicesList = devicesList;
    }
    public void selected(int index){
        this.selectedIndex = index;
        notifyDataSetChanged();
    }
    public int getCount() {
        return devicesList.size();
    }
    
    public Object getItem(int position) {
        return devicesList.get(position);
    }
    
    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        if(convertView ==null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.device_item, null);
            holder.device_name = (TextView) convertView.findViewById(R.id.device_name);
            holder.device_logo = (ImageView) convertView.findViewById(R.id.device_logo);
            convertView.setTag(holder);
       }else{
           holder = (ViewHolder) convertView.getTag();
       }
        holder.device_name.setText(devicesList.get(position).getDeviceName());
        holder.device_logo.setImageResource(devicesList.get(position).getDeviceLogoId());
        if(selectedIndex == position){
            convertView.setBackgroundResource(R.drawable.ic_devices_select);
        }else{
            convertView.setBackgroundResource(R.drawable.ic_devices_focus);
        }
        return convertView;
    }
    
    static class ViewHolder{
        TextView device_name;
        ImageView device_logo;
    }
}
