package com.fengxue.excelbrowser;

public class beifen {
    /*
    //移动更新数据
    public boolean updata_data(){
        //比较视图和数据的边界偏移
        int cell_left=Celllist.getFirst().getFirst().idlist[1];
        int cell_right=Celllist.getFirst().getLast().idlist[1];
        int cell_top=Celllist.getFirst().getFirst().idlist[0];
        int cell_bottom=Celllist.getLast().getFirst().idlist[0];
        int view_left=excelColumnToIndex(indicator.Row_list.get(0));
        int view_right=excelColumnToIndex(indicator.Row_list.get(indicator.Row_list.size()-1));
        int view_top=Integer.valueOf(indicator.Col_list.get(0));
        int view_bottom=Integer.valueOf(indicator.Col_list.get(indicator.Col_list.size()-1));
        logs("cell:"," cell_left:"+cell_left+" cell_right:"+cell_right+" cell_top:"+cell_top+" cell_bottom:"+cell_bottom);
        logs("view:"," view_left:"+view_left+" view_right:"+view_right+" view_top:"+view_top+" view_bottom:"+view_bottom);
        //偏移行数
        int offst_left=cell_left-view_left;
        int offst_right=view_right-cell_right;
        int offst_top=cell_top-view_top;
        int offst_bottom=view_bottom-cell_bottom;
        //左加载
        if (offst_left > 0) {
            if(cell_left>1){
                logs("加载数据","offst_left:"+offst_left);
                if (offst_left==1){
                    LinkedList<Cellitem> data=dataControl.get_col_data(sheetindex,cell_left-offst_left,cell_top,cell_bottom);
                    int d=0;
                    for (LinkedList<Cellitem> list:Celllist){list.removeLast();list.addFirst(data.get(d));d+=1;}
                } else if (offst_left>1) {
                    for (int r=1;r<=offst_left;r++){
                        LinkedList<Cellitem> data=dataControl.get_col_data(sheetindex,cell_left-r,cell_top,cell_bottom);
                        int d=0;
                        for (LinkedList<Cellitem> list:Celllist){list.removeLast();list.addFirst(data.get(d));d+=1;}}}return true;
            }}
        //右加载
        if (offst_right > 0) {
            logs("加载数据","offst_right:"+offst_right);
            if (offst_right==1){
                LinkedList<Cellitem> data=dataControl.get_col_data(sheetindex,cell_right+offst_right,cell_top,cell_bottom);
                int d=0;
                for (LinkedList<Cellitem> list:Celllist){list.removeFirst();list.addLast(data.get(d));d+=1;}
            } else if (offst_right>1) {
                for (int r=1;r<=offst_right;r++){
                    LinkedList<Cellitem> data=dataControl.get_col_data(sheetindex,cell_right+r,cell_top,cell_bottom);
                    int d=0;
                    for (LinkedList<Cellitem> list:Celllist){list.removeFirst();list.addLast(data.get(d));d+=1;}}}return true;}
        //上加载
        if (offst_top > 0) {
            if (cell_top>1){
                logs("加载数据","offst_top:"+offst_top);
                if (offst_top==1){
                    LinkedList<Cellitem> data=dataControl.get_row_data(sheetindex,cell_top-offst_top,cell_left,cell_right);
                    Celllist.removeLast();Celllist.addFirst(data);
                } else if (offst_top>1) {
                    for (int r=1;r<=offst_top;r++){
                        LinkedList<Cellitem> data=dataControl.get_row_data(sheetindex,cell_top-r,cell_left,cell_right);
                        Celllist.removeLast();Celllist.addFirst(data);}}return true;}}
        //下加载
        if (offst_bottom > 0) {
            logs("加载数据","offst_bottom:"+offst_bottom);
            if (offst_bottom==1){
                LinkedList<Cellitem> data=dataControl.get_row_data(sheetindex,cell_bottom+offst_bottom,cell_left,cell_right);
                Celllist.removeFirst();Celllist.addLast(data);
            } else if (offst_bottom>1) {
                for (int r=1;r<=offst_bottom;r++){
                    LinkedList<Cellitem> data=dataControl.get_row_data(sheetindex,cell_bottom+r,cell_left,cell_right);
                    Celllist.removeFirst();Celllist.addLast(data);}}return true;}
        return false;

    }
     */
}
