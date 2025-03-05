package com.fengxue.excelbrowser;
import static com.fengxue.excelbrowser.Testview.logs;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import androidx.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExcelView extends View {
    public Paint BackgroundPaint;
    public TextPaint TextPaint;
    public Paint LinePaint;
    public int textsize;//字体大小
    public ExcelView2.TextAlign textAlign;//对齐方式
    public Context content;//当前视图对象
    private int cell_starx=180;//数据开始绘制点
    private int cell_stary=70;//数据开始绘制点
    private float scrollX = 0f; // 水平滚动位置
    private float scrollY = 0f; // 垂直滚动位置
    private float lastTouchX; // 上一次触摸的位置
    private float lastTouchY; // 上一次触摸的位置
    private Scroller scroller;
    public Indicator indicator;//行号列号指示器+全选按钮
    private ExcelFileControl dataControl;
    public LinkedList<LinkedList<Cellitem>> Celllist;//数据
    public int[] DataVisibleRange;
    public float cell_pianyi_x;
    public float cell_pianyi_y;
    public int screen_width;
    public int screen_heigt;
    public boolean first=true;
    public boolean data_load_lock =true;
    public int sheetindex=0;
    public enum TextAlign {
        TOP_LEFT, TOP_CENTER, TOP_RIGHT,
        MIDDLE_LEFT, MIDDLE_CENTER, MIDDLE_RIGHT,
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
    }// 对齐方式枚举
    //构造器
    public ExcelView(Context context) {super(context);init();}
    public ExcelView(Context context, @Nullable AttributeSet attrs) {super(context, attrs);init();}
    public ExcelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {super(context, attrs, defStyleAttr);init();}
    public ExcelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {super(context, attrs, defStyleAttr, defStyleRes);init();}
    //初始化
    public void init(){
        //初始化视图和对齐方式,字体大小
        content=getContext();
        textAlign= ExcelView2.TextAlign.MIDDLE_CENTER;
        textsize=18;
        scroller = new Scroller(content);
        screen_width=getWidth();
        screen_heigt=getHeight();
        logs("screen_width",screen_width);
        logs("screen_heigt",screen_heigt);
        // 初始化默认画笔
        BackgroundPaint = new Paint();
        BackgroundPaint.setColor(Color.WHITE);
        TextPaint = new TextPaint();
        TextPaint.setTextSize(18);
        TextPaint.setColor(Color.BLACK);
        LinePaint = new Paint();
        LinePaint.setColor(Color.BLACK);
        LinePaint.setStrokeWidth(2); //线条宽度
    }
    @Override//大小变化
    protected void onSizeChanged(int width, int heigt, int oldwidth, int oldheigt) {
        super.onSizeChanged(width,heigt,oldwidth,oldheigt);
        cell_starx=width/6;
        cell_stary=heigt/28;
        screen_width=width;
        screen_heigt=heigt;
        indicator=new Indicator();
        indicator.set_default_size(cell_starx,cell_stary);
        indicator.set_screen_size();
        logs("scren",width+"--"+heigt);
    }
    @Override//绘制
    protected void onDraw(Canvas canvas){
        //绘制cell
        float pianyi_x=getDecimalAsPercentage(cell_pianyi_x);
        float pianyi_y=getDecimalAsPercentage(cell_pianyi_y);
        int starx=1;
        int stary=1;
        if (this.Celllist!=null && !this.Celllist.isEmpty() && this.dataControl!=null){
            for (LinkedList<Cellitem> list : this.Celllist) {
                for (Cellitem cell : list) {
                    Rect rect=new Rect(
                            cell_starx * starx - (int)(pianyi_x*cell_starx),
                            cell_stary * stary - (int)(pianyi_y*cell_stary),
                            cell_starx + cell_starx * starx - (int)(pianyi_x*cell_starx),
                            cell_stary + cell_stary * stary - (int)(pianyi_y*cell_stary));
                    //logs("cell","id=R"+cell.idlist[0]+"C"+cell.idlist[1]+" rect=<left "+rect.left+", top "+rect.top+" ,right "+rect.right+" , bottom"+rect.bottom);
                    drawbackground(canvas, cell, rect);
                    drawGridLines(canvas, rect,LinePaint);
                    if (cell.text != null && !cell.text.isEmpty()) {drawText(canvas, cell, rect);}
                    starx+=1;}
                starx=1;
                stary+=1;}}
        //绘制指示器
        indicator.canvas_indicator(canvas);
        if (first==true  && this.dataControl!=null ){
            List<String> list=this.dataControl.get_all_sheet_name();
            logs("sheet标签列表",list.toString());
            int p=excelColumnToIndex(indicator.Row_list.get(indicator.Row_list.size()-1));
            int m=Integer.valueOf(indicator.Col_list.get(indicator.Col_list.size()-1));
            Celllist=this.dataControl.get_defult_data(0,m,p);
            first=false;
            invalidate();}
    }
    @Override//滚动
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                lastTouchX = currentX;
                lastTouchY = currentY;
                break;
            case MotionEvent.ACTION_MOVE:
                final float dx = lastTouchX - currentX;
                final float dy = lastTouchY - currentY;
                scrollBy(dx, dy);
                lastTouchX = currentX;
                lastTouchY = currentY;
                break;
        }
        return true; // 确保返回 true 以接收后续事件
    }
    //滚动
    private void scrollBy(float dx, float dy) {
        // 更新滚动位置
        scrollX += dx;
        scrollY += dy;
        // 确保不会滚动到负方向
        scrollX = Math.max(0, scrollX);
        scrollY = Math.max(0, scrollY);
        // 请求重绘
        indicator.scroll_indicator(scrollX,scrollY);
        if (Celllist!=null && !Celllist.isEmpty()){updata_data();}
        calculate_xy();//计算两个控件的偏移值
        invalidate();
    }
    //移动更新数据
    public void updata_data(){
        int q=excelColumnToIndex(indicator.Row_list.get(0));
        int z=Integer.valueOf(indicator.Col_list.get(0));
        int p=excelColumnToIndex(indicator.Row_list.get(indicator.Row_list.size()-1));
        int m=Integer.valueOf(indicator.Col_list.get(indicator.Col_list.size()-1));
        int cellq=Celllist.getFirst().getFirst().idlist[1];
        int cellz=Celllist.getFirst().getFirst().idlist[0];
        logs("indi-qpzm","q"+q+"p"+p+"z"+z+"m"+m);
        logs("cell-qpzm","q"+cellq+"z"+cellz);
        if (q!=cellq|z!=cellz){data_load_lock=true;}//当数据和视图不一致时，调整锁
        if (data_load_lock){
            //加载数据
			
			
			
            data_load_lock =false;
        }
    }

    //计算偏移
    public void calculate_xy(){
        this.cell_pianyi_x =roundToTwoDecimals(scrollX/cell_starx)>=1 ? roundToTwoDecimals(scrollX/cell_starx):1;
        this.cell_pianyi_y =roundToTwoDecimals(scrollY/cell_stary)>=1 ? roundToTwoDecimals(scrollY/cell_stary) :1;
    }
    //绑定数据类
    public void setdataControl(ExcelFileControl excelFileControl){this.dataControl=excelFileControl;}
    //绘制背景
    private void drawbackground(Canvas canvas,Cellitem cell,Rect bounds) {
        // 绘制背景
        BackgroundPaint.setColor(cell.background);
        canvas.drawRect(bounds, BackgroundPaint);
    }
    //绘制背景
    public static void drawbackground(Canvas canvas, Rect bounds,Paint backgroundPaint){
        canvas.drawRect(bounds, backgroundPaint);
    }
    //绘制线条
    public static void drawGridLines(Canvas canvas, Rect bounds,Paint linePaint) {
        canvas.drawLine(bounds.left, bounds.top, bounds.right, bounds.top, linePaint); // 上边线
        canvas.drawLine(bounds.left, bounds.bottom, bounds.right, bounds.bottom, linePaint); // 下边线
        canvas.drawLine(bounds.left, bounds.top, bounds.left, bounds.bottom, linePaint); // 左边线
        canvas.drawLine(bounds.right, bounds.top, bounds.right, bounds.bottom, linePaint); // 右边线
    }
    //绘制文本
    public void drawText( Canvas canvas, String text, Rect bounds,TextPaint textPaint) {
        int padding = 4; // 上下左右padding都是4
        int availableWidth = bounds.width() - 2 * padding;
        int availableHeight = bounds.height() - 2 * padding;
        // 计算单行文本的高度
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float lineHeight = fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading;
        // 计算最大行数
        int maxLines = Math.max(1, (int) (availableHeight / lineHeight)); // 确保至少显示一行
        // 将文本按行分割，并确保每行不超过可用宽度
        List<String> lines = splitTextIntoLinesByWidth(text, textPaint, availableWidth);

        // 如果行数超出限制，截断并添加省略号
        if (lines.size() > maxLines) {
            lines = lines.subList(0, maxLines - 1);
            lines.add("...");}
        // 计算文本起点坐标
        float xStart = bounds.left + padding;
        float yStart = getTextStartY(lineHeight, lines.size(), bounds, textAlign, padding);
        // 绘制每一行文本
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            float x = calculateXPosition(xStart, availableWidth, line, textPaint, textAlign);
            float y = yStart + i * lineHeight;
            canvas.drawText(line, x, y, textPaint);}
    }
    //绘制文本
    public void drawText( Canvas canvas, Cellitem cell, Rect bounds) {
        // 配置TextPaint（假设这些设置不经常变化，因此可以缓存）
        if (TextPaint.getTextSize() != textsize || TextPaint.getTypeface() != (cell.isbold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT) || TextPaint.getColor() != cell.textcolor) {
            TextPaint.setTextSize(textsize);
            TextPaint.setTypeface(cell.isbold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            TextPaint.setColor(cell.textcolor);}
        int padding = 4; // 上下左右padding都是4
        int availableWidth = bounds.width() - 2 * padding;
        int availableHeight = bounds.height() - 2 * padding;
        // 计算单行文本的高度
        Paint.FontMetrics fontMetrics = TextPaint.getFontMetrics();
        float lineHeight = fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading;
        // 计算最大行数
        int maxLines = Math.max(1, (int) (availableHeight / lineHeight)); // 确保至少显示一行
        // 将文本按行分割，并确保每行不超过可用宽度
        List<String> lines = splitTextIntoLinesByWidth(cell.text, TextPaint, availableWidth);

        // 如果行数超出限制，截断并添加省略号
        if (lines.size() > maxLines) {
            lines = lines.subList(0, maxLines - 1);
            lines.add("...");}
        // 计算文本起点坐标
        float xStart = bounds.left + padding;
        float yStart = getTextStartY(lineHeight, lines.size(), bounds, textAlign, padding);
        // 绘制每一行文本
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            float x = calculateXPosition(xStart, availableWidth, line, TextPaint, textAlign);
            float y = yStart + i * lineHeight;
            canvas.drawText(line, x, y, TextPaint);}
    }
    //文本起点计算方法
    public static  List<String> splitTextIntoLinesByWidth(String text, TextPaint textPaint, int availableWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (int i = 0; i < text.length(); ) {
            // 构建当前行直到超过可用宽度
            while (i < text.length()) {
                char c = text.charAt(i);
                String nextChar = Character.toString(c);
                float nextLineWidth = textPaint.measureText((currentLine + nextChar).toString());

                if (nextLineWidth > availableWidth) {
                    break;
                }
                currentLine.append(nextChar);
                i++;
            }

            // 添加当前行到列表中
            if (currentLine.length() > 0) {
                lines.add(currentLine.toString().trim());
                currentLine.setLength(0); // 清空StringBuilder为下一行做准备
            }

            // 如果已经到达文本末尾，则退出循环
            if (i >= text.length()) {
                break;
            }

            // 处理剩下的字符，避免无限循环
            if (textPaint.measureText(Character.toString(text.charAt(i))) > availableWidth) {
                // 如果单个字符超出了可用宽度，则将其单独作为一行
                lines.add(Character.toString(text.charAt(i)));
                i++;
            }
        }

        return lines;
    }
    public static  float calculateXPosition(float xStart, int availableWidth, String line, TextPaint textPaint, ExcelView2.TextAlign textAlign) {
        float lineWidth = textPaint.measureText(line);
        switch (textAlign) {
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                return xStart + (availableWidth - lineWidth) / 2;
            case TOP_RIGHT:
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:
                return xStart + availableWidth - lineWidth;
            default:
                return xStart;
        }
    }
    public static  float getTextStartY(float lineHeight, int lineCount, Rect bounds, ExcelView2.TextAlign textAlign, int padding) {
        float totalTextHeight = lineHeight * lineCount;
        switch (textAlign) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                return bounds.top + padding + lineHeight; // 顶部对齐，加上上边距和第一行的行高
            case MIDDLE_LEFT:
            case MIDDLE_CENTER:
            case MIDDLE_RIGHT:
                return Math.max(bounds.centerY() - totalTextHeight / 2 + lineHeight / 2, bounds.top + padding + lineHeight); // 居中对齐但不超出顶部边距
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                return Math.min(bounds.bottom - totalTextHeight - padding + lineHeight, bounds.bottom - padding); // 底部对齐但不超出底部边距
            default: // 默认居中对齐
                return Math.max(bounds.centerY() - totalTextHeight / 2 + lineHeight / 2, bounds.top + padding + lineHeight);
        }
    }
    //取2位小数
    public static float roundToTwoDecimals(float value) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
    //只取小数部分
    public static  float getDecimalAsPercentage(float number) {
        // 获取小数部分
        float decimalPart = number - (int)number;
        // 转换为百分比形式
        return decimalPart;
    }
    // 生成纵向指示器 (1, 2, 3, ...)
    public static  List<String> generateVerticalIndicators(int start, int maxRows) {
        // 检测start是否大于0
        if (start < 0) {
            throw new IllegalArgumentException("Start value must be greater than 0.");
        }

        List<String> indicators = new ArrayList<>();
        // 从start开始到maxRows结束
        for (int i = start; i <= maxRows; i++) {
            indicators.add(String.valueOf(i));
        }
        return indicators;
    }
    // 生成横向指示器（支持自定义起始位置）
    public static  List<String> generateHorizontalIndicators(int start, int maxColumns) {
        // 检测start是否大于0
        if (start < 0) {
            throw new IllegalArgumentException("Start value must be greater than 0.");
        }
        List<String> indicators = new ArrayList<>();
        for (int i = start; i < start + maxColumns; i++) {
            indicators.add(columnIndexToExcelColumn(i));
        }
        return indicators;
    }
    // 辅助方法：将列索引转换为Excel格式的列名
    public static  String columnIndexToExcelColumn(int columnIndex) {
        if (columnIndex <=0) { // 检查输入是否大于0
            throw new IllegalArgumentException("Column index must be greater than 0.");
        }

        StringBuilder column = new StringBuilder();
        while (--columnIndex >= 0) { // 首先将columnIndex减1以适应从1开始的输入
            column.append((char) ('A' + (columnIndex % 26))); // 使用余数计算当前位的字母
            columnIndex /= 26; // 准备计算下一位
        }

        return column.reverse().toString(); // 最后需要反转字符串，因为我们是从低位到高位构建的
    }
    // 辅助方法反向：将Excel格式的列名转换回对应的整数索引
    public static int excelColumnToIndex(String column) {
        int index = 0;
        column = column.toUpperCase(); // 确保输入为大写
        for (int i = 0; i < column.length(); i++) {
            char c = column.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                index = index * 26 + (c - 'A' + 1); // 调整索引计算以适应1为基础的索引
            } else {
                throw new IllegalArgumentException("Invalid character in column name: " + c);
            }
        }
        return index;
    }
    //行号列号指示器
    public class Indicator {
        public int width;
        public int heigt;
        public float scroll_X_int =1;
        public float scroll_Y_int =1;
        public int Col_visible_range;
        public int Row_visible_range;
        public List<String> Row_list;
        public List<String> Col_list;
        //初始化表格大小
        public void set_default_size(int width,int heigt) {
            this.width=width;this.heigt =heigt;}
        //设置绘制区域
        public void set_screen_size() {
            this.Row_visible_range=(screen_width/width)+4;
            this.Col_visible_range=(screen_heigt/heigt)+4;}
        //滚动
        public void scroll_indicator(float x,float y){
            this.scroll_X_int =roundToTwoDecimals(x/width)>=1 ? roundToTwoDecimals(x/width) :1;
            this.scroll_Y_int =roundToTwoDecimals(y/heigt)>=1 ? roundToTwoDecimals(y/heigt) :1;}
        //绘制
        public void canvas_indicator(Canvas canvas){
            // 初始化默认画笔
            Paint BackgroundPaint = new Paint();
            BackgroundPaint.setColor(Color.rgb(216,216,216));
            TextPaint TextPaint = new TextPaint();
            TextPaint.setTextSize(18);
            TextPaint.setColor(Color.BLACK);
            Paint LinePaint = new Paint();
            LinePaint.setColor(Color.BLACK);
            LinePaint.setStrokeWidth(2);
            //计算
            Row_list=generateHorizontalIndicators((int)this.scroll_X_int,(int)(this.scroll_X_int +this.Row_visible_range));
            Col_list=generateVerticalIndicators((int)this.scroll_Y_int,(int)(this.scroll_Y_int +this.Col_visible_range));
            //logs("nub_row",(this.scroll_X_int +this.Row_visible_range));
            //logs("nub_col",(this.scroll_Y_int +this.Col_visible_range));
            //logs("Col_list",Col_list.toString());
            //logs("Row_list",Row_list.toString());
            //绘制背景
            Rect row_rect=new Rect(width,0,screen_width,heigt);
            Rect col_rect=new Rect(0,heigt,width,screen_heigt);
            drawbackground(canvas,row_rect,BackgroundPaint);
            drawbackground(canvas,col_rect,BackgroundPaint);
            float pianyi_x=getDecimalAsPercentage(scroll_X_int);
            float pianyi_y=getDecimalAsPercentage(scroll_Y_int);
            //绘制框框和文本
            int starr=1;
            for (String string:Row_list){
                Rect row_cell=new Rect(width*starr-(int)(pianyi_x*width),0,width*starr+width-(int)(pianyi_x*width),heigt);
                starr+=1;
                drawGridLines(canvas,row_cell,LinePaint);
                drawText(canvas,string,row_cell,TextPaint);
            }
            int starc=1;
            for (String string:Col_list){
                Rect col_cell=new Rect(0,heigt*starc-(int)(pianyi_y*heigt),width,heigt*starc+heigt-(int)(pianyi_y*heigt));
                starc+=1;
                drawGridLines(canvas,col_cell,LinePaint);
                drawText(canvas,string,col_cell,TextPaint);
            }
            //绘制全选按钮
            drawbackground(canvas,new Rect(0,0,width,heigt),BackgroundPaint);
            drawGridLines(canvas,new Rect(0,0,width,heigt),LinePaint);
            drawText(canvas,"All",new Rect(0,0,width,heigt),TextPaint);
        }
    }
}
