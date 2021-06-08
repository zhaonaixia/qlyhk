package com.cd.qlyhk.vo;

/**
 * 文章阅读情况返回
 * 
 * @author sailor
 *
 */
public class RespArticleReadCondVO {

	// 分享者
	private String sharer;
	// 阅读的分享文章
	private String share_uuid;
	// 文章UUID
	private String article_uuid;
	// 文章的标题
	private String article_title;
	// 总阅读次数
	private int total_num;
	// 总阅读人数
	private int total_readers;
	// 分享次数
	private int share_num;
	// 分享人数
	private int share_readers;

	public String getSharer() {
		return sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public String getShare_uuid() {
		return share_uuid;
	}

	public void setShare_uuid(String share_uuid) {
		this.share_uuid = share_uuid;
	}

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getTotal_readers() {
		return total_readers;
	}

	public void setTotal_readers(int total_readers) {
		this.total_readers = total_readers;
	}

	public int getShare_num() {
		return share_num;
	}

	public void setShare_num(int share_num) {
		this.share_num = share_num;
	}

	public int getShare_readers() {
		return share_readers;
	}

	public void setShare_readers(int share_readers) {
		this.share_readers = share_readers;
	}

	public String getArticle_uuid() {
		return article_uuid;
	}

	public void setArticle_uuid(String article_uuid) {
		this.article_uuid = article_uuid;
	}

}
