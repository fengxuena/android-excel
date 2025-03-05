package com.fengxue.excelbrowser;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.LinkedList;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        System.out.printf("结果为:"+columnIndexToExcelColumn(0));
        System.out.printf("结果为:"+columnIndexToExcelColumn(1));
        System.out.printf("结果为:"+columnIndexToExcelColumn(2));
    }

    // 辅助方法：将列索引转换为Excel格式的列名
    public static String columnIndexToExcelColumn(int columnIndex) {
        if (columnIndex < 0) return "";  // 新增边界保护
        StringBuilder column = new StringBuilder();
        while (columnIndex >= 0) {
            int remainder = columnIndex % 26;
            column.insert(0, (char) ('A' + remainder));
            columnIndex = (columnIndex / 26) - 1;
        }
        return column.toString();
    }

}