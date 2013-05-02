
package com.joy.cloudshare.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.joy.cloudshare.R;
import com.joy.cloudshare.utils.ComConstants;
import com.joy.cloudshare.utils.ComUtils;

public class LaunchActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_layout);
        if (!checkIgrsBase() ) {
            if (checkSDCard()) {
                installIgrs();
            }
            this.finish();
            return;
        }
            ComUtils.checkWIFI(this, this.getString(R.string.app_name));
        startActivity(new Intent(getApplicationContext(), MediaMainActivity.class));
    }

    private boolean checkIgrsBase() {
        if (!ComUtils.checkInstall(this, ComConstants.IGRS_BASE_PACKAGE)) {
            // installIgrs();
            return false;
        }
        return true;
    }


    private void installIgrs() {
        ComUtils.toast(this, R.string.tip_install_igrs);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File f = null;
        try {
            ComUtils.saveToSdCard(ComConstants.IGRS_BASE_NAME,
                    ComUtils.readFile(this, ComConstants.IGRS_BASE_NAME));
            f = new File(ComConstants.IGRS_BASE_PATH
                    + ComConstants.IGRS_BASE_NAME);
            Runtime.getRuntime().exec("chmod 777 " + f.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Uri uri = Uri.fromFile(f);
        intent.setDataAndType(uri, ComConstants.IGRS_FILE_TYPE_APK);
        this.startActivity(intent);
    }


    private boolean checkSDCard()
    {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(LaunchActivity.this, ComConstants.TIP_NO_SDCARD,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
