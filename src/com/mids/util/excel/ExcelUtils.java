package com.mids.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/**
 * 封装对excel的操作，包括本地读写excel和流中输出excel<br/>
 * 关联jar poi-3.5-beta5-20090219.jar<br/>
 * 有参构造函数参数为excel的全路径<br/>
 * @author 沙琪玛
 *
 */
public class ExcelUtils {

    // excel文件路径
    private String path = "";

    /**
     * 无参构造函数 默认
     */
    public ExcelUtils() {

    }

    /**
     * 有参构造函数
     * @param path excel路径
     */
    public ExcelUtils(String path) {
        this.path = path;
    }

    /**
     * 在磁盘生成一个含有内容的excel,路径为path属性
     * @param sheetName 导出的sheet名称
     * @param fieldName 列名数组
     * @param data 数据组
     * @throws IOException 
     */
    public void makeExcel(String sheetName, String[] fieldName, List<String[]> data) throws IOException {
        //在内存中生成工作薄
        Workbook workbook = makeWorkBook(sheetName, fieldName, data);
        //截取文件夹路径
        String filePath = path.substring(0, path.lastIndexOf("\\"));
        // 如果路径不存在，创建路径
        File file = new File(filePath);
        //System.out.println(path+"-----------"+file.exists());
        if (!file.exists())
            file.mkdirs();
        FileOutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        fileOut.close();
    }

    /**
     * 在输出流中导出excel
     * @param excelName 导出的excel名称 包括扩展名
     * @param sheetName 导出的sheet名称
     * @param fieldName 列名数组
     * @param data 数据组
     * @param response response
     */
    public void makeStreamExcel(String excelName, String sheetName, String[] fieldName, List<String[]> data, HttpServletResponse response) {
        OutputStream os = null;
        try {
            response.reset(); // 清空输出流
            os = response.getOutputStream(); // 取得输出流
            response.setHeader("Content-disposition", "attachment; filename=" + new String(excelName.getBytes(), "ISO-8859-1")); // 设定输出文件头
            response.setContentType("application/msexcel"); // 定义输出类型
        } catch (IOException ex) {// 捕捉异常
            System.out.println("流操作错误:" + ex.getMessage());
        }
        //在内存中生成工作薄
        Workbook workbook = makeWorkBook(sheetName, fieldName, data);
        try {
            os.flush();
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output is closed");
        }
    }

    /**
     * 根据条件，生成工作薄对象到内存
     * @param sheetName 工作表对象名称
     * @param fieldName 首列列名称
     * @param data 数据
     * @return HSSFWorkbook
     */
    public Workbook makeWorkBook(String sheetName, String[] fieldName, List<String[]> data) {
        // 产生工作薄对象
        Workbook workbook = new SXSSFWorkbook();
        // 产生工作表对象
        Sheet sheet = workbook.createSheet();
        sheet.setColumnWidth(0, (short) 2000);// 单位 
        sheet.setColumnWidth(1, (short) 10000);// 单位 
        sheet.setColumnWidth(2, (short) 5000);// 单位 
        sheet.setColumnWidth(3, (short) 10000);// 单位 
        sheet.setColumnWidth(4, (short) 15000);// 单位 
        sheet.setColumnWidth(5, (short) 2000);// 单位 
        sheet.setColumnWidth(6, (short) 6000);// 单位 
        sheet.setColumnWidth(7, (short) 6000);// 单位 
        sheet.setColumnWidth(8, (short) 8000);// 单位 
        // 为了工作表能支持中文,设置字符集为UTF_16
        workbook.setSheetName(0, sheetName);
        // 产生一行
        Row row = sheet.createRow(0);
       
        row.setHeight((short)400);       
        // 设置字体          
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        //font.setFontName("黑体"); //字体
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体
        font.setBold(true);
        
        CellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);
        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillForegroundColor((short)1);
        //cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillBackgroundColor((short)1);
        // 产生单元格
        Cell cell;
        // 写入各个字段的名称
        for (int i = 0; i < fieldName.length; i++) {
            // 创建第一行各个字段名称的单元格
            //cell = row.createCell((short) i);
            cell = row.createCell(i);
            //-----------------------------------------------------------------------------
            
            cell.setCellStyle(cellStyle);
            //-----------------------------------------------------------------------------
            // 设置单元格内容为字符串型
            //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellType(CellType.STRING);
            // 为了能在单元格中输入中文,设置字符集为UTF_16
            // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            // 给单元格内容赋值
            //cell.setCellValue(new HSSFRichTextString(fieldName[i]));
            cell.setCellValue(fieldName[i]);
        }
        // 写入各条记录,每条记录对应excel表中的一行
        font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        //font.setFontName("黑体"); //字体
      
