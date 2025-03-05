package com.fengxue.excelbrowser;
import static com.fengxue.excelbrowser.Testview.logs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.filepicker.model.DialogConfigs;

public class MainActivity extends AppCompatActivity {
    @Override//入口
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Permission permission = new Permission();
        permission.checkPermissions(this);
        setContentView(R.layout.main_ui);
        Button button1=findViewById(R.id.open_xlsx);
        Button button2=findViewById(R.id.test_button);
        button1.setOnClickListener(view -> {
            if (permission.allfilepermission(this)){
                ExcelFilePick excelFilePick = new ExcelFilePick(this, DialogConfigs.SINGLE_MODE, DialogConfigs.FILE_SELECT, new String[]{"xlsx", "xls"}, true, (files) -> {
                    if (files.length == 1) {startActivityTakeString(this, Testview.class, "FILE_PATH", files[0]);}
                });
                excelFilePick.show();
            }else {permission.show_dialog(this,"若要使用导入导出功能，请手动授予所有文件的管理权限！",Toast.LENGTH_LONG);}
        });
        button2.setOnClickListener(view -> {startActivityTakeString(this, Testview.class,"FILE_PATH",null);});
    }
    //启动Active
    public static void startActivityTakeString(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);}
    //带参数启动Active
    public static void startActivityTakeString(Context context, Class activity, String key, String value) {
        Intent intent = new Intent(context, activity);
        intent.putExtra(key, value); // 添加String参数
        context.startActivity(intent);
    }
    //显示提示框
    public static void show_tip(String text,Context context) {
        AlertDialog.Builder msgbox = new AlertDialog.Builder(context);
        msgbox.setTitle("提示");
        msgbox.setMessage(text);
        msgbox.setPositiveButton("知悉", null);
        msgbox.show();}
}
