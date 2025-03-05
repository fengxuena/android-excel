package com.fengxue.excelbrowser;
import android.graphics.Color;
import android.graphics.Rect;

public class Cellitem {
    public int[] idlist;//row第几列,col第几行
    public String text;
    public String formula;
    public int textcolor;
    public int background;
    public boolean isbold;

    public Cellitem(int[] idlist,String text,String formula,int textcolor,int background,boolean isbold){
        if (idlist.length!=2){throw new RuntimeException("ExcelView-ID list must contain exactly two elements.");}
        this.idlist=idlist;
        this.text=text;
        this.formula=formula;
        this.textcolor=(textcolor==0) ? Color.BLACK:textcolor;
        this.background=(background==0) ? Color.WHITE:background;
        this.isbold=isbold;
    }

}