   	 	font.setFontHeightInPoints((short) 10); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色                 
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//粗体
        font.setBold(true);
        
        cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);
        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillForegroundColor((short)1);
        //cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillBackgroundColor((short)1);
        
        for (int i = 0; i < data.size(); i++) {
            String[] tmp = data.get(i);
            // 生成一行
            row = sheet.createRow(i + 1);
            row.setHeight((short)350);   
            for (int j = 0; j < tmp.length; j++) {
                //cell = row.createCell((short) j);
            	 cell = row.createCell(j);
            	 //-----------------------------------------------------------------------------
            	 
                 cell.setCellStyle(cellStyle);
                 //-----------------------------------------------------------------------------
                 //设置单元格字符类型为String
                 //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellType(CellType.STRING);
                 //cell.setCellValue(new HSSFRichTextString((tmp[j] == null) ? "" : tmp[j]));
                 cell.setCellValue((tmp[j] == null) ? "" : tmp[j]);
            }
            
            //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX="+i);
        }
        return workbook;
    }
    
    /**
     * 根据条件，生成工作薄对象到内存
     * @param sheetName 工作表对象名称
     * @param fieldName 首列列名称
     * @param fieldSize 首列列宽度
     * @param data 数据
     * @return HSSFWorkbook
     */
    public Workbook makeWorkBookEx(String sheetName, String[] fieldName, Integer[] fieldSize, List<String[]> data) {
        // 产生工作薄对象
        Workbook workbook = new SXSSFWorkbook();
        // 产生工作表对象
        Sheet sheet = workbook.createSheet();
        for(int i=0; i<fieldSize.length; i++)
        {
        	sheet.setColumnWidth(i, fieldSize[i]);// 单位 
        }
        // 为了工作表能支持中文,设置字符集为UTF_16
        workbook.setSheetName(0, sheetName);
        // 产生一行
        Row row = sheet.createRow(0);
       
        row.setHeight((short)400);       
        // 设置字体          
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        //font.setFontName("黑体"); //字体
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体
        font.setBold(true);
        
        org.apache.poi.ss.usermodel.CellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);
        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillForegroundColor((short)1);
        //cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillBackgroundColor((short)1);
        // 产生单元格
        Cell cell;
        // 写入各个字段的名称
        for (int i = 0; i < fieldName.length; i++) {
            // 创建第一行各个字段名称的单元格
            //cell = row.createCell((short) i);
            cell = row.createCell(i);
            //-----------------------------------------------------------------------------
            
            cell.setCellStyle(cellStyle);
            //-----------------------------------------------------------------------------
            // 设置单元格内容为字符串型
            //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellType(CellType.STRING);
            // 为了能在单元格中输入中文,设置字符集为UTF_16
            // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            // 给单元格内容赋值
            //cell.setCellValue(new HSSFRichTextString(fieldName[i]));
            cell.setCellValue(fieldName[i]);
        }
        // 写入各条记录,每条记录对应excel表中的一行
        font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        //font.setFontName("黑体"); //字体
      
   	 	font.setFontHeightInPoints((short) 10); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色                 
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//粗体
        font.setBold(true);
        
        cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);
        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillForegroundColor((short)1);
        //cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillBackgroundColor((short)1);
        
        for (int i = 0; i < data.size(); i++) {
            String[] tmp = data.get(i);
            // 生成一行
            row = sheet.createRow(i + 1);
            row.setHeight((short)350);   
            for (int j = 0; j < tmp.length; j++) {
                //cell = row.createCell((short) j);
            	 cell = row.createCell(j);
            	 //-----------------------------------------------------------------------------
            	 
                 cell.setCellStyle(cellStyle);
                 //-----------------------------------------------------------------------------
                 //设置单元格字符类型为String
                 //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                 cell.setCellType(CellType.STRING);
                 //cell.setCellValue(new HSSFRichTextString((tmp[j] == null) ? "" : tmp[j]));
                 cell.setCellValue((tmp[j] == null) ? "" : tmp[j]);
            }
            
            //System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX="+i);
        }
        return workbook;
    }
    
    /**
     * 根据条件，生成工作薄对象到内存
     * @param sheetName 工作表对象名称
     * @param fieldName 首列列名称
     * @param fieldSize 首列列宽度
     * @param data 数据
     * @return HSSFWorkbook
     * Map<String, List<String[]>> key=sheetName,value is datalist
     */
    public Workbook makeWorkBookEx4MultiSheet(List<String> sheetNameList, String[] fieldName, Integer[] fieldSize, Map<String, List<String[]>> datamap) {
        // 产生工作薄对象
        Workbook workbook = new SXSSFWorkbook();        
        // 设置字体          
        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        //font.setFontName("黑体"); //字体
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体
        font.setBold(true);
        
        org.apache.poi.ss.usermodel.CellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);
        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillForegroundColor((short)1);
        //cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillBackgroundColor((short)1);
       
        // 写入各条记录,每条记录对应excel表中的一行
        font = workbook.createFont();
        font.setFontHeightInPoints((short) 12); //字体高度
        font.setColor(HSSFFont.COLOR_RED); //字体颜色
        //font.setFontName("黑体"); //字体
      
   	 	font.setFontHeightInPoints((short) 10); //字体高度
        font.setColor(HSSFFont.COLOR_NORMAL); //字体颜色                 
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//粗体
        font.setBold(true);
        
        cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);
        //cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        //cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillForegroundColor((short)1);
        //cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);
        cellStyle.setFillBackgroundColor((short)1);
        // 产生工作表对象
        for(int index = 0; index < sheetNameList.size(); index++)
        {
        	Sheet sheet = workbook.createSheet();
            for(int i=0; i<fieldSize.length; i++)
            {
            	sheet.setColumnWidth(i, fieldSize[i]);// 单位 
            }
            // 为了工作表能支持中文,设置字符集为UTF_16
            workbook.setSheetName(0, sheetNameList.get(index));
        }
        for(int index = 0; index < sheetNameList.size(); index++)
        {
        	Sheet sheet = workbook.getSheetAt(index);
            // 产生一行
            Row row = sheet.createRow(0);
           
            row.setHeight((short)400);       
            
            // 产生单元格
            Cell cell;
            // 写入各个字段的名称
            for (int i = 0; i < fieldName.length; i++) {
                cell = row.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellType(CellType.STRING);
                //cell.setCellValue(new HSSFRichTextString(fieldName[i]));
                cell.setCellValue(fieldName[i]);
            }
            List<String[]> data = datamap.get(sheet);
            for (int i = 0; i < data.size(); i++) {
                String[] tmp = data.get(i);
                // 生成一行
                row = sheet.createRow(i + 1);
                row.setHeight((short)350);   
                for (int j = 0; j < tmp.length; j++) {
                    //cell = row.createCell((short) j);
                	 cell = row.createCell(j);
                	 cell.setCellStyle(cellStyle);
                     cell.setCellType(CellType.STRING);
                     cell.setCellValue((tmp[j] == null) ? "" : tmp[j]);
                }
            }
        }
       
        return workbook;
    }
    /**
     * 得到一个工作区最后一条记录的序号
     * @param sheetOrder 工作区序号
     * @return int
     * @throws IOException
     */
    public int getSheetLastRowNum(int sheetOrder) throws IOException {
    	Workbook workbook=null;
    	if(path.toLowerCase().endsWith("xls"))
    	{
    		workbook=new HSSFWorkbook(new FileInputStream(path));
		}
		if(path.toLowerCase().endsWith("xlsx"))
		{
			workbook=new XSSFWorkbook(new FileInputStream(path));
		} 
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        return sheet.getLastRowNum();
    }

    public void write(int sheetOrder, int colum, int row, String content) throws Exception {
    	Workbook workbook=null;
    	if(path.toLowerCase().endsWith("xls"))
    	{
    		workbook=new HSSFWorkbook(new FileInputStream(path));
		}
		if(path.toLowerCase().endsWith("xlsx"))
		{
			workbook=new XSSFWorkbook(new FileInputStream(path));
		} 
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        Row rows = sheet.createRow(row);
        Cell cell = rows.createCell(colum);
        cell.setCellValue(content);
        FileOutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        fileOut.close();

    }

    
    public String read(int sheetOrder, int colum, int row) throws Exception {
    	Workbook workbook=null;
    	if(path.toLowerCase().endsWith("xls"))
    	{
    		workbook=new HSSFWorkbook(new FileInputStream(path));
		}
		if(path.toLowerCase().endsWith("xlsx"))
		{
			workbook=new XSSFWorkbook(new FileInputStream(path));
		} 
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        Row rows = sheet.getRow(row);
        Cell cell = rows.getCell(colum);
        String content = cell.getStringCellValue();
        return content;
    }

    /**
     * 根据path属性，在磁盘生成一个新的excel
     * @throws IOException
     */
    public void makeEmptyExcel() throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        //截取文件夹路径
        String filePath = path.substring(0, path.lastIndexOf("\\"));
        // 如果路径不存在，创建路径
        File file = new File(filePath);
        if (!file.exists())
            file.mkdirs();
        FileOutputStream fileOut = new FileOutputStream(filePath + "\\" + path.substring(path.lastIndexOf("\\") + 1));
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * 得到一个工作区数
     * @param sheetOrder 工作区序号
     * @return int
     * @throws IOException
     */
    public int getSheetNum() throws IOException {
    	Workbook workbook=null;
    	if(path.toLowerCase().endsWith("xls"))
    	{
    		workbook=new HSSFWorkbook(new FileInputStream(path));
		}
		if(path.toLowerCase().endsWith("xlsx"))
		{
			workbook=new XSSFWorkbook(new FileInputStream(path));
		} 
        int n = workbook.getNumberOfSheets();
        return n;
    }
    /**
     * 根据工作区序号，读取该工作去下的所有记录，每一条记录是一个String[]<br/>
     * 注意如果单元格中的数据为数字将会被自动转换为字符串<br/>
     * 如果单元格中存在除数字，字符串以外的其他类型数据，将会产生错误
     * @param sheetOrder 工作区序号
     * @return
     * @throws IOException 
     * @throws  
     */
    public List<String[]> getDataFromSheet(int sheetOrder, int skipRows) throws IOException {
        //Workbook workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(path)));
    	Workbook workbook=null;
    	if(path.toLowerCase().endsWith("xls"))
    	{
    		workbook=new HSSFWorkbook(new FileInputStream(path));
		}
		if(path.toLowerCase().endsWith("xlsx"))
		{
			workbook=new XSSFWorkbook(new FileInputStream(path));
		}
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        List<String[]> strs = new ArrayList<String[]>();
        //注意得到的行数是基于0的索引 遍历所有的行
        //System.out.println(sheet.getLastRowNum());
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++)
        {
        	if(i<skipRows)
        	{
        		continue;
        	}
            Row row = sheet.getRow(i);
            if(row==null)
            {
            	continue;
            }
            short minColIx = row.getFirstCellNum();
            short maxColIx = row.getLastCellNum();
            if (maxColIx == -1) {
                continue;
            }
            
            int emptyIndex=0;
            for (short colIx = minColIx; colIx < maxColIx; colIx++) {//去掉空行
                Cell cell = row.getCell(colIx);
                if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {//空行 
                	emptyIndex++;
                }
            }
            if(emptyIndex>=maxColIx)
            {
            	continue;
            }
            
            String[] str = new String[maxColIx];
            int cellIndex = 0;
            for (short colIx = minColIx; colIx < maxColIx; colIx++) {
                Cell cell = row.getCell(colIx);
                //if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {//空行
                if (cell == null) {//空 
                    continue;
                }
                //cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell.setCellType(CellType.STRING);                

                str[cellIndex++] = getCellValue(cell);
            }
            strs.add(str);
        }
        return strs;
    }

    private String getCellValue(Cell cell) {

        String cellValue = null;
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case STRING:
                //System.out.println(cell.getRichStringCellValue().getString());
                cellValue = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = com.mids.util.MyDateUtil.convertDate2FormatString(new Date(cell.getDateCellValue().getTime()));
                } else {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = "";
        }
        return cellValue;
    }
    private String getCellValue_deprecated(Cell cell) {

        String cellValue = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                //System.out.println(cell.getRichStringCellValue().getString());
                cellValue = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = com.mids.util.MyDateUtil.convertDate2FormatString(new Date(cell.getDateCellValue().getTime()));
                } else {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = "";
        }
        return cellValue;
    }


    public static void main(String[] args) throws Exception {
        ExcelUtils excelUtils = new ExcelUtils("D:\\temp\\FF_ECCS_File.xls");
        List<String[]> sheets = excelUtils.getDataFromSheet(0,0);

        List<String[]> ss = new ArrayList<String[]>();
        ss.add(new String[] {"你撒地方", "sdfds"});
        ss.add(new String[] {"瓦尔", "撒地方"});
        excelUtils.makeExcel("smsLog", new String[] {"色粉", "的是否"}, ss);
        List<String[]> strs = excelUtils.getDataFromSheet(0,0);
        String content = "Hello Worlds";
        excelUtils.write(2, 3, 1, content);
        String newContent = excelUtils.read(0, 1, 1);
        System.out.println(newContent);
        excelUtils.makeEmptyExcel();
    }
}
