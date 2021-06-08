package com.cd.qlyhk.service;

import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.QlyRhShareVO;
import com.cd.qlyhk.vo.QlyRhUserClickArticleVO;
import com.cd.qlyhk.vo.ReqShareArticleDTO;
import com.cd.qlyhk.vo.WxRecordDTO;

/**
 * 分享服务接口
 * @author sailor
 *
 */
public interface IShareService {

	final String BEAN_ID = "shareService";
	
	/**
	 * 访问记录
	 * @param recordDTO
	 */
	void insertRecord(WxRecordDTO recordDTO);
	
	/**
	 * 更新阅读时长
	 * @param uuid
	 */
	void updateRecordQuitDate(String uuid);
	
	/**
	 * 新增分享记录
	 * @param reqShareArticleDTO
	 */
	void insertRhShare(ReqShareArticleDTO reqShareArticleDTO);
	
	/**
	 * 获取公众号二维码URL（扫描关注并推送一条文章消息）
	 * @param articleId
	 * @return
	 */
	Response getTempCodeUrl(String articleId);
	
	/**
	 * 获取我的分享VO
	 * @param shareUuid
	 * @return
	 */
	QlyRhShareVO getRhShareVO(String shareUuid);
	
	/**
	 * 扫描二维码关注之后，处理数据
	 * @param openId
	 * @param shareUuid
	 */
	QlyRhShareVO scanQRCodeShareAarticle(String openId, String shareUuid, String appId);
	
	/**
	 * 根据文章标题查询是否已经分享过
	 * @param articleTitle
	 * @param openId
	 * @return
	 */
	Response getArticleShareByUuid(String articleUuid, String openId);
	
	/**
	 * 记录用户点击文章详情
	 * @param vo
	 */
	void insertRecordClickArticle(QlyRhUserClickArticleVO vo);
}
