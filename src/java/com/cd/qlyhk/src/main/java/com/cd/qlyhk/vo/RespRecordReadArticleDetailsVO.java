package com.cd.qlyhk.vo;

import java.util.List;
import java.util.Map;

/**
 * 访客详情里面阅读文章情况
 * 
 * @author sailor
 *
 */
public class RespRecordReadArticleDetailsVO {

	// 文章的标题
	private String article_title;
	// 文章分享ID
	private String share_uuid;
	// 阅读次数
	private int read_num;
	// 阅读时长
	private long read_time;
	// 分享次数
	private int share_num;
	
	private List<Map<String, Object>> readDetails;

	public String getArticle_title() {
		return article_title;
	}

	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}

	public int getRead_num() {
		return read_num;
	}

	public void setRead_num(int read_num) {
		this.read_num = read_num;
	}

	public long getRead_time() {
		return read_time;
	}

	public void setRead_time(long read_time) {
		this.read_time = read_time;
	}

	public int getShare_num() {
		return share_num;
	}

	public void setShare_num(int share_num) {
		this.share_num = share_num;
	}

	public String getShare_uuid() {
		return share_uuid;
	}

	public void setShare_uuid(String share_uuid) {
		this.share_uuid = share_uuid;
	}

	public List<Map<String, Object>> getReadDetails() {
		return readDetails;
	}

	public void setReadDetails(List<Map<String, Object>> readDetails) {
		this.readDetails = readDetails;
	}

}
