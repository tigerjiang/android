package com.joy.cloudshare.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.igrs.base.android.util.ResourceInfo;
import com.igrs.base.android.util.IgrsType.FileType;
import com.igrs.base.demo.IgrsBaseProxyManager;
import com.igrs.base.services.callbacks.IFetchMultimediaResourceCallback;
import com.joy.cloudshare.R;
import com.joy.cloudshare.activity.MediaMainActivity;
import com.joy.cloudshare.adapter.MediaMusicAdapter;
import com.joy.cloudshare.adapter.MediaVideoAdapter;
import com.joy.cloudshare.common.CommonResource;
import com.joy.cloudshare.common.MediaInfo;
import com.joy.cloudshare.utils.ComUtils;
import com.joy.cloudshare.utils.ComUtils.OnShareChangListener;
import com.joy.cloudshare.view.PushDialogFragement.OnMediaPushListener;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MediaVideoFragment extends Fragment implements
		OnItemClickListener, OnShareChangListener {

	private ListView mVideoListView;
	private Context mContext;
	private List<MediaInfo> mVideoInfos;
	private MediaVideoAdapter mVideoAdapter;
	private String mSharePath = "/mnt/sdcard";
	private View mRootView;
	private static final String TAG = MediaVideoFragment.class.getSimpleName();
	private IgrsBaseProxyManager igrsBaseProxyManager = IgrsBaseProxyManager
			.getInstance();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.media_vedio_layout, null);
		mContext = this.getActivity();
		mVideoListView = (ListView) mRootView
				.findViewById(R.id.media_vedio_list);
		mVideoListView.setOnItemClickListener(this);
		ComUtils.getInstance().registerShareChangeListener(this);
		mSharePath = ComUtils.getInstance().getSharePath();
		refreshData(mSharePath);
		return mRootView;
	}

	private void refreshData(String sharePath) {

		mVideoInfos = CommonResource.getVideoResourceList(CommonResource
				.getFilesCursor(mContext, FileType.video, mSharePath));
		mVideoAdapter = new MediaVideoAdapter(mContext, mVideoInfos);
		mVideoListView.setAdapter(mVideoAdapter);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mVideoAdapter.selected(position);
		final MediaInfo mediaData = mVideoInfos.get(position);
		final PushDialogFragement dialogFragement = PushDialogFragement.newInstance();
		Bundle args = new Bundle();
		args.putSerializable("info", mediaData);
		dialogFragement.setArguments(args);
		dialogFragement.setmMediaPushListener(new OnMediaPushListener() {

			public void push() {
				if(mediaData!=null&& ComUtils.getInstance().getCurrentIgrsLanServiceName()!=null&&!ComUtils.getInstance().getCurrentIgrsLanServiceName().equals(ComUtils.INVALID_SERVICENAME)){
	  				((MediaMainActivity)getActivity()).sendLocalRestoLanDevices(mediaData, ComUtils.getInstance().getCurrentIgrsLanServiceName());
	  				}else{
	  					dialogFragement.dismiss();
	  					ComUtils.getInstance().proptMsg(mContext,"目前没有连接的设备,请选择一个设备");
	  				}
	  				Log.d(TAG, "push");
			}
		});

		dialogFragement.show(getFragmentManager(), "push_dialog");
	}

	public void onSharePathChanger(String path) {
		refreshData(path);
		
	}
}
