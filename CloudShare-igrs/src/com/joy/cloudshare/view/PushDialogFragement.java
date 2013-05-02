package com.joy.cloudshare.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.igrs.base.android.util.IgrsType.FileType;
import com.joy.cloudshare.R;
import com.joy.cloudshare.common.MediaInfo;

public class PushDialogFragement extends DialogFragment implements
		OnClickListener {

	private View mRootView;
	private MediaInfo mInfo;
	private Button mBackButton, mPushButton;
	private ImageView mInfoTypeLogo;
	private TextView mNameTxt;
	OnMediaPushListener mMediaPushListener;

	public static PushDialogFragement newInstance() {
		return new PushDialogFragement();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, this.getTheme());
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.push_dialog, null);
		initView(mRootView);
		initData();
		return mRootView;
	}
	
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.push_dialog, null);
//		initView(mRootView);
//		initData();
//		return new AlertDialog.Builder(getActivity()).setView(mRootView).create();
//		
//	}

	private void initView(View view) {
		mBackButton = (Button) view.findViewById(R.id.back);
		mPushButton = (Button) view.findViewById(R.id.push);
		mInfoTypeLogo = (ImageView) view.findViewById(R.id.media_type_log);
		mNameTxt = (TextView) view.findViewById(R.id.name_txt);
		mBackButton.setOnClickListener(this);
		mPushButton.setOnClickListener(this);

	}

	private void initData() {
		Bundle bundle = this.getArguments();
		if (bundle != null) {
			mInfo = (MediaInfo) bundle.getSerializable("info");
		} else {
			mInfo = null;
			return;
		}
		if (mInfo.getFileType() == FileType.pic) {
			mInfoTypeLogo.setImageBitmap(Thumbnails.getThumbnail(getActivity().getContentResolver(),
	                Integer.valueOf(mInfo.getId()),
	                Thumbnails.MICRO_KIND, new BitmapFactory.Options()));
		} else if (mInfo.getFileType() == FileType.music) {
			mInfoTypeLogo.setImageResource(R.drawable.ic_music_bg);
		} else if (mInfo.getFileType() == FileType.video) {
			mInfoTypeLogo.setImageResource(R.drawable.ic_movie_bg);
		}
		mNameTxt.setText(mInfo.getName());

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.dismiss();
			break;
		case R.id.push:
			mMediaPushListener.push();
			this.dismiss();
			break;
		}

	}

	public OnMediaPushListener getmMediaPushListener() {
		return mMediaPushListener;
	}

	public void setmMediaPushListener(OnMediaPushListener mMediaPushListener) {
		this.mMediaPushListener = mMediaPushListener;
	}

	public interface OnMediaPushListener {
		public void push();
	}
	
}
