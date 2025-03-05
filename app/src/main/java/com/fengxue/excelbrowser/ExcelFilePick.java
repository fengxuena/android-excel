package com.fengxue.excelbrowser;


import static com.fengxue.excelbrowser.Testview.logs;

import android.content.Context;
import android.os.Environment;

import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;

import java.io.File;
import java.util.Arrays;


public class ExcelFilePick {
    // 定义内部接口 OnFileSelectedListener
    @FunctionalInterface
    public interface OnFileSelectedListener { void onFileSelected(String[] files);}
    protected Context context;
    protected DialogProperties properties;
    private OnFileSelectedListener listener;

    // 构造函数中直接传入选项参数和监听器
    public ExcelFilePick(Context context, int selectionMode, int selectionType, String[] extensions, boolean showHiddenFiles, OnFileSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        this.properties = new DialogProperties();
        properties.selection_mode = selectionMode; // 选择模式
        properties.selection_type = selectionType; // 文件或目录类型
        properties.root = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString());
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = extensions; // 设置文件扩展名过滤器
        properties.show_hidden_files = showHiddenFiles; // 是否显示隐藏文件

    }

    // 显示文件选择对话框
    public void show() {
        FilePickerDialog dialog = new FilePickerDialog(context, properties);
        dialog.setTitle(getDialogTitle());
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if (listener != null) {
                    listener.onFileSelected(files);
                }
            }
        });
        dialog.show();
    }
    //构建标题
    private String getDialogTitle() {
        StringBuilder titleBuilder = new StringBuilder("请选择");

        // 根据选择模式添加文本
        switch (properties.selection_mode) {
            case DialogConfigs.SINGLE_MODE:
                titleBuilder.append("单个");
                break;
            case DialogConfigs.MULTI_MODE:
                titleBuilder.append("多个");
                break;
            default:break;
        }

        // 根据选择类型和扩展名添加文本
        switch (properties.selection_type) {
            case DialogConfigs.FILE_SELECT:
                if (properties.extensions != null && properties.extensions.length > 0) {
                    titleBuilder.append("(");
                    int maxExtensionsToShow = 3;
                    for (int i = 0; i < properties.extensions.length; i++) {
                        if (i >= maxExtensionsToShow) {
                            titleBuilder.append("...");
                            break; // 超过最大显示数量后退出循环
                        }
                        if (i > 0) {
                            titleBuilder.append(","); // 使用中文顿号分隔
                        }
                        titleBuilder.append(properties.extensions[i]);
                    }
                    titleBuilder.append(")");
                }
                titleBuilder.append("文件:");
                break;
            case DialogConfigs.DIR_SELECT:
                titleBuilder.append("目录:");
                break;
            case DialogConfigs.FILE_AND_DIR_SELECT:
                titleBuilder.append("文件或目录:");
                break;
            default:break;
        }

        return titleBuilder.toString();
    }
}