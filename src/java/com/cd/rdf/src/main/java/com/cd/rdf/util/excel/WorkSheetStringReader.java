package com.cd.rdf.util.excel;

import com.cd.rdf.base.BaseValueObject;

/**
 * <pre>
 * excel工作表读取类(字符格式)
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
public class WorkSheetStringReader extends BaseValueObject{
  /** 行对象 */
  private RowStringReader[] rows;

  /** 工作表在工作薄中的下标，从0开始 */
  private int index;
  
  /** 工作表名称 */
  private String name;

  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public RowStringReader[] getRows() {
    return rows;
  }
  public void setRows(RowStringReader[] rows) {
    this.rows = rows;
  }
  
  /**
   * 获取行对象
   * @param index int 数组下标，从开始
   * @return RowStringReader
   */
  public RowStringReader getRow(int index) {
    return rows[index];
  }

}
