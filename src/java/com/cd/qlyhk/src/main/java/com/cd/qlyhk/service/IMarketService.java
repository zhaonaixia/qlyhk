package com.cd.qlyhk.service;

import java.util.Map;

import com.cd.qlyhk.common.vo.PageQueryVO;
import com.cd.qlyhk.dto.QlyRhMarketDTO;
import com.cd.qlyhk.dto.QlyRhUserStreamDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.QlyRhUserAccountVO;
import com.cd.qlyhk.vo.QlyRhUserStreamVO;
/**
 * 分销服务接口
 * @author sailor
 *
 */
public interface IMarketService {

	final String BEAN_ID = "marketService";
	
	/**
	 * 新增用户流水记录
	 * @param vo
	 */
	void insertQlyRhUserStream(QlyRhUserStreamVO vo);
	
	/**
	 * 更新我的账户信息
	 * @param vo
	 */
	void updateQlyRhUserAccount(QlyRhUserAccountVO vo);
	
	/**
	 * 我的账户信息
	 * @param userId
	 * @return
	 */
	QlyRhUserAccountVO getQlyRhUserAccountVO(int userId);
	
	/**
	 * 统计累计收益
	 * @param param
	 * @return
	 */
	double getQlyRhUserStreamTotalIncome(Map<String, Object> param);
	
	/**
	 * 查询收益余额列表
	 * @param streamDTO
	 * @return
	 */
	Response queryIncomeBalance(QlyRhUserStreamDTO streamDTO);
	
	/**
	 * 查询我的伙伴列表
	 * @param streamDTO
	 * @return
	 */
	Response queryPartnerMember(QlyRhUserStreamDTO streamDTO);
	
	/**
	 * 查询分销列表
	 * @param streamDTO
	 * @return
	 */
	Response queryRetailList(QlyRhMarketDTO marketDTO);
	
	/**
	 * 查询分销列表
	 * @param streamDTO
	 * @return
	 */
	Response queryRetailList(Map<String, Object> paramMap, PageQueryVO pageQueryVO);
	
	/**
	 * 穿透-余额列表
	 * @param streamDTO
	 * @return
	 */
	Response queryPenetBalance(QlyRhUserStreamDTO streamDTO);
	
	/**
	 * 查询用户流水列表
	 * @param paramMap
	 * @param pageQueryVO
	 * @return
	 */
	Response queryQlyRhUserStreamList(Map<String, Object> paramMap, PageQueryVO pageQueryVO);
}
