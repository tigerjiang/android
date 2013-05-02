package com.joy.cloudshare.view;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.joy.cloudshare.common.DeviceTypeVO;
import com.joy.cloudshare.utils.ComUtils;

public class CommonDialogFragement extends DialogFragment {

    private View mRootView;
    private List<DeviceTypeVO> mDeviceList;
    private int checkedIndex;
    private Context mContext;
    private String[] mData;
    public static final String DEVICE_LIST = "device_list";
    public static final String SELECTED_INDEX = "index";
    public static final int INVALID_INDEX = -1;

    public static CommonDialogFragement newInstance() {
        return new CommonDialogFragement();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        checkedIndex = ComUtils.getIndexOfDevices();
        Bundle args = getArguments();
        if (args == null || mDeviceList == null || mDeviceList.size() == 0) {
            mData = new String[] { "K600D", "K700D", "K800D" };
        } else {
            mDeviceList = (List<DeviceTypeVO>) args
                    .getSerializable(DEVICE_LIST);
            checkedIndex = args.getInt(DEVICE_LIST, INVALID_INDEX);
            mData = new String[mDeviceList.size()];
            setData();
        }
    }

    private void setData() {
        for (int i = 0; i < mDeviceList.size(); i++) {
            if (TextUtils.isEmpty(mDeviceList.get(i).getName())) {
                mData[i] = mDeviceList.get(i).getName();
            } else {
                mData[i] = "no name";
            }
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(mContext).setSingleChoiceItems(mData,
                checkedIndex, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // ComUtils.setCurrentDevice(mDeviceList.get(which));
                        ComUtils.setIndex(which);
                        Toast.makeText(mContext, mData[which],
                                Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }).create();
    }
}
