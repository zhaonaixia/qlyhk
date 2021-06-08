package com.cd.qlyhk.vo;

import com.cd.rdf.base.BaseValueObject;

/**
 * 公司发布内容VO类
 * 
 * @author sailor
 *
 */
public class QlyRhCompanyArticleVO extends BaseValueObject {

	private static final long serialVersionUID = 1L;

	private int id;
	// 公司ID
	private int company_id;
	// 文章ID
	private int article_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

}
