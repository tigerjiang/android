package com.joy.cloudshare.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.joy.cloudshare.R;
import com.joy.cloudshare.activity.SettingActivity;


public class HeaderBarView extends RelativeLayout implements OnClickListener{

    private Context mContext;
    private ImageButton mDeviceSelButton;
    private ImageButton mSearchButton;
	public HeaderBarView(Context context) {
		super(context);
		mContext = context;
	}

	public HeaderBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public HeaderBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDeviceSelButton = (ImageButton) findViewById(R.id.head_device_btn);
        mSearchButton = (ImageButton) findViewById(R.id.head_search_btn);
        mDeviceSelButton.setOnClickListener(this);
        mSearchButton.setOnClickListener(this);
    }

    public void onClick(View v) {
      switch(v.getId()){
          case R.id.head_device_btn:
        	  mContext.startActivity(new Intent(mContext, SettingActivity.class));
//              Toast.makeText(mContext, "device", Toast.LENGTH_SHORT).show();
              break;
          case R.id.head_search_btn:
//              Toast.makeText(mContext, "search", Toast.LENGTH_SHORT).show();
              break;
      }
        
    }

	
}
