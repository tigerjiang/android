package com.joy.cloudshare.adapter;

import java.io.File;
import java.util.List;

import com.igrs.base.lan.IgrsLanInfo;
import com.joy.cloudshare.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class TvAdapter extends BaseAdapter {

	private List<IgrsLanInfo> mDevices;
	private LayoutInflater mInflater;
	private int index = -1;
	
	public TvAdapter(Context context,List<IgrsLanInfo> devices){
		this.mDevices = devices;
		this.mInflater = LayoutInflater.from(context);
	}

	public void setSelectedItem(int position){
		this.index = position;
	}
	public int getCount() {
		return mDevices.size();
	}

	public Object getItem(int position) {
		return mDevices.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.device_list_item, null);
			holder.mDeviceCheckbox = (CheckBox) convertView.findViewById(R.id.device_check);
			holder.mDeviceNameTextView = (TextView) convertView.findViewById(R.id.device_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final IgrsLanInfo tempDevice = mDevices.get(position);
		if(index == position){
//			holder.mDeviceCheckbox.toggle();
			holder.mDeviceCheckbox.setChecked(true);
		}else{
			holder.mDeviceCheckbox.setChecked(false);
		}
		holder.mDeviceNameTextView.setText(tempDevice.getServiceName()); 
		holder.mDeviceCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					Log.d("sharepath", tempDevice.getServiceName());
				}else{
					Log.d("sharepath", "nothing");
				}
			}
		});
		return convertView;
	}

	static class ViewHolder{
		CheckBox mDeviceCheckbox;
		TextView mDeviceNameTextView;
	}
}