package com.joy.cloudshare.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.igrs.base.android.util.IgrsType.DeviceType;
import com.igrs.base.demo.IgrsBaseProxyManager;
import com.igrs.base.lan.IgrsLanInfo;
import com.joy.cloudshare.R;
import com.joy.cloudshare.adapter.DeviceAdapter;
import com.joy.cloudshare.adapter.TvAdapter;
import com.joy.cloudshare.utils.ComUtils;

public class DevicesActivity extends Activity implements OnItemClickListener,
        OnClickListener {
    private List<IgrsLanInfo> igrsLanInfos;
    private TvAdapter mDeviceAdapter;
    private Context mContext;
    private ListView mDeviveListView;
    private Button mBackButton;
    private TextView mDeviceName;
    private IgrsLanInfo mCurrentIgrsLanInfo;
    private String mCurrentServiceName;
    private IgrsBaseProxyManager igrsBaseProxyManager = IgrsBaseProxyManager
            .getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices);
        mContext = getApplicationContext();
        initView();
        initData();
    }

    private void initView() {
        mDeviveListView = (ListView) findViewById(R.id.device_list);
        mDeviveListView.setOnItemClickListener(this);
        mBackButton = (Button) findViewById(R.id.set_title_back);
        mBackButton.setOnClickListener(this);
        mDeviceName = (TextView) findViewById(R.id.current_device_name);
    }

    private void initData() {
        mCurrentServiceName = ComUtils.getInstance()
                .getCurrentIgrsLanServiceName();
        igrsLanInfos = new ArrayList<IgrsLanInfo>();
        refreshDevices();
        if (igrsLanInfos.size() > 0) {

        } else {
            TextView emptyView = new TextView(mContext);
            emptyView.setText("没有发现设备");
            mDeviveListView.setEmptyView(emptyView);
        }
        mDeviceAdapter = new TvAdapter(mContext, igrsLanInfos);
        mDeviveListView.setAdapter(mDeviceAdapter);
        List<String> tempList = new ArrayList<String>();
        for (IgrsLanInfo info : igrsLanInfos) {
            tempList.add(info.getServiceName());
        }
        if (tempList.contains(mCurrentServiceName)) {
            mDeviceName.setText(mCurrentServiceName);
            mDeviceAdapter.setSelectedItem(tempList
                    .indexOf(mCurrentServiceName));
        } else {
            mDeviceName.setText("无设备");
        }

    }

    public void onClick(View v) {
        if (v.getId() == R.id.set_title_back) {
            this.finish();
        }

    }

    public void onItemClick(AdapterView<?> viewGroup, View view, int position,
            long id) {
        mCurrentIgrsLanInfo = igrsLanInfos.get(position);
        ComUtils.getInstance().setCurrentIgrsLanInfo(mContext,
                mCurrentIgrsLanInfo.getServiceName());
        mDeviceName.setText(mCurrentIgrsLanInfo.getServiceName());
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.device_check);
        checkbox.toggle();
        mDeviceAdapter.setSelectedItem(position);
        mDeviceAdapter.notifyDataSetChanged();

    }

    private void refreshDevices() {
        try {
            List<IgrsLanInfo> friendsList = igrsBaseProxyManager
                    .getLanNetWorkService().getFriendsList(); // 得到设备列表
            if (friendsList != null && friendsList.size() > 0) {
                for (IgrsLanInfo igrsLanInfo : friendsList) {
                    if (igrsLanInfo.getDeviceType() == DeviceType.tv
                            || igrsLanInfo.getDeviceType() == DeviceType.pda)// 只显示pad和tv设备，不包括远程设备
                        igrsLanInfos.add(igrsLanInfo);

                }
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}