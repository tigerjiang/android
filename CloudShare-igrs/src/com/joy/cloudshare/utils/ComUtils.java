package com.joy.cloudshare.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.igrs.base.demo.IgrsBaseProxyManager;
import com.igrs.base.lan.IgrsLanInfo;
import com.joy.cloudshare.R;
import com.joy.cloudshare.activity.DevicesActivity;
import com.joy.cloudshare.common.DeviceTypeVO;

public class ComUtils {

	private static DeviceTypeVO currentDevice = new DeviceTypeVO();
	public static int index;
	private static ComUtils instance;
	private  String  mCurrentIgrsLanServiceName;
	private OnShareChangListener shareChangListener;
	private String sharePath;
	private SharedPreferences mSharedPreferences;
	private String mRootPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath();
	public static final String INVALID_SERVICENAME = "invalid name"; 

	public static ComUtils getInstance() {
		if (instance == null) {
			synchronized (IgrsBaseProxyManager.class) {
				if (instance == null) {
					instance = new ComUtils();
				}
			}
		}
		return instance;
	}

	private ComUtils() {

	}

	public void setSharePath(Context context, String sharePath) {
		if (!TextUtils.isEmpty(this.sharePath)
				&& this.sharePath.equals(sharePath)) {
			return;
		}
		mSharedPreferences = context.getSharedPreferences(
				"cloud_share.profile", Context.MODE_WORLD_WRITEABLE);
		mSharedPreferences.edit().putString("share_path", sharePath).commit();
		setSharePath(mSharedPreferences.getString("share_path", mRootPath));
		this.sharePath = sharePath;
		if (shareChangListener != null)
			shareChangListener.onSharePathChanger(sharePath);
	}

	public String getSharePath() {
		return mSharedPreferences.getString("share_path", mRootPath);
	}

