package com.search.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 */
public class ExcelUtils {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    public static List<List<String>> importXls(String xlsFilePath) {
        return importXls(xlsFilePath, 0);
    }

    /**
     * 导入excel
     *
     * @param xlsFilePath
     * @return
     */
    public static List<List<String>> importXls(String xlsFilePath, int sheetIndex) {
        File file = new File(xlsFilePath);

        if (file == null || !file.exists()) {
            return new ArrayList<>();
        }
        try {
            InputStream in = new FileInputStream(file);
            return ExcelUtils.importXls(in, sheetIndex);
        } catch (Exception e) {
            log.error("importXls exception.", e);
        }
        return new ArrayList<>();
    }

    public static List<List<String>> importXls(InputStream inputStream) {
        return importXls(inputStream, 0);
    }

    @SuppressWarnings("resource")
    public static List<List<String>> importXls(InputStream inputStream, int sheetIndex) {
        List<List<String>> resultList = null;
        Workbook hssfWorkbook = null;
        if (inputStream == null) {
            return null;
        }
        try {
            hssfWorkbook = WorkbookFactory.create(inputStream);
            resultList = new ArrayList<List<String>>();
            for (int numSheet = sheetIndex; numSheet < (sheetIndex + 1); numSheet++) {
                Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                // 循环行Row
                for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                    List<String> rowValues = new ArrayList<>();
                    org.apache.poi.ss.usermodel.Row hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow == null) {
                        continue;
                    }
                    String blank = "";
                    for (int i = 0; i < hssfRow.getLastCellNum(); i++) {
                        Cell brandIdHSSFCell = hssfRow.getCell(i);
                        String tmpValue = getCellData(brandIdHSSFCell);
                        rowValues.add(tmpValue);
                        blank += tmpValue;
                    }
                    if (StringUtils.isNotBlank(blank)) {
                        resultList.add(rowValues);
                    }
                }
            }
            return resultList;
        } catch (Exception e) {
            log.error("importXls exception.", e);
        } finally {
            if (hssfWorkbook != null) {
                try {
                    hssfWorkbook.close();
                } catch (IOException e) {
                    log.error("workbook close exception.", e);
                }
            }
        }
        return null;
    }

    public static String getCellData(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case HSSFCell.CELL_TYPE_NUMERIC: {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cell.getDateCellValue();
                    // 把Date转换成本地格式的字符串
                    String cellvalue = cell.getDateCellValue().toString();
                    return cellvalue;
                }
                // 如果是纯数字
                else {
                    String cellValue = "";
                    DecimalFormat df = new DecimalFormat("0.0000000");
                    cellValue = df.format(cell.getNumericCellValue());
                    return cellValue;
                }
            }
            case HSSFCell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() ? "1" : "0";
            case HSSFCell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public static String safeGetValueFromCell(List<String> valueCell, int index) {
        if (valueCell.size() > index) {
            return valueCell.get(index);
        }
        return "";
    }

    public static String safeGetValue(String value) {
        if (value == null) {
            return "";
        }
        return value;
    }


    public static void exportXSS(List<List<String>> dataList, String filePath) {
        try (XSSFWorkbook wb = new XSSFWorkbook(); FileOutputStream out = new FileOutputStream(filePath)) {
//            XSSFCellStyle setBorder = wb.createCellStyle();
            XSSFSheet sheet1 = wb.createSheet("sheet1");

            for (int i = 0; i < dataList.size(); i++) {
                XSSFRow row = sheet1.createRow(i);
                for (int j = 0; j < dataList.get(i).size(); j++) {
                    XSSFCell cell = row.createCell(j);
                    String str = dataList.get(i).get(j);
                    if (StringUtils.isBlank(str)) {
                        str = "";
                    }
                    cell.setCellValue(str);
                }
            }
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            log.error("exprot Xls exception.", e);
        }
    }

    /**
     * 设置excel值
     *
     * @param dataList
     * @param wb
     */
    public static void setExcelData(List<List<String>> dataList, XSSFWorkbook wb) {
        XSSFSheet sheet1 = wb.createSheet("sheet1");
        for (int i = 0; i < dataList.size(); i++) {
            XSSFRow row = sheet1.createRow(i);
            for (int j = 0; j < dataList.get(i).size(); j++) {
                XSSFCell cell = row.createCell(j);
                String str = dataList.get(i).get(j);
                if (StringUtils.isBlank(str)) {
                    str = "";
                }
                cell.setCellValue(str);
            }
        }
    }

    public static void setBrowser(HttpServletResponse response, XSSFWorkbook workbook, String fileName) {
        try (OutputStream os = new BufferedOutputStream(response.getOutputStream())) {
            //清空response
            response.reset();
            //设置response的Header
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //将excel写入到输出流中
            workbook.write(os);
            os.flush();
            log.info("设置浏览器下载成功!");
        } catch (Exception e) {
            log.info("设置浏览器下载失败!");
            e.printStackTrace();
        }

    }

}
