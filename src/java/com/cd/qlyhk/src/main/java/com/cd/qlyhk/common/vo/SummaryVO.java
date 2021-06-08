package com.cd.qlyhk.common.vo;

import java.io.Serializable;

/**
 * 合计汇总
 * @author lijia
 *
 */
public class SummaryVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4936280827390651280L;

	/**
	 * 合计金额
	 */
	private String hjJe;
	
	/**
	 * 合计税额
	 */
	private String hjSe;

	public String getHjJe() {
		return hjJe;
	}

	public void setHjJe(String hjJe) {
		this.hjJe = hjJe;
	}

	public String getHjSe() {
		return hjSe;
	}

	public void setHjSe(String hjSe) {
		this.hjSe = hjSe;
	}
}