	public void setCurrentIgrsLanInfo(Context context, String  serviceName) {
		if (!TextUtils.isEmpty(this.mCurrentIgrsLanServiceName)
				&& this.mCurrentIgrsLanServiceName.equals(serviceName)) {
			return;
		}
		mSharedPreferences = context.getSharedPreferences(
				"cloud_share.profile", Context.MODE_WORLD_WRITEABLE);
		if(serviceName.equals(INVALID_SERVICENAME)&&!getCurrentIgrsLanServiceName().equals(INVALID_SERVICENAME)){
			return;
		}
		mSharedPreferences.edit().putString("igrs_servicename", serviceName).commit();
		setCurrentIgrsLanServiceName(mSharedPreferences.getString("igrs_servicename", serviceName));
		this.mCurrentIgrsLanServiceName = serviceName;
	}
	
	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
	}

	public String getCurrentIgrsLanServiceName() {
		return mSharedPreferences.getString("igrs_servicename", INVALID_SERVICENAME);
	}

	public  void setCurrentIgrsLanServiceName(String  serviceName) {
		this.mCurrentIgrsLanServiceName = serviceName;
	}

	/**
	 * 检测WIFI
	 * */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null == manager) {
			return false;
		} else {
			NetworkInfo[] info = manager.getAllNetworkInfo();
			if (null != info) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
			return false;
		}
	}

	/**
	 * WIFI
	 * */
	public static boolean checkWIFI(Context context, String title) {

		if (!isNetworkAvailable(context)) {
			showSettingPanel(context, title);
			return false;
		}
		return true;

	}

	/**
	 * 
	 * */
	private static void showSettingPanel(final Context context, String title) {
		Resources res = context.getResources();
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setMessage(res.getString(R.string.no_wlan_tip));
		// sx modify
		builder.setCancelable(false);
		builder.setPositiveButton(res.getString(R.string.setting),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// context.startActivity(new
						// Intent(Settings.ACTION_WIRELESS_SETTINGS));
						context.startActivity(new Intent(
								android.provider.Settings.ACTION_WIFI_SETTINGS));
						if (null != dialog) {
							dialog.dismiss();
						}
						// android.os.Process.killProcess(android.os.Process.myPid());
					}
				});
		builder.setNegativeButton(res.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (null != dialog) {
							dialog.dismiss();
							// android.os.Process.killProcess(android.os.Process.myPid());
						}
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static byte[] readFile(Context context, String fileName)
			throws IOException {
		InputStream fis = context.getAssets().open(fileName);
		byte[] b = new byte[1024];
		int n = 0;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while ((n = fis.read(b)) != -1) {
			byteArrayOutputStream.write(b);
		}
		byte content[] = byteArrayOutputStream.toByteArray();
		return content;
	}

	public static void saveToSdCard(String filename, byte[] content)
			throws IOException {
		String sDir = ComConstants.IGRS_BASE_PATH;
		File destDir = new File(sDir);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		File file = new File(sDir, filename);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(content);
		fos.close();
	}

	public static boolean checkInstall(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		List<String> pName = new ArrayList<String>();
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);
	}

	public static void toast(Context context, int tip) {
		String text = context.getResources().getString(tip).toString();
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void toast(Context context, String tip) {
		Toast.makeText(context, tip, Toast.LENGTH_LONG).show();
	}

	public static boolean checkEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static String nullToEmpty(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 设置当前设备
	 * */
	public static DeviceTypeVO getCurrentDevice() {
		return currentDevice;
	}

	/**
	 * 设置默认设备
	 * */
	public static void setCurrentDevice(DeviceTypeVO d) {
		currentDevice.setValue(d);
	}

	public static int getIndexOfDevices() {
		return index;
	}

	public static void setIndex(int i) {
		index = i;
	}

	public static List<DeviceTypeVO> getDeviceListFromServer(String str) {
		List<DeviceTypeVO> list = null;
		DeviceTypeVO device = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader(str));
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xpp.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					list = new ArrayList<DeviceTypeVO>();
					break;
				case XmlPullParser.START_TAG:
					if ("Device".equals(nodeName)) {
						device = new DeviceTypeVO();
					} else if ("alias".equals(nodeName)) {
						device.setName(xpp.nextText());
					} else if ("ip".equals(nodeName)) {
						device.setIp(xpp.nextText());
					} else if ("type".equals(nodeName)) {
						device.setType(xpp.nextText());
					} else if ("sourceinfo".equals(nodeName)) {
						device.setSourceDesp(getSourceDespMap(xpp.nextText()));
					} else if ("playertype".equals(nodeName)) {
						device.setPlayerType(xpp.nextText());
					} else if ("screenswitch".equals(nodeName)) {
						device.setScreenSupport(xpp.nextText());
					} else if ("irsupport".equals(nodeName)) {
						device.setIrSupported(xpp.nextText());
					} else if ("tv2padtype".equals(nodeName)) {
						device.setAppType(xpp.nextText());
					} else if ("pad2tvtype".equals(nodeName)) {
						device.setPad2tvtype(xpp.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if ("Device".equals(nodeName)) {
						list.add(device);
						device = null;
					}
					break;
				default:
					break;
				}

				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("End document");
		return list;
	}

	private static List<Map<String, String>> getSourceDespMap(String str) {
		String split_3 = ";";
		String split_4 = ":";
		String SOURCE_NAME = ComConstants.SOURCE_NAME;
		String SOURCE_VALUE = ComConstants.SOURCE_VALUE;
		ArrayList<Map<String, String>> sourceDesp = new ArrayList<Map<String, String>>();
		if (null == str || "".equals(str)) {
			return new ArrayList<Map<String, String>>();
		}
		try {
			String[] splitStrArray = str.split(split_3);
			HashMap<String, String> map = null;
			for (int i = 0; i < splitStrArray.length; i++) {
				String[] splitDetailSource = splitStrArray[i].split(split_4);
				map = new HashMap<String, String>();
				map.put(SOURCE_NAME, splitDetailSource[0]);
				map.put(SOURCE_VALUE, splitDetailSource[1]);
				sourceDesp.add(map);
				Log.i(ComConstants.SX_TAG, "********sourceName" + i
						+ "*********" + splitDetailSource[0]);
				Log.i(ComConstants.SX_TAG, "********sourceValue" + i
						+ "*********" + splitDetailSource[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return sourceDesp;
		}
		return sourceDesp;
	}

	public interface OnShareChangListener {
		void onSharePathChanger(String path);
	}

	public void registerShareChangeListener(OnShareChangListener l) {
		this.shareChangListener = l;
	}
	
	public void proptMsg(final Context context,String displayMsg) {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("请选择设备")
				.setMessage(displayMsg)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						context.startActivity(new Intent(context, DevicesActivity.class));
						dialog.dismiss();
					}
				}).create();
		dialog.show();
	}

}
