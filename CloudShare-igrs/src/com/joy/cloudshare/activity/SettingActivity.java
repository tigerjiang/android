package com.joy.cloudshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.joy.cloudshare.R;

public class SettingActivity extends Activity implements OnClickListener {
	private RelativeLayout mShareLayout;
	private RelativeLayout mDeviceLayout;
	private Button mBcakButton;
	private static final int SET_SHARE_REQUESTCODE = 1;
	private static final int SET_DEVICE_REQUESTCODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		mShareLayout = (RelativeLayout) findViewById(R.id.set_share_layout);
		mDeviceLayout = (RelativeLayout) findViewById(R.id.set_device_layout);
		mBcakButton = (Button) findViewById(R.id.set_title_back);
		mShareLayout.setOnClickListener(this);
		mDeviceLayout.setOnClickListener(this);
		mBcakButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.set_share_layout:
			intent.setClass(this,ShareActivity.class);
			startActivityForResult(intent, SET_SHARE_REQUESTCODE);
			break;
		case R.id.set_device_layout:
			intent.setClass(this,DevicesActivity.class);
			startActivityForResult(intent, SET_DEVICE_REQUESTCODE);
			break;
		case R.id.set_title_back:
			this.finish();
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==RESULT_OK){
		  Intent resultData = data;
		  if(requestCode == SET_SHARE_REQUESTCODE){
			  //set share
		  }else if(requestCode == SET_DEVICE_REQUESTCODE){
			  //select device
		  }
		}
	}
}
