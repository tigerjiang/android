package com.joy.cloudshare.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joy.cloudshare.R;
import com.joy.cloudshare.adapter.ShareDirAdapter;
import com.joy.cloudshare.utils.ComUtils;

public class ShareActivity extends Activity implements OnItemClickListener,
		OnItemSelectedListener, OnClickListener {
	private TextView mShareDirTxt;
	private Button mBackButton;
	private ListView mFileListView;
	private ShareDirAdapter mDirAdapter;
	private List<File> mFileList;
	private File mFile;
	private Context mContext;
	private String mRootPath;
	private boolean isRootPath;
	private View mHeadView;
	private String mCuttentPath;
	private Button mSubmitBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		mCuttentPath = mRootPath;
		mContext = getApplicationContext();
		initView();
		refreshData(mRootPath);
	}

	private void refreshData(String path) {
		ShareDirAdapter tempAdapter;
		if (TextUtils.isEmpty(path)) {
			return;
		} else {
			if (path.equals(mRootPath)) {
				isRootPath = true;
			} else {
				isRootPath = false;
			}
			mCuttentPath = path;
			mShareDirTxt.setText(path);
			mFileList = getFile(path);
			Collections.sort(mFileList, new Comparator<File>() {

				public int compare(File f1, File f2) {
					return f1.getName().compareToIgnoreCase(f2.getName());
				}
			});
			Collections.sort(mFileList, new Comparator<File>() {

				public int compare(File f1, File f2) {
					int temp1 = f1.isDirectory() ? 1 : 0;
					int temp2 = f2.isDirectory() ? 1 : 0;
					return temp2 - temp1;
				}
			});
			mDirAdapter = new ShareDirAdapter(mContext, mFileList);
		}
		// mFileListView.removeHeaderView(mHeadView);
		// if(!isRootPath){
		// mFileListView.addHeaderView(mHeadView);
		// }
		mFileListView.setAdapter(mDirAdapter);
		// mDirAdapter = tempAdapter;
	}

	private void initView() {
		mShareDirTxt = (TextView) findViewById(R.id.share_dir);
		mBackButton = (Button) findViewById(R.id.set_title_back);
		mSubmitBtn = (Button) findViewById(R.id.set_title_submit);
		mFileListView = (ListView) findViewById(R.id.dir_list);
		mFileListView.setOnItemClickListener(this);
		mFileListView.setOnItemSelectedListener(this);
		mBackButton.setOnClickListener(this);
		mSubmitBtn.setOnClickListener(this);
		// mHeadView
		// =LayoutInflater.from(mContext).inflate(R.layout.dir_list_item, null);
		// mHeadView.findViewById(R.id.dir_check).setVisibility(View.INVISIBLE);
		// mHeadView.findViewById(R.id.arrows_img).setVisibility(View.INVISIBLE);
		// ((TextView)mHeadView.findViewById(R.id.dir_name)).setText("��һ��");
		// mHeadView.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// refreshData(mFile.getParent());
		// }
		// });

	}

	private List<File> getFile(String path) {
		List<File> fileList = new ArrayList<File>();
		File parentFile = new File(path);
		File[] files = parentFile.listFiles(new FileFilter() {

			public boolean accept(File file) {

				return !file.isHidden();
			}
		});
		Collections.addAll(fileList, files);
		return fileList;
	}

	private String getRootPath() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getPath();
		} else {
			Toast.makeText(mContext, "No Sdcard", Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	public void onItemClick(AdapterView<?> viewGroup, View view, int position,
			long id) {
		int index = (int) id;
		mFile = mFileList.get(index);
		if (!mFile.isDirectory()) {
			return;
		}
		// if (!isRootPath && index == 0) {
		// refreshData(mFile.getParentFile().getParent());
		// } else {
		mCuttentPath = mFile.getPath();
		refreshData(mCuttentPath);
		// }
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.dir_check);
		checkbox.toggle();
		mDirAdapter.setSelectItem(index);
		mDirAdapter.notifyDataSetChanged();
	}

	public void onClick(View v) {
		if (v.getId() == R.id.set_title_back) {
			this.finish();
		}else if(v.getId() == R.id.set_title_submit){
			ComUtils.getInstance().setSharePath(mContext,mCuttentPath);
		}
	}

	public void onItemSelected(AdapterView<?> viewGroup, View view,
			int position, long id) {

	}

	public void onNothingSelected(AdapterView<?> viewGroup) {
		// TODO Auto-generated method stub

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isRootPath) {
				File neeFile = new File(mCuttentPath);
				refreshData(neeFile.getParent());
			} else {
				this.finish();
			}
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
