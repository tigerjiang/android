package com.joy.cloudshare.view;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.igrs.base.android.util.IgrsType.FileType;
import com.igrs.base.demo.IgrsBaseProxyManager;
import com.joy.cloudshare.R;
import com.joy.cloudshare.activity.MediaMainActivity;
import com.joy.cloudshare.adapter.MediaPicAdapter;
import com.joy.cloudshare.common.CommonResource;
import com.joy.cloudshare.common.MediaInfo;
import com.joy.cloudshare.utils.ComUtils;
import com.joy.cloudshare.utils.ComUtils.OnShareChangListener;
import com.joy.cloudshare.view.PushDialogFragement.OnMediaPushListener;

public class MediaPicFragment extends Fragment implements OnItemClickListener ,OnShareChangListener{
    private static final String TAG = MediaPicFragment.class.getSimpleName();
    private View mRootView;
    private GridView mPicGridView;
    private MediaPicAdapter mPicAdapter;
    private List<MediaInfo> mPicMediaInfos;
    private Context mContext;
    private String mSharePath = "/mnt/sdcard";
    private IgrsBaseProxyManager igrsBaseProxyManager = IgrsBaseProxyManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.media_pic_layout, null);
        mPicGridView = (GridView) mRootView
                .findViewById(R.id.media_pic_gridview);
        mPicGridView.setOnItemClickListener(this);
        mContext = this.getActivity();
        ComUtils.getInstance().registerShareChangeListener(this);
        mSharePath = ComUtils.getInstance().getSharePath();
        refreshData(mSharePath);
        return mRootView;
    }

    private void refreshData(String sharePath) {
    	
    	mPicMediaInfos = CommonResource.getPicResourceList(CommonResource
    			 .getFilesCursor(mContext, FileType.pic, mSharePath));
    	mPicAdapter = new MediaPicAdapter(mContext, mPicMediaInfos);		
    	mPicGridView.setAdapter(mPicAdapter);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        mPicAdapter.selected(position);
       final MediaInfo mediaData = mPicMediaInfos.get(position);
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
