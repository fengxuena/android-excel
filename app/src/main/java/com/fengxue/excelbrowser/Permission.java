package com.fengxue.excelbrowser;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class Permission {
    // 需要申请权限的数组
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA};
    // 保存真正需要去申请的权限
    private List<String> permissionList = new ArrayList<>();
    public static int RequestCode = 100;

    //检查并请求普通权限
    public void checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permission);
                }
            }
            // 对于其他非MANAGE_EXTERNAL_STORAGE权限，如果有未授予的权限则请求权限
            if (!permissionList.isEmpty()) {
                requestPermission(activity);
            }
        }
    }

    //请求普通权限
    public void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[0]), RequestCode);}

    //检查是否开启了 MANAGE_EXTERNAL_STORAGE 权限
    public boolean allfilepermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true;
            } else {
                // 权限未被授予，需要申请
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                activity.startActivity(intent);
                return false;
            }
        } else {
            // Android 10及以下版本不需要此权限
            return true;
        }
    }

    //提示授予权限
    public void show_dialog(Context context,String string,int showtime){
        //Toast.makeText(MainActivity.this,"若要使用导入导出功能，请授予所有文件管理权限!",Toast.LENGTH_LONG).show();
        Toast.makeText(context,string,showtime).show();
    }
}