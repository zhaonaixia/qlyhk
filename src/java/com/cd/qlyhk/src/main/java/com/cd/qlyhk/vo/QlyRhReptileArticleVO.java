package com.cd.qlyhk.vo;

import java.util.Date;
/**
 * 文章主体内容VO类
 * @author sailor
 *
 */
public class QlyRhReptileArticleVO {
	// 文章ID
	private int id;
	// 类别ID
	private int category_id;
	// UUID
	private String uuid;
	// 文章标题
	private String article_title;
	// 文章URL
	private String article_url;
	// 文章图片
	private String pic_url;
	// 是否公开显示（1表示公开显示 0表示否）
	private String ispublic;
	// 审核状态（01 表示待审核 02表示审核通过 03表示审核不通过）
	private String audit_status;
	// 审核人
	private String audit_user;
	// 创建人
	private String create_user;
	// 描述
	private String description;
	// 创建时间
	private Date create_datetime;
	// 修改人
	private String modify_user;
	// 修改时间
	private Date modify_datetime;
	// 来源
	private String source;
	// 原文创建日期
	private String edit_date;
	// 是否置顶（1表示是 0表示否）
	private String istop;
	// 采集日期（yyyy-MM-dd）
	private String collect_date;
	
	// 是否已编辑（Y 表示是 N或者null表示未编辑）
	private String editor;

	// 分享配文
	private String share_text;
	
	// 非数据库定义的字段
	private int company_id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public String getArticle_url() {
		return article_url;
	}

	public void setArticle_url(String article_url) {
		this.article_url = article_url;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreate_datetime() {
		return create_datetime;
	}

	public void setCreate_datetime(Date create_datetime) {
		this.create_datetime = create_datetime;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getModify_user() {
		return modify_user;
	}

	public void setModify_user(String modify_user) {
		this.modify_user = modify_user;
	}

	public Date getModify_datetime() {
		return modify_datetime;
	}

	public void setModify_datetime(Date modify_datetime) {
		this.modify_datetime = modify_datetime;
	}

	public String getIspublic() {
		return ispublic;
	}

	public void setIspublic(String ispublic) {
		this.ispublic = ispublic;
	}

	public String getAudit_status() {
		return audit_status;
	}

	public void setAudit_status(String audit_status) {
		this.audit_status = audit_status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getEdit_date() {
		return edit_date;
	}

	public void setEdit_date(String edit_date) {
		this.edit_date = edit_date;
	}

	public String getIstop() {
		return istop;
	}

	public void setIstop(String istop) {
		this.istop = istop;
	}

	public String getCollect_date() {
		return collect_date;
	}

	public void setCollect_date(String collect_date) {
		this.collect_date = collect_date;
	}

	public String getAudit_user() {
		return audit_user;
	}

	public void setAudit_user(String audit_user) {
		this.audit_user = audit_user;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getShare_text() {
		return share_text;
	}

	public void setShare_text(String share_text) {
		this.share_text = share_text;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

}
