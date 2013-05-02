package com.joy.cloudshare.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.igrs.base.IProviderExporterService;
import com.igrs.base.IgrsBaseConnectListener;
import com.igrs.base.android.util.IgrsType.DeviceType;
import com.igrs.base.appcallbacks.IConnectionCallback;
import com.igrs.base.demo.GlobalControl;
import com.igrs.base.demo.IgrsBaseProxyManager;
import com.igrs.base.demo.TransmissionMultiMediaManager;
import com.igrs.base.lan.IgrsLanInfo;
import com.igrs.base.services.callbacks.IFetchLanFriendsListCallback;
import com.igrs.base.services.lantransfer.IgrsBaseExporterLanService;
import com.igrs.base.util.IgrsTag;
import com.joy.cloudshare.R;
import com.joy.cloudshare.common.MediaInfo;
import com.joy.cloudshare.utils.ComUtils;
import com.joy.cloudshare.view.MediaMusicFragment;
import com.joy.cloudshare.view.MediaPicFragment;
import com.joy.cloudshare.view.MediaVideoFragment;

@SuppressLint("NewApi")
public class MediaMainActivity extends Activity {
	private static final String TAG = "MediaMainActivity";

	private LinearLayout mDeviceLayout;
	private RadioGroup mFileTypeLayout;
	private MediaVideoFragment mVideoFragment;
	private MediaPicFragment mPicFragment;
	private MediaMusicFragment mMusicFragment;
	private Fragment mContentFragment;
	private RadioButton mPicRadioButton;
	private RadioButton mMusicRadioButton;
	private RadioButton mVideoRadioButton;
	private SharedPreferences mSharedPreferences;
	private int mSelectedFlitTypeViewResId;
	private int mSelectedDeviceViewResId;
	private String mRootPath;
	private Context mContext; 
	private IgrsBaseProxyManager igrsBaseProxyManager = IgrsBaseProxyManager
			.getInstance();


	// deviceid
	public static String deviceid;
	// Device type ,Tv or pad
	public DeviceType deviceType;

	private IgrsBaseConnectListener igrsBaseConnectListener = null;

	ExecutorService singleThread = Executors.newSingleThreadExecutor();

	private boolean isDisplayNet = false;

