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
import com.joy.cloudshare.adapter.MediaPicAdapter;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class MediaMusicFragment extends Fragment implements
        OnItemClickListener, OnItemSelectedListener,OnShareChangListener {
    private ListView mMusicListView;
    private Context mContext;
    private List<MediaInfo> mMusicResourceList;
    private MediaMusicAdapter mMusicAdapter;
    private String mSharePath = "/mnt/sdcard";
    private View mRootView;
    private static final String TAG = MediaMusicFragment.class.getSimpleName();
    private IgrsBaseProxyManager igrsBaseProxyManager = IgrsBaseProxyManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.media_music_layout, null);
        mContext = this.getActivity();
        mMusicListView = (ListView) mRootView
                .findViewById(R.id.media_music_list);
        mMusicListView.setOnItemSelectedListener(this);
        mMusicListView.setOnItemClickListener(this);
        ComUtils.getInstance().registerShareChangeListener(this);
        mSharePath = ComUtils.getInstance().getSharePath();
        refreshData(mSharePath);
        return mRootView;
    }

    private void refreshData(String sharePath) {
        mMusicResourceList = CommonResource.getAudioResourceList(CommonResource
                .getFilesCursor(mContext, FileType.music, mSharePath));
        mMusicAdapter = new MediaMusicAdapter(mContext, mMusicResourceList);
        mMusicListView.setAdapter(mMusicAdapter);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        mMusicAdapter.selected(position);
       final MediaInfo mediaData = mMusicResourceList.get(position);
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

    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        mMusicListView.getChildAt(position).setSelected(true);
        view.setSelected(true);
        
        Log.d("MediaMusicFragment", "onItemSelected");
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

	public void onSharePathChanger(String path) {
		refreshData(path);
		
	}

}
