package com.cd.qlyhk.dto;

import java.util.List;

import com.cd.qlyhk.vo.QlyRhMorningPaperArticleVO;

public class QlyRhMorningPaperArticleDTO {
	//财税早报的日期（格式yyyy-MM-dd）
	String mp_date;
	// 要保存到财税早报清单的文章列表
	List<QlyRhMorningPaperArticleVO> articlesList;

	public String getMp_date() {
		return mp_date;
	}

	public void setMp_date(String mp_date) {
		this.mp_date = mp_date;
	}

	public List<QlyRhMorningPaperArticleVO> getArticlesList() {
		return articlesList;
	}

	public void setArticlesList(List<QlyRhMorningPaperArticleVO> articlesList) {
		this.articlesList = articlesList;
	}
	
}
