package com.joy.cloudshare.adapter;

import java.io.File;
import java.util.List;

import com.joy.cloudshare.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareDirAdapter extends BaseAdapter {
	private List<File> mFiles;
	private LayoutInflater mInflater;
	private int mSelectedIndex = -1;
	public ShareDirAdapter(Context context,List<File> files){
		this.mFiles = files;
		this.mInflater = LayoutInflater.from(context);
	}
	public void setSelectItem(int position){
		mSelectedIndex = position;
	}

	public int getCount() {
		return mFiles.size();
	}

	public Object getItem(int position) {
		return mFiles.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.dir_list_item, null);
			holder.mDirCheckbox = (CheckBox) convertView.findViewById(R.id.dir_check);
			holder.mDirNameTextView = (TextView) convertView.findViewById(R.id.dir_name);
			holder.mArrowsImageView = (ImageView) convertView.findViewById(R.id.arrows_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final File tempFile = mFiles.get(position);
		if(tempFile.isDirectory()){
			holder.mDirCheckbox.setVisibility(View.VISIBLE);
			holder.mArrowsImageView.setVisibility(View.VISIBLE);
		}else{
			holder.mDirCheckbox.setVisibility(View.INVISIBLE);
			holder.mArrowsImageView.setVisibility(View.INVISIBLE);
		}
		if(mSelectedIndex == position){
			holder.mDirCheckbox.toggle();
		}else{
			holder.mDirCheckbox.setChecked(false);
		}
		holder.mDirNameTextView.setText(tempFile.getName()); 
		holder.mDirCheckbox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					Log.d("sharepath", tempFile.getAbsolutePath());
				}else{
					Log.d("sharepath", "nothing");
				}
			}
		});
		return convertView;
	}

	static class ViewHolder{
		CheckBox mDirCheckbox;
		TextView mDirNameTextView;
		ImageView mArrowsImageView;
	}
}