	/**
	 * ����״̬
	 */
	private boolean serverConnecting = false,
	/**
	 * wifi״̬
	 */
	lanNetworkConnect = false,
	/**
	 * 3g ״̬
	 */
	internetConnect = false;
	private Handler netConnectHandler = null, notifyHandler;
	private IProviderExporterService iProviderExporterService;
	private IgrsBaseExporterLanService igrsBaseExporterLanService;
	private TransmissionMultiMediaManager transmissionMultiMediaManager;
	List<String> fTemp = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_main_layout);
		mDeviceLayout = (LinearLayout) findViewById(R.id.media_devices_layout);
		mDeviceLayout.getChildAt(0).setSelected(true);
		mSelectedDeviceViewResId = mDeviceLayout.getChildAt(0).getId();
		mFileTypeLayout = (RadioGroup) findViewById(R.id.media_file_type_layout);
		mSelectedFlitTypeViewResId = mFileTypeLayout.getCheckedRadioButtonId();
		mPicRadioButton = (RadioButton) findViewById(R.id.media_pic_layout);
		mMusicRadioButton = (RadioButton) findViewById(R.id.media_music_layout);
		mVideoRadioButton = (RadioButton) findViewById(R.id.media_video_layout);
		mContext = this.getApplicationContext();
		mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		ComUtils.getInstance().setSharePath(MediaMainActivity.this,mRootPath);
		ComUtils.getInstance().setCurrentIgrsLanInfo(MediaMainActivity.this,ComUtils.INVALID_SERVICENAME);
		initIgrsService();
		initDefaultState();
		initLocalDevices();
		initView();
		// initLocalData();

	}

	// Initial the igrs service
	private void initIgrsService(){
       netConnectHandler = new Handler() {
           @Override
           public void handleMessage(Message msg) {

               if (msg.arg2 == GlobalControl.SERVICE_CONNECT_SUCCESS) {
                   igrsBaseConnectListener.onServiceConnected();
               } else if (msg.arg2 == GlobalControl.SERVICE_CONNECT_FAILURE) {
                   android.os.Process.killProcess(android.os.Process.myPid());
               }
           }
       };
       
       notifyHandler = new Handler() {
       
           @Override
           public void handleMessage(Message msg) {
               if (msg.what == GlobalControl.LOCAL_DEVICE_LIST_REFRESH) {
                   
                   ////
                      Date date=new Date();
                       SimpleDateFormat simple=new       SimpleDateFormat ();
                      simple.applyPattern("yyyy-MM-dd hh:mm:ss");
                      Log.i(TAG,"i receiver ,message:"+simple.format(date));
                      
               }else   
               {
                   
               }
           }
       };
       try {
           igrsBaseProxyManager.connectToIgrsBaseService(this,
                   netConnectHandler);
         } catch (RemoteException e) {
           Message msg = new Message();
           msg.arg2 = GlobalControl.SERVICE_CONNECT_FAILURE;
           netConnectHandler.sendMessage(msg);
       }
       igrsBaseConnectListener = new IgrsBaseConnectListener() {
           
          
           public void onServiceConnected() {
               initHandlerCallBack();
               try {
                   igrsBaseExporterLanService = igrsBaseProxyManager
                           .getLanNetWorkService();
                   if (igrsBaseExporterLanService == null) {
                       return;
                   }
                 } catch (RemoteException e2) {
                    e2.printStackTrace();
                    return;
                 }
               try {
                    iProviderExporterService = igrsBaseProxyManager.getConnectService();
               
                    if (iProviderExporterService != null)
                        iProviderExporterService.registerConnectionCallback(iConnectionCallback); // ע�����������״̬�ص�
               } catch (RemoteException e2) {
                    e2.printStackTrace();
                 }

               transmissionMultiMediaManager = new TransmissionMultiMediaManager(MediaMainActivity.this, iProviderExporterService,igrsBaseExporterLanService, notifyHandler);
               igrsBaseProxyManager.registerIgrsProxyResultConnectHandler(IgrsTag.SEND_COMMAND_CONTROL, notifyHandler);
                try {
                     lanNetworkConnect = igrsBaseProxyManager.getConnectService().isLanNetWorkConnecting(); // �жϾ���������״̬
                    } catch (RemoteException e1) {
                     e1.printStackTrace();
                    }
                    
                try {
                   List<IgrsLanInfo> friendsList = igrsBaseProxyManager.getLanNetWorkService().getFriendsList(); // �õ��豸�б�
                   if (friendsList != null && friendsList.size() > 0) {
                       for (IgrsLanInfo igrsLanInfo : friendsList) {
                           if (igrsLanInfo.getDeviceType() == DeviceType.tv
                                   || igrsLanInfo.getDeviceType() == DeviceType.pda)// ֻ��ʾpad��tv�豸��������Զ���豸
                               fTemp.add(igrsLanInfo.getServiceName());
                       }
                   }
                   Log.i(TAG, String.valueOf(friendsList.size()));
                 } catch (RemoteException e) {
                    e.printStackTrace();
                 }

             // 注册局域网设备时时更新回调函数
				try {
					igrsBaseProxyManager.getLanNetWorkService()
							.registerIFetchLanFriendsListCallback(
									ifetchFriendsCallBacks, "");
				} catch (RemoteException e) {
					e.printStackTrace();
				}

           
               initLocalData();
               mContentFragment = mPicFragment;
               initData(mContentFragment);
           }

           public void onServiceDisconnected() {

           }
       };
       
       
   }

	
	private IConnectionCallback iConnectionCallback = new IConnectionCallback.Stub() {

		public void onConnectionChanged(boolean connected)
				throws RemoteException {
			serverConnecting = connected;// ����������״̬
		}

		public void onConnectionLanChanged(boolean wificonnected)
				throws RemoteException {
			lanNetworkConnect = wificonnected;// ����������״̬�ı�ʱ
		}

		public void onConnectionInternetChanged(boolean internetConnected)
				throws RemoteException {
			internetConnect = internetConnected; // ����
		}

		public void onRunningException(boolean runningException)
				throws RemoteException {
			// ���쳣ʱ
		}

		public void serviceLoadingFinish() throws RemoteException {
			// TODO Auto-generated method stub

		}
	};

	// get local devices
	private IFetchLanFriendsListCallback ifetchFriendsCallBacks = new IFetchLanFriendsListCallback.Stub() {

		public void processFriendsUpdate(List<IgrsLanInfo> friendList)
				throws RemoteException {

			if (!isDisplayNet) {
				Message msg = new Message();
				msg.obj = friendList; // list Ϊ�豸�б� ����ui����
				msg.what = GlobalControl.LOCAL_DEVICE_LIST_REFRESH;
				fTemp.clear();
				for (IgrsLanInfo info : friendList) {
					fTemp.add(info.getServiceName());
				}
				if(!fTemp.contains(ComUtils.getInstance().getCurrentIgrsLanServiceName())){
					ComUtils.getInstance().setCurrentIgrsLanInfo(MediaMainActivity.this,ComUtils.INVALID_SERVICENAME);
				}
				notifyHandler.sendMessage(msg);
			}
		}
	};

	public void sendLocalRestoLanDevices(MediaInfo mediaData,
			String serviceName) {
		transmissionMultiMediaManager.sendLocalResourceToLanFriends(mediaData,
				serviceName);
	}

	/**
	 * register handler callback
	 */
	private void initHandlerCallBack() {
		igrsBaseProxyManager.registerIgrsProxyResultConnectHandler(
				IgrsTag.RELOCAL_ADDRESS, notifyHandler);
	}

	// initial the view
	private void initView() {
		mFileTypeLayout
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.media_pic_layout:
							mContentFragment = mPicFragment;
							initData(mContentFragment);
							break;
						case R.id.media_music_layout:
							mContentFragment = mMusicFragment;
							initData(mContentFragment);
							break;
						case R.id.media_video_layout:
							mContentFragment = mVideoFragment;
							initData(mContentFragment);
							break;
						default:
							break;
						}
						mSelectedFlitTypeViewResId = checkedId;
						changeState(checkedId);
					}
				});

	}

	private void initLocalDevices() {
		for (int i = 0; i < mDeviceLayout.getChildCount(); i++) {
			final int index = i;
			mDeviceLayout.getChildAt(i).setOnClickListener(
					new View.OnClickListener() {

						public void onClick(View v) {
							setSelectedItem(index);
						}
					});
		}
		for (int i = 0; i < mDeviceLayout.getChildCount(); i++) {

			mDeviceLayout.getChildAt(i).setOnFocusChangeListener(
					new OnFocusChangeListener() {

						public void onFocusChange(View v, boolean hasFocus) {
							if (mSelectedDeviceViewResId == v.getId()) {
								return;
							}
							if (hasFocus) {
								((TextView) ((ViewGroup) v).getChildAt(1))
										.setTextColor(Color.WHITE);
							} else {
								((TextView) ((ViewGroup) v).getChildAt(1))
										.setTextColor(Color.GRAY);
							}

							Log.d(TAG, "setOnFocusChangeListener");
						}
					});
		}

	}

	private void initLocalData() {
		mVideoFragment = new MediaVideoFragment();
		mPicFragment = new MediaPicFragment();
		mMusicFragment = new MediaMusicFragment();
	}

	private void setSelectedItem(int index) {
		for (int i = 0; i < mDeviceLayout.getChildCount(); i++) {
			TextView tvNmae = (TextView) ((ViewGroup) mDeviceLayout
					.getChildAt(i)).getChildAt(1);
			if (i == index) {
				mDeviceLayout.getChildAt(i).setSelected(true);
				tvNmae.setTextColor(Color.WHITE);
			} else {
				mDeviceLayout.getChildAt(i).setSelected(false);
				tvNmae.setTextColor(Color.GRAY);
			}
		}
	}

	@SuppressLint("NewApi")
	private void initData(Fragment mContentFragment) {
		// Execute a transaction, replacing any existing fragment
		// with this one inside the frame.
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.details, mContentFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	private void changeState(int resId) {
		for (int i = 0; i < mFileTypeLayout.getChildCount(); i++) {
			if (mFileTypeLayout.getChildAt(i).getId() == resId) {
				((RadioButton) mFileTypeLayout.getChildAt(i))
						.setTextColor(Color.WHITE);
			} else {
				((RadioButton) mFileTypeLayout.getChildAt(i))
						.setTextColor(Color.GRAY);
			}
		}
	}

	private void initDefaultState() {
		for (int i = 0; i < mFileTypeLayout.getChildCount(); i++) {
			mFileTypeLayout.getChildAt(i).setOnTouchListener(
					new OnTouchListener() {

						public boolean onTouch(View v, MotionEvent event) {
							if (mSelectedFlitTypeViewResId == v.getId()) {
								return false;
							}
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								((RadioButton) v).setTextColor(Color.WHITE);
							} else if (event.getAction() == MotionEvent.ACTION_UP) {
								((RadioButton) v).setTextColor(Color.GRAY);
							}
							return false;
						}
					});
		}
	}
	
	@Override
	protected void onDestroy() {
		igrsBaseProxyManager.unBindOndestry(MediaMainActivity.this);
		super.onDestroy();
	}

}
