package cn.itcast.core.utils.Excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExportExcelUtil {

    /**
     * EXCEL第一行索引
     */
    public static final int EXCEL_FIRST_ROW_INDEX = 0;

    public static final String TRUE_VALUE = "Y";

    public static final String FALSE_VALUE = "N";

    /**
     * 导出Excel
     *
     * @param sheetName
     * @param title
     * @param values
     * @return
     */
    public static HttpServletResponse getXSSFWorkbook(HttpServletResponse response, String sheetName, String[] title, String[][] values, String filename) {

        // 第一步，创建一个webbook，对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        XSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle cellStyle = wb.createCellStyle();

        //声明列对象
        XSSFCell cell = row.createCell(0);
        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell.setCellValue(title[i]);
            cell.setCellStyle(cellStyle);
            cell = row.createCell(i + 1);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }

        // 第六步，将文件存到指定位置
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/x-msdownload");
            response.setContentType("application/octet-stream; charset=utf-8");
            String encode = URLEncoder.encode(filename, "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + encode);
            wb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    /**
     * 获取单元格字符串内容
     *
     * @param cell
     * @return
     */
    private static String getCellValue(XSSFCell cell) {
        String value = "";
        // 注意：一定要设成这个，否则可能会出现乱码
        //cell.setEncoding(XSSFCell.ENCODING_UTF_16);

        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    if (date != null) {
                        value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    } else {
                        value = "";
                    }
                } else {
                    long longVal = Math.round(cell.getNumericCellValue());
                    if (Double.parseDouble(longVal + ".0") == cell.getNumericCellValue())
                        value = String.valueOf(longVal);
                    else
                        value = String.valueOf(cell.getNumericCellValue());
                    //value =cell.getStringCellValue();
                }
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                // 导入时如果为公式生成的数据则无值
                if (!cell.getCellFormula().equals("")) {
                    try {
                        value = String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        value = cell.getStringCellValue();
                    }
                } else {
                    value = cell.getStringCellValue() + "";
                }
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                value = "";
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                value = (cell.getBooleanCellValue() ? TRUE_VALUE : FALSE_VALUE);
                break;
            default:
                value = "";
        }

        return value;
    }


    /**
     * 去掉字符串右边的空格
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */
    private static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

    /**
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     *
     * @param inputStream 读取数据的源Excel
     * @param headerIndex 表单头所在的索引
     * @return 读出的Excel中数据的内容
     * @throws IOException
     */
    public static List<Map<String, String>> parse(InputStream inputStream, int headerIndex) throws IOException {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        int maxcolumnIndex = 0;
        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        XSSFCell cell = null;
        for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
            XSSFSheet st = wb.getSheetAt(sheetIndex);
            List<String> headers = new ArrayList<String>();
            for (int rowIndex = 0; rowIndex <= st.getLastRowNum(); rowIndex++) {
                XSSFRow row = st.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                int currentcolumnIndex = row.getLastCellNum() + 1;
                if (currentcolumnIndex > maxcolumnIndex) {
                    maxcolumnIndex = currentcolumnIndex;
                }
                Map<String, String> rows = new LinkedHashMap<String, String>();

                for (int columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                    String value = "";
                    cell = row.getCell(columnIndex);
                    if (cell != null) {
                        value = getCellValue(cell);
                    }
                    if (rowIndex == headerIndex && value != null && !value.trim().equals("")) {
                        headers.add(value.trim());
                    } else {
                        if (columnIndex < headers.size()) {
                            rows.put(headers.get(columnIndex), value);
                        }
                    }
                }

                if (!isEmptyMap(rows)) {
                    result.add(rows);
                }
            }
        }
        return result;
    }


    private static boolean isEmptyMap(Map map) {
        boolean empty = map.isEmpty();
        if (!empty) {
            Collection values = map.values();
            for (Object value : values) {
                if (value != null && !value.toString().equals("")) {
                    return false;
                }
            }
            return true;
        }
        return empty;
    }

}
