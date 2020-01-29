package com.yepit.mapp.framework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianlong on 2017/9/1.
 */
public class ExcelUtils {

    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    public static void createXlsx(String sheetName, List<String> header, List<List<String>> dataList, String finalFileName) throws IOException {
        //创建excel工作簿
        XSSFWorkbook book = new XSSFWorkbook();
        CreationHelper createHelper = book.getCreationHelper();

        XSSFCellStyle style = book.createCellStyle();
        style.setWrapText(true);

        //创建第一个sheet（页），命名为sheetName
        if(StringUtils.isBlank(sheetName)){
            sheetName = "sheet0";
        }
        XSSFSheet sheet = book.createSheet(sheetName);
        sheet.setColumnWidth(3, 13000);
        sheet.setDefaultColumnWidth(20);

        //如果有表头,则在第一行创建
        if (header != null && header.size() > 0) {
            XSSFRow row = sheet.createRow((short) 0);
            for (int i = 0; i < header.size(); i++) {
                row.createCell(i).setCellValue(header.get(i));
            }
        }

        //如果有需要填充的数据,则从第二行开始填写
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                System.out.println("行数:"+i);
                XSSFRow row = null;
                if(header == null || header.size() == 0){
                    row = sheet.createRow((i));
                }else{
                    row = sheet.createRow((i + 1));
                }
                List<String> rowData = dataList.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }
        }

        //创建一个文件
        File newFile = new File(finalFileName);
        if(!newFile.exists()){
            newFile.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(finalFileName);
        // 把上面创建的工作簿输出到文件中
        book.write(fileOut);
        //关闭输出流
        fileOut.close();
    }

    public static void createXls(String sheetName, List<String> header, List<List<String>> dataList, String finalFileName) throws IOException {
        //创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        //创建第一个sheet（页），命名为sheetName
        if(StringUtils.isBlank(sheetName)){
            sheetName = "sheet0";
        }
        Sheet sheet = wb.createSheet(sheetName);

        //如果有表头,则在第一行创建
        if (header != null && header.size() > 0) {
            Row row = sheet.createRow((short) 0);
            for (int i = 0; i < header.size(); i++) {
                row.createCell(i).setCellValue(header.get(i));
            }
        }

        //如果有需要填充的数据,则从第二行开始填写
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Row row = null;
                if(header == null || header.size() == 0){
                    row = sheet.createRow((short) (i));
                }else{
                    row = sheet.createRow((short) (i + 1));
                }
                List<String> rowData = dataList.get(i);
                for (int j = 0; j < rowData.size(); j++) {
                    row.createCell(j).setCellValue(rowData.get(j));
                }
            }
        }

        //创建一个文件
        File newFile = new File(finalFileName);
        if(!newFile.exists()){
            newFile.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(finalFileName);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        //关闭输出流
        fileOut.close();
    }

    //使用POI读入excel工作簿文件
    public static List<List<String>> readWorkBook(String filePath, int startRow) throws Exception {
        if (StringUtils.isBlank(filePath)) {
            throw new Exception("要读取的Excel文件不存在");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("要读取的Excel文件不存在");
        }

        List<List<String>> dataList = null;

        String postfix = filePath.substring(filePath.indexOf(".") + 1, filePath.length());

        if (postfix.equals(OFFICE_EXCEL_2003_POSTFIX)) {//读取xls的文件
            dataList = readXls(filePath, startRow);
        } else if(postfix.equals(OFFICE_EXCEL_2010_POSTFIX)){//读取xlsx格式的文件
            dataList = readXlsx(filePath, startRow);
        }else{
            throw new Exception("只能读取.xls和.xlsx格式的文件");
        }

        return dataList;
    }

    /**
     * 读取xlsx格式的文件
     *
     * @param filePath
     * @param startRow
     * @return
     * @throws Exception
     */
    public static List<List<String>> readXlsx(String filePath, int startRow) throws Exception {
        FileInputStream in = null;
        Workbook wb = null;
        List<List<String>> rows = null;
        try {
            in = new FileInputStream(filePath);
            wb = WorkbookFactory.create(in);
            Sheet sheet = wb.getSheetAt(0);
            rows = new ArrayList<List<String>>();
            for (Row row : sheet) {
                List<String> rowInfo = new ArrayList<String>();
                //遍历row中的所有方格
                for (Cell cell : row) {
                    if(cell == null || StringUtils.isBlank(cell.toString())){
                        rowInfo.add("");
                    }else{
                        rowInfo.add(cell.toString());
                    }
                }
                rows.add(rowInfo);
            }

            //去掉startRow之前的行
            if (startRow > 0 && startRow < rows.size()) {
                rows.remove(startRow - 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return rows;
    }

    /**
     * 读取xls格式的文件
     *
     * @param filePath
     * @param startRow
     * @return
     * @throws Exception
     */
    public static List<List<String>> readXls(String filePath, int startRow) throws Exception {
        FileInputStream in = null;
        HSSFWorkbook wb = null;
        List<List<String>> rows = null;
        try {
            in = new FileInputStream(filePath);
            wb = new HSSFWorkbook(in);
            HSSFSheet sheet = wb.getSheetAt(0);
            rows = new ArrayList<List<String>>();
            for (int rowNum = startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = sheet.getRow(rowNum);
                if (hssfRow != null) {
                    List<String> rowInfo = new ArrayList<String>();
                    for (int i = 0; i < hssfRow.getPhysicalNumberOfCells(); i++) {
                        HSSFCell cell = hssfRow.getCell(i);
                        if(cell == null || StringUtils.isBlank(cell.toString())){
                            rowInfo.add("");
                        }else{
                            rowInfo.add(hssfRow.getCell(i).toString());
                        }
                    }
                    rows.add(rowInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return rows;
    }

//    public static void main(String[] args) throws Exception {
//        List<String> header = new ArrayList<String>();
//        header.add("用户号码");
//        header.add("名称");
//        header.add("属地");
//        header.add("产品编号");
//        header.add("是否发送短信");
//        header.add("操作类型");
//        header.add("开户结果");
//
//        List<List<String>> dataList = new ArrayList<List<String>>();
//        List<String> row1 = new ArrayList<String>();
//        row1.add("19283822");
//        row1.add("测试用户1");
//        row1.add("025");
//        row1.add("11988290112");
//        row1.add("1");
//        row1.add("1");
//        dataList.add(row1);
//
//        List<String> row2 = new ArrayList<String>();
//        row2.add("888882223");
//        row2.add("测试用户2");
//        row2.add("025");
//        row2.add("00099922");
//        row2.add("1");
//        row2.add("2");
//        dataList.add(row2);
//
////        createWorkBook("批量开户结果", header, dataList, "workbook.xls");
//        List<String> header1 = new ArrayList<String>();
//        header1.add("用户号码");
//        header1.add("导入结果");
//        List<List<String>> resultList = new ArrayList<List<String>>();
//        for(int i=0;i<110000;i++){
//            List<String> row = new ArrayList<String>();
//            row.add(String.valueOf(i));
//            row.add("导入成功");
//            resultList.add(row);
//        }
//        ExcelUtils.createXlsx("黑名单导入结果", header1, resultList, "blackUser.xlsx");
//
////        List<List<String>> rowList = readWorkBook("/Users/qianlong/OscGithub/Corporate-Steward/cs-be/uploadFiles/2017090316513610.xls", 1);
////        System.out.println(JsonUtils.Object2Json(rowList));
//    }

}
