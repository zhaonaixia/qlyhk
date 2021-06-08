/**
 * Copyright(c) cd Science & Technology Ltd.
 */
package com.cd.qlyhk.common.vo;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * TODO。
 * </pre>
 *
 * @author sailor
 * 修改记录 
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

public class PageQueryResultVO<T> implements Serializable {

    private static final long serialVersionUID = -77450552828729327L;

    /**
     * 分页对象
     */
    private Page              pageInfo;

    /**
     * 每页数量
     */
    private List<T>           dataList;
    /**
     * 统计结果
     */
    private BillCount         totalInfo; // 代码有问题，给前端的接口是totalInfo，却用了billCount
    //private BillCount         billCount;
    
    /**
     * 汇总数
     */
    private SummaryVO summaryVO;

    public PageQueryResultVO() {

    }

    public PageQueryResultVO(Page page, List<T> dataList) {
        this.pageInfo = page;
        this.dataList = dataList;
    }

    public Page getPage() {
        return pageInfo;
    }

    public void setPage(Page page) {
        this.pageInfo = page;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public BillCount getBillCount() {
        return totalInfo;
    }

    public void setBillCount(BillCount billCount) {
        this.totalInfo = billCount;
    }

    public BillCount getTotalInfo() {
      return totalInfo;
    }

    public void setTotalInfo(BillCount totalInfo) {
      this.totalInfo = totalInfo;
    }

    public SummaryVO getSummaryVO() {
		return summaryVO;
	}

	public void setSummaryVO(SummaryVO summaryVO) {
		this.summaryVO = summaryVO;
	}

	@Override
    public String toString() {
        return "PageQueryResultVO{" +
                "page=" + pageInfo +
                ", dataList=" + dataList +
                ", billCount=" + totalInfo +
                '}';
    }
}
