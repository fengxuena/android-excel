package com.fengxue.excelbrowser;
import static com.fengxue.excelbrowser.Testview.logs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelFileControl {

	private String filepath;
	private InputStream inputStream;
	private OutputStream outputStream;
	private XSSFWorkbook workbook;
	//加载
	public void load_excel(String filepath){
		this.filepath=filepath;
		try {
			inputStream = new FileInputStream(new File(filepath));
			workbook = new XSSFWorkbook(inputStream);
			inputStream.close();
		} catch (IOException e) {throw new RuntimeException(e);}

	}
	//获取cell的值
	public String getcellvalue(int sheets,int row,int col){
		String data="nope";
		if (workbook!=null){
			XSSFSheet sheet = workbook.getSheetAt(sheets);
			int maxrow = sheet.getLastRowNum();
			data=getCellValue(sheet.getRow(row).getCell(col));}
		return data;
	}
	//获取所有sheet名称
	public LinkedList<String> get_all_sheet_name(){
		LinkedList<String> list=new LinkedList<String>();
		if (workbook!=null){
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				list.add(sheet.getSheetName());}}
		return list;
	}
	//获取wookbook对象
	public Workbook getworkbook(){
		return workbook;
	}
	//cell数据类型转换为string
	private static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell != null) {
			CellType typ = cell.getCellType();
			switch (cell.getCellType()) {
				case STRING:
					cellValue = cell.getStringCellValue();
					break;
				case NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						cellValue = sdf.format(date);
					} else {
						DecimalFormat df = new DecimalFormat("#");
						cellValue = df.format(cell.getNumericCellValue());
					}
					break;
				case BOOLEAN:
					cellValue = String.valueOf(cell.getBooleanCellValue());
					break;
				case FORMULA:
					cellValue = cell.getCellFormula();
					break;
				case BLANK:
					cellValue = "";
					break;
				default:
					cellValue = "";
			}
		}
		return cellValue;
	}
	//获取指定行的数据
	public LinkedList<LinkedList<Cellitem>> get_defult_data(int sheetindex,int endrow,int endcol) {
		LinkedList<LinkedList<Cellitem>> list = new LinkedList<>();
		if (workbook != null) {
			Sheet sheet = workbook.getSheetAt(sheetindex);
			for (int a=0;a<=endrow;a++){
				Row rowdata = sheet.getRow(a);
				//short cols=rs.getLastCellNum();
				LinkedList<Cellitem> cellItems = new LinkedList<>();
				for (int b=0
					 ;b<=endcol;b++){
					Cell cell=rowdata.getCell(b);
					Cellitem cellItem;
					int[] id=new int[]{a+1,b+1};
					String formula="";
					try {formula=cell.getCellFormula();}catch (Exception e){e.printStackTrace();}
					cellItem=new Cellitem(id,getCellValue(cell),formula,0,0,false);
					//logs("test","id:R"+cellItem.idlist[0]+"C"+cellItem.idlist[1]+" text"+cellItem.text);
					cellItems.add(cellItem);}
				list.add(cellItems);}}
		return list;
	}
	//获取指定row数据
	public LinkedList<Cellitem> get_row_data(int sheetindex,int row,int star_col,int end_col){
		LinkedList<Cellitem> list = new LinkedList<>();
		if (workbook != null && row>=1 &&star_col>=1 && end_col>=1) {
			Sheet sheet = workbook.getSheetAt(sheetindex);
			Row rs =sheet.getRow(row-1);
			for (int b=star_col-1;b<=end_col-1;b++){
				Cell cl=rs.getCell(b);
				int[] id=new int[]{row,b+1};
				String formula="";
				try {formula=cl.getCellFormula();}catch (Exception e){e.printStackTrace();}
				Cellitem cellitem=new Cellitem(id,getCellValue(cl),formula,0,0,false);
				logs("test","id:R"+cellitem.idlist[0]+"C"+cellitem.idlist[1]+" text"+cellitem.text);
				list.add(cellitem);
			}
		}
		return list;
	}
	//获取指定col数据
	public LinkedList<Cellitem> get_col_data(int sheetindex,int col,int star_row,int end_row){
		LinkedList<Cellitem> list = new LinkedList<>();
		if (workbook != null && col>=1 &&star_row>=1 && end_row>=1) {
			Sheet sheet = workbook.getSheetAt(sheetindex);
			for (int a=star_row-1;a<=end_row-1;a++){
				Cell cl=sheet.getRow(a).getCell(col-1);
				int[] id=new int[]{a+1,col};
				String formula="";
				try {formula=cl.getCellFormula();}catch (Exception e){e.printStackTrace();}
				Cellitem cellitem=new Cellitem(id,getCellValue(cl),formula,0,0,false);
				logs("test","id:R"+cellitem.idlist[0]+"C"+cellitem.idlist[1]+" text"+cellitem.text);
				list.add(cellitem);
			}
		}
		return list;
	}
	//关闭流
	public void close(){
		if (inputStream != null) {
			try {inputStream.close();} catch (IOException e) {e.printStackTrace();}}
		if (outputStream != null) {
			try {outputStream.close();} catch (IOException e) {e.printStackTrace();}}
		if (workbook!=null){
			try {workbook.close();} catch (IOException e) {e.printStackTrace();}}
	}
}



