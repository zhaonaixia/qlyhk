package com.cd.rdf.util.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cd.rdf.base.BaseValueObject;

import jxl.WorkbookSettings;

/**
 * <pre>
 * excel工作薄读取类(字符格式)
 * 支持excel95、97、2003、2007,文件后缀名为:.xls、.xlsx
 * </pre>
 * 
 * @author awens
 * @version 1.00.00
 * 
 * <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
@SuppressWarnings("serial")
public class WorkBookStringReader extends BaseValueObject{
  
    protected final Logger logger  = LoggerFactory.getLogger(getClass());
    
  /** 工作表列表 */
  WorkSheetStringReader[] sheets;

  /**
   * 
   * @param is InputStream excel文件流
   * @throws UnsupportedEncodingException 
   */
  @SuppressWarnings("unused")
  public WorkBookStringReader( InputStream isExcel) throws Exception {
    super();
        String errMsg;
        InputStream is = null;
        
        byte[] buffer = null;        
    //String filePostfix = getPostfix(fullFileName).toLowerCase();
    org.apache.poi.ss.usermodel.Workbook wbPoi = null;
    jxl.Workbook wbJxl = null;
    try {
          buffer = inputStreamToBytes(isExcel);
      is = new ByteArrayInputStream(buffer);            
         //将文件的输入流转换成Workbook     
      wbPoi = WorkbookFactory.create(is);
    } catch (Exception e) {
      logger.error("通过POI组件读取文件失败,错误原因:{}",e.getMessage());
      errMsg = e.getMessage();
      if ( errMsg.contains("spreadsheet seems to be Excel") ) {
        try {
          is = new ByteArrayInputStream(buffer);            
              WorkbookSettings workbookSettings = new WorkbookSettings();
                workbookSettings.setEncoding("GBK"); //解决中文乱码,要使用GBK编码,用ISO-8859-1中文会乱码
          wbJxl = jxl.Workbook.getWorkbook(is,workbookSettings);
        } catch (Exception e2) {
          logger.error("通过Jxl组件读取文件失败,错误原因:{}",e2.getMessage());
          errMsg = "无效的excel文件格式";
          logger.error(errMsg  + ",错误原因:" + e2.getMessage());
          throw new RuntimeException(errMsg,e2);
        }
      }
      else {
        errMsg = "无效的excel文件格式";
        logger.error(errMsg  + ",错误原因:" + e.getMessage());
        throw new RuntimeException(errMsg,e);       
      }
    }
    
    if ( wbPoi != null ) {
      parseFromPoiWorkBook(wbPoi);      
    }
    else if ( wbJxl != null ) {
      parseFromJxlWorkBook(wbJxl);      
    }   
  }

  /**
   * 
   * @param buffer byte excel文件字节流
   * @throws Exception 
   */
  @SuppressWarnings("unused")
  public WorkBookStringReader( byte[] buffer) throws Exception {
    super();
        String errMsg;

        InputStream  is =null;
    //String filePostfix = getPostfix(fullFileName).toLowerCase();
    org.apache.poi.ss.usermodel.Workbook wbPoi = null;
    jxl.Workbook wbJxl = null;
    try {
      is = new ByteArrayInputStream(buffer);            
         //将文件的输入流转换成Workbook
      // ***** 
//          WorkbookSettings workbookSettings = new WorkbookSettings();
//            workbookSettings.setEncoding("GBK"); //解决中文乱码,要使用GBK编码,用ISO-8859-1中文会乱码
//      wbJxl = jxl.Workbook.getWorkbook(is,workbookSettings);
      
      wbPoi = WorkbookFactory.create(is);
    } catch (Exception e) {
      logger.error("通过POI组件读取文件失败,错误原因:{}",e.getMessage());
      errMsg = e.getMessage();
      if ( errMsg.contains("spreadsheet seems to be Excel") ) {
        try {
          is = new ByteArrayInputStream(buffer);            
              WorkbookSettings workbookSettings = new WorkbookSettings();
                workbookSettings.setEncoding("GBK"); //解决中文乱码,要使用GBK编码,用ISO-8859-1中文会乱码
          wbJxl = jxl.Workbook.getWorkbook(is,workbookSettings);
        } catch (Exception e2) {
          logger.error("通过Jxl组件读取文件失败,错误原因:{}",e2.getMessage());
          errMsg = "无效的excel文件格式";
          logger.error(errMsg  + ",错误原因:" + e2.getMessage());
          throw new RuntimeException(errMsg,e2);
        }
      }
      else {
        errMsg = "无效的excel文件格式";
        logger.error(errMsg  + ",错误原因:" + e.getMessage());
        throw new RuntimeException(errMsg,e);       
      }
    }
    
    if ( wbPoi != null ) {
      parseFromPoiWorkBook(wbPoi);      
    }
    else if ( wbJxl != null ) {
      parseFromJxlWorkBook(wbJxl);      
    }   
  }
  
    /**
     * get postfix of the path
     * @param fullFileName
     * @return
     */
    private String getPostfix(String fullFileName) {
        String result = null;
        if (fullFileName.contains(".")) {
            return fullFileName.substring(fullFileName.lastIndexOf(".") + 1, fullFileName.length());
        }
        return result;
    }
  
  public WorkSheetStringReader[] getSheets() {
    return sheets;
  }
  
  public void setSheets(WorkSheetStringReader[] sheets) {
    this.sheets = sheets;
  }

  /**
   * 获取工作表对象
   * @param index int 数组下标，从开始
   * @return WorkSheetStringReader
   */
  public WorkSheetStringReader getSheet(int index) {
    return sheets[index];
  }

  private void parseFromPoiWorkBook(org.apache.poi.ss.usermodel.Workbook wbPoi) {
    int wsCount = wbPoi.getNumberOfSheets();
    sheets = new WorkSheetStringReader[wsCount];
    for (int i=0; i<wsCount; i++) {
      WorkSheetStringReader wsStringReader = readSheetFromPoi(wbPoi, i);
      sheets[i] = wsStringReader;
    }
  }
  
  private void parseFromJxlWorkBook(jxl.Workbook wbJxl) throws Exception {
    jxl.Sheet[] sheetsJxl = wbJxl.getSheets();
    this.sheets = new WorkSheetStringReader[sheetsJxl.length];
    
    // <<< 工作表
    for (int i=0; i<sheetsJxl.length; i++) {
      jxl.Sheet crntSheet = sheetsJxl[i];     
      int rowCount = crntSheet.getRows();
      WorkSheetStringReader wsStrReader = new WorkSheetStringReader();
      wsStrReader.setIndex(i);
      byte[] wsNameBuff = crntSheet.getName().getBytes();
      String wsName =  new String(wsNameBuff,"GBK"); // ISO-8859-1      
      //String wsName =  new String(crntSheet.getName().getBytes()); // ISO-8859-1
      wsStrReader.setName( wsName ); // 乱码,需要转码
      RowStringReader[] rowsStrReader = new RowStringReader[rowCount];
      wsStrReader.setRows(rowsStrReader);
      sheets[i] = wsStrReader;  
      
      // << 行
      for (int j =0; j<rowCount; j++ ) {
        jxl.Cell[] cells = crntSheet.getRow(j);
        RowStringReader rowStrReader = new RowStringReader();
        rowsStrReader[j] = rowStrReader;
        String[] cellsText = new String[cells.length];
        rowStrReader.setCellsText(cellsText);

          for(int  k=0; k<cells.length; k++){
            String cellTest = cells[k].getContents();
            if ( StringUtils.isNotBlank(cellTest) ) {
              cellsText[k]  = cellTest.trim();
            }
            else {
              cellsText[k]  = "";             
            }
              
          }
          
      }
      // >> 行
    }
    // >>> 工作表
  }

  public static InputStream getInputStream(FileInputStream fileInput) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] buffer = new byte[1024*4];
    int n = -1;
    InputStream result = null;
    InputStream inputStream = null;
    try {
      while ((n=fileInput.read(buffer)) != -1) {
        baos.write(buffer, 0, n);       
      }
      byte[] byteArray = baos.toByteArray();
      inputStream = new ByteArrayInputStream(byteArray);
      result = inputStream;           
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e.getMessage());
        }
      }
    }
    
    return result;
  }

  public static byte[] inputStreamToBytes(InputStream is) throws IOException {
    byte[] result = null;
    result = new byte[is.available()];
    is.read(result);
    return result;
  }
  
  
  /**
   * 从Poi解析出的工作簿读取工作表数据
   * @param wbPoi
   * @param index
   * @return
   */
  public WorkSheetStringReader readSheetFromPoi(Workbook wbPoi , int index) {
    WorkSheetStringReader result = null;
        int maxRowIndex = 0;  // 最大的行下标
        int maxColIndex = 0; // 最大的列下标        
    
    int wsCount = wbPoi.getNumberOfSheets();
    if  ( index < wsCount ) {
      // <<< 获取最大行列坐标
      //Sheet wsPoi = wbPoi.getSheetAt(index);
      Sheet sheet = wbPoi.getSheetAt(index);
            maxRowIndex = sheet.getLastRowNum();// 得到excel的总记录条数
            for (int i = 0; i <= maxRowIndex; i++) {// 遍历行
                Row row = sheet.getRow(i);
                if(row != null){
                    int columNos = row.getLastCellNum();// 表头总共的列数
                    if ( maxColIndex < columNos )
                      maxColIndex = columNos;
                }
            }
      // >>> 获取最大行列坐标

      // <<< 生成数据
            // << 初始化工作表所有表格的值为空字符串
            result = new WorkSheetStringReader();
            result.setIndex(index);
            result.setName(sheet.getSheetName());
            RowStringReader[] rowsStrReader = new RowStringReader[maxRowIndex+1];
            result.setRows(rowsStrReader);
            for (int j=0; j<=maxRowIndex; j++) {
              RowStringReader rowStrReader = new RowStringReader();
              rowsStrReader[j] = rowStrReader;
              String[] cellsText = new String[maxColIndex+1];
              rowStrReader.setCellsText(cellsText);
              for (int k=0; k<=maxColIndex; k++) {
                cellsText[k] = "";
              }
            }
            // >> 初始化工作表所有表格的值为空字符串
            
            for (int j = 0; j <= maxRowIndex; j++) {// 遍历行
                Row row = sheet.getRow(j);
                if(row != null){
                  RowStringReader rowStrReader = result.getRow(j);
                  String[] cellsText = rowStrReader.getCellsText();                 
                    int columNos = row.getLastCellNum();// 表头总共的列数
                    for (int k = 0; k < columNos; k++) {
                        Cell cell = row.getCell(k);
                        if(cell != null){
                            cell.setCellType(CellType.STRING);
                            String cellText = cell.getStringCellValue().trim();
                            cellsText[k] = cellText;
                        }
                    }
                }
            }
      // >>> 生成数据     
    }
    
    return result;
  }
   
  public static void printBytes(byte[] bytes) {
    // << 原样打印字节数组
    System.out.print("字节数组的值:[");
    for (int i=0; i<bytes.length; i++) {
      if (i==0)
        System.out.print(bytes[i]); 
      else
        System.out.print(", "+ bytes[i]);         
    }
    System.out.println("]");      
    // >> 原样打印字节数组        
  }
  
      
 /* 
  public static void main(String[] args) throws Exception {
    byte[] sheetNameBytes = new byte[] {-54, -3, -63, -65, -67, -16, -74, -18, -41, -36, -43, -53};  // 数据金额总账
    String name = new String(sheetNameBytes);
    String nameGbk = new String(sheetNameBytes,"GBK");
    // << 原样打印字节数组
    printBytes(sheetNameBytes);
    // >> 原样打印字节数组
    System.out.println("name:"+name);
    System.out.println("nameGbk:"+nameGbk);
    System.out.println("由字符串name转换回来的字节数组(不可还原)--------------------------------------------------");
    byte[] byteFromStr = name.getBytes();
    printBytes(byteFromStr);
    System.out.println("由字符串nameGbk转换回来的字节数组(不可还原)--------------------------------------------------");
    printBytes(nameGbk.getBytes());

        
        String fullFileNamJxl = "T:/cdtec/xkj/运维/系统bug/POI读取Excel报错/222222_POI读取老版格式报错.xls";
        File fileJxl = new File(fullFileNamJxl);
        InputStream isJxl = new FileInputStream(fileJxl);
        byte[] bufferJxl = WorkBookStringReader.inputStreamToBytes(isJxl);
        WorkBookStringReader wbStrReaderJxl = new WorkBookStringReader(bufferJxl);
        System.out.println("通过Jxl读取Excel95格式的文件返回结果:" + JSON.toJSONString(wbStrReaderJxl));

        isJxl.close();
        isJxl = new FileInputStream(fileJxl);
        WorkBookStringReader wbStrReaderJxl2 = new WorkBookStringReader(isJxl );
        System.out.println("通过Jxl读取Excel95格式的文件返回结果:" + JSON.toJSONString(wbStrReaderJxl2));

        //String fullFileNamPoi = "T:/cdtec/xkj/运维/系统bug/POI读取Excel报错/招行20190601-20190630_祥和.xls";
        String fullFileNamPoi = "T:/cdtec/xkj/运维/系统bug/POI读取Excel报错/222222_Excel97.xls";
        File filePoi = new File(fullFileNamPoi);
        InputStream isPoi = new FileInputStream(filePoi);
        byte[] bufferPoi = WorkBookStringReader.inputStreamToBytes(isPoi);
        WorkBookStringReader wbStrReaderPoi = new WorkBookStringReader(bufferPoi);
        System.out.println("通过Jxl读取Excel97~2003(.xls)格式的文件返回结果:" + JSON.toJSONString(wbStrReaderPoi));

        fullFileNamPoi = "T:/cdtec/xkj/运维/系统bug/POI读取Excel报错/222222_Excel2007.xlsx";
        filePoi = new File(fullFileNamPoi);
        isPoi = new FileInputStream(filePoi);
        bufferPoi = WorkBookStringReader.inputStreamToBytes(isPoi);
        wbStrReaderPoi = new WorkBookStringReader(bufferPoi);
        System.out.println("通过Jxl读取Excel2007(.xlsx)格式的文件返回结果:" + JSON.toJSONString(wbStrReaderPoi));
  }
*/  
}
