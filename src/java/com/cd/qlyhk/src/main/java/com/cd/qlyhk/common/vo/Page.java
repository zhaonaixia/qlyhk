package com.cd.qlyhk.common.vo;

import java.io.Serializable;

/**
 * 
 * <pre>
 * Page对象。MySQL分页
 * </pre>
 * 
 * @author jiangshuangbin jiangshuangbin@ycs168.cn
 * @version 1.00.00
 * 
 *          <pre>
 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */
public class Page implements Serializable {

    private static final long serialVersionUID  = -7631810861106628096L;

    /**
     * 默认的每页记录数
     */
    public static final int   DEFAULT_PAGE_SIZE = 15;

    /**
     * 每页的记录数
     */
    private int               pageSize          = DEFAULT_PAGE_SIZE;

    /**
     * 当前页
     */
    private int               currentPage       = 1;

    /**
     * 总记录数
     */
    private int               totalRecord       = 0;

    /**
     * 总页数
     */
    private int               totalPage         = 1;

    private int               start             = 0;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        if (this.pageSize <= 0) {
            return 0;
        }
        if (totalRecord <= 0) {
            this.totalPage = 1;
        } else {
            if (this.totalRecord % this.pageSize == 0) {
                this.totalPage = this.totalRecord / this.pageSize;
            } else {
                this.totalPage = (this.totalRecord / this.pageSize) + 1;
            }
        }
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 
     */
    public Page() {}

    /**
     * 
     */
    public Page(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    /**
     * 
     * @param pageSize
     * @param pageNo
     */
    public Page(int currentPage, int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public Page(String currentPage, String pageSize, String totalRecord) {
        super();
        this.pageSize = Integer.valueOf(pageSize);
        this.currentPage = Integer.valueOf(currentPage);
        this.totalRecord = Integer.valueOf(totalRecord);
    }

    public Page(int currentPage, int pageSize, int totalRecord) {
        super();
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalRecord = totalRecord;

        if (this.pageSize * (this.currentPage - 1) > this.totalRecord) {
            this.currentPage = 1;
        }

        if (totalRecord <= 0) {
            this.totalPage = 1;
        } else {
            if (this.totalRecord % this.pageSize == 0) {
                this.totalPage = this.totalRecord / this.pageSize;
            } else {
                this.totalPage = (this.totalRecord / this.pageSize) + 1;
            }
        }
    }

    public int getStart() {
        start = this.pageSize * (this.currentPage - 1);
        if (start > this.totalRecord) {
            start = 0;
            this.currentPage = 1;
        }
        return start;
    }

    public int getLimit() {
        return this.pageSize;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", totalRecord=" + totalRecord +
                ", totalPage=" + totalPage +
                ", start=" + start +
                '}';
    }
}
