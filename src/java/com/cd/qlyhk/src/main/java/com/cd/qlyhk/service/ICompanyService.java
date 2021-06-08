package com.cd.qlyhk.service;

import java.util.List;
import java.util.Map;

import com.cd.qlyhk.dto.QlyRhCompanyDTO;
import com.cd.qlyhk.dto.QlyRhReleaseArticleDTO;
import com.cd.qlyhk.dto.QlyRhTeamArticleDTO;
import com.cd.qlyhk.rest.Response;
/**
 * 公司服务接口
 * @author sailor
 *
 */
public interface ICompanyService {

	final String BEAN_ID = "companyService";
	
	/**
	 * 新增公司
	 * @param vo
	 */
	Response insertQlyRhCompany(QlyRhCompanyDTO vo);
	
	/**
	 * 创建公司
	 * @param vo
	 */
	Response createCompany(QlyRhCompanyDTO vo);
	
	/**
	 * 加入公司
	 * @param vo
	 */
	Response joinCompany(QlyRhCompanyDTO vo);
	
	/**
	 * 查询公司信息
	 * @param vo
	 */
	Response getCompanyInfo(QlyRhCompanyDTO vo);
	
	/**
	 * 修改公司信息
	 * @param vo
	 */
	Response editCompany(QlyRhCompanyDTO vo);
	
	/**
	 * 添加发布文章
	 * @param releaseArticleDTO
	 * @param articleImgUpload
	 * @param articleImgUrl
	 * @return
	 */
	Response addReleaseArticle(QlyRhReleaseArticleDTO releaseArticleDTO, String articleImgUpload, String articleImgUrl);
	
	/**
	 * 查询发布的文章列表
	 * @param releaseArticleDTO
	 * @return
	 */
	Response queryReleaseArticleList(QlyRhReleaseArticleDTO releaseArticleDTO);
	
	/**
	 * 查询公司下的员工分享情况列表
	 * @param vo
	 * @return
	 */
	Response queryCompanyStaffShareList(QlyRhCompanyDTO vo);
	
	/**
	 * 获取公司下的所有成员列表
	 * @param vo
	 * @return
	 */
	Response queryCompanyAllUser(QlyRhCompanyDTO vo);
	
	/**
	 * 公司转让
	 * @param vo
	 */
	Response teamTransfer(QlyRhCompanyDTO vo);
	
	/**
	 * 获取公司文章阅读详情列表
	 * @param vo
	 * @return
	 */
	Response queryTeamArticleReadDetails(QlyRhCompanyDTO vo);
	
	/**
	 * 获取公司文章分享详情列表
	 * @param vo
	 * @return
	 */
	Response queryTeamArticleShareDetails(QlyRhCompanyDTO vo);
	
	/**
	 * 队员分享详情
	 * @param openId
	 * @return
	 */
	Response queryTeamArticleReadCond(QlyRhTeamArticleDTO vo);
	
	/**
	 * 查询公司下面的成员
	 * @param companyId
	 * @return
	 */
	List<Map<String, Object>> queryTeamMemberList(int companyId);
	
	/**
	 * 获取推送公司新文章
	 * @param openId
	 * @return
	 */
	String getPushNewTeamArticle(String openId);
	
	/**
	 *公司列表
	 * @param vo
	 */
	Response queryCompanys(QlyRhCompanyDTO vo);
	
	/**
	 * 获取公司下成员数
	 * @param companyId
	 * @return
	 */
	int getCompanyAllUserNum(int companyId);
	
	/**
	 * 删除公司成员
	 * @param vo
	 */
	Response delCompanyUser(QlyRhCompanyDTO vo);
	
	/**
	 * 查询公司下的成员信息
	 * @param vo
	 * @return
	 */
	Response getCompanyStaffInfo(QlyRhCompanyDTO vo);
}
