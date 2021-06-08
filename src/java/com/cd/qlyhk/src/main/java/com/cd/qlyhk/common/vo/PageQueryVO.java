/**
 * Copyright(c) cd Science & Technology Ltd.
 */
package com.cd.qlyhk.common.vo;

import java.io.Serializable;

/**
 * <pre>
 * TODO。
 * </pre>
 *
 * @author sailor
 * 
 *          <pre>
 * 修改记录 
 *    修改后版本:     修改人：  修改日期:     修改内容:
 *          </pre>
 */

public class PageQueryVO implements Serializable {

    private static final long serialVersionUID = -77450552828729327L;

    /**
     * 当前页
     */
    private int               currentPage      = 1;

    /**
     * 每页数量
     */
    private int               pageSize         = 15;

    /**
     * 排序字段名
     */
    private String            orderName        = "";
    /**
     * 排序字段名
     */
    private String            orderName2        = "";

    private String            orderName3        = "";

    /**
     * 排序顺序
     */
    private String            order            = "";
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderName2() {
        return orderName2;
    }

    public void setOrderName2(String orderName2) {
        this.orderName2 = orderName2;
    }

    public String getOrderName3() {
        return orderName3;
    }

    public void setOrderName3(String orderName3) {
        this.orderName3 = orderName3;
    }

    @Override
    public String toString() {
        return "PageQueryVO{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", orderName='" + orderName + '\'' +
                ", order='" + order + '\'' +
                '}';
    }


}
