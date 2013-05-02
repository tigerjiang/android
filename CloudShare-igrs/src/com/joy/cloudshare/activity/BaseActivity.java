
package com.joy.cloudshare.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.joy.cloudshare.common.DeviceTypeVO;
import com.joy.cloudshare.utils.ComConstants;
import com.joy.cloudshare.utils.ComUtils;
import com.joy.cloudshare.view.CommonDialogFragement;

public class BaseActivity extends Activity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private ArrayList<DeviceTypeVO> mDiviceList = new ArrayList<DeviceTypeVO>();
    private String str = "";

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ComConstants.MST_PREPARED_GET_DEVICES_LIST:
                    Log.d(TAG, "init");
                    break;
                case ComConstants.MST_GET_DEVICES_LIST_OVER: 
                    onInitComplete(str);
                    break;
            }
        }
    };


    private void onInit()
    {
        Log.d(TAG, "init");
    }

    private void onInitComplete(String str)
    {
        Log.d(TAG, "--------------------------->" + str);
//        deviceList = str;
//        showTvlist();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(conn);
    }

    // liulihuan 加上新手教程后的create
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        bindService(new Intent(IDeviceService.class.getName()), conn, Context.BIND_AUTO_CREATE);
//        startService(new Intent(IDeviceService.class.getName()));

    }

//    private void showTvlist()
//    {
//        mDiviceList  = (ArrayList<DeviceTypeVO>)ComUtils.getDeviceListFromServer(deviceList);
//        CommonDialogFragement mDeviceDialogFragement = CommonDialogFragement.newInstance();
//        Bundle args = new Bundle();
////        int currentIndex = mDiviceList.indexOf(ComUtils.getCurrentDevice());
//       int  currentIndex = ComUtils.getIndexOfDevices();
//        args.putSerializable(CommonDialogFragement.DEVICE_LIST, mDiviceList);
//        args.putInt(CommonDialogFragement.SELECTED_INDEX,currentIndex);
//        final FragmentManager fm = getFragmentManager();
//        fm.beginTransaction();
//        mDeviceDialogFragement.show(getFragmentManager(), "dialog");
//    }

}
