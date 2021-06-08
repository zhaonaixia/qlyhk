package com.cd.rdf.util.excel;

import com.cd.rdf.base.BaseValueObject;

/**
 * <pre>
 * excel工作表数据行读取类(字符格式)
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
public class RowStringReader extends BaseValueObject{
  private String[] cellsText;
  public String[] getCellsText() {
    return cellsText;
  }
  public void setCellsText(String[] cellsText) {
    this.cellsText = cellsText;
  }
  
  /**
   * 获取列的内容
   * @param index
   * @return
   */
  public String getCellText(int index) {
    return cellsText[index];
  }   

}
