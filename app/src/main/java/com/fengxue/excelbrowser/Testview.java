package com.fengxue.excelbrowser;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class Testview extends AppCompatActivity {
    @Override//入口
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ui);
        ExcelView cell_view=findViewById(R.id.excel);
        String receivedValue = getIntent().getStringExtra("FILE_PATH");// 获取传递过来的String参数
        if (receivedValue != null) {
            logs("打开文件路径为",receivedValue);
            ExcelFileControl excelFileControl=new ExcelFileControl();
            excelFileControl.load_excel(receivedValue);
            cell_view.setdataControl(excelFileControl);
            //用双端队列，比如说顶部-1,底部+1比如LinkedList，然后视图统一使用坐标系绘制，左上角为起点
        }
    }

    //LOG静态方法
    public static void logs(String name, String string){
        Log.i("MYTAG",name+": "+string);
    }
    public static void logs(String name, List list){
        Log.i("MYTAG",name+": "+list.toString());
    }
    public static void logs(String name, int ints){
        Log.i("MYTAG",name+": "+ints);
    }
    public static void logs(String name, Object object){Log.i("MYTAG",name+": "+String.valueOf(object));}

}
