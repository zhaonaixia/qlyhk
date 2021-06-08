package com.cd.qlyhk.service;

import com.cd.qlyhk.dto.ReqEnterprisePaymentDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.QlyRhOrderVO;

/**
 * 订单服务接口定义
 * @author sailor
 *
 */
public interface IOrderService {

	final String BEAN_ID = "orderService";
	
	/**
	 * 查询套餐列表
	 * @return
	 */
	Response queryQlyRhPackage(String propValue);
	
	/**
	 * 创建订单
	 * @param propValue
	 * @param packageId
	 * @param packageCode
	 * @param openId
	 * @return
	 */
	Response createOrder(String propValue, String packageId, String packageCode, String openId);
	
	/**
	 * 获取订单信息
	 * @param orderId
	 * @return
	 */
	QlyRhOrderVO getQlyRhOrderVO(String orderId);
	
	/**
	 * 更新订单支付状态
	 * @param payStatus
	 * @param orderId
	 */
	int updateOrderPayStatus(String payStatus, String orderId);
	
	/**
	 * 订单完成
	 * @param propValue
	 * @param openId
	 * @param order
	 */
	void orderFinish(String propValue, String openId, QlyRhOrderVO order);
	
	/**
	 * 更新订单状态
	 * @param status
	 * @param orderId
	 */
	void updateOrderStatus(String status, String orderId);
	
	/**
	 * 获取订单信息（最近一条）
	 * @param orderId
	 * @return
	 */
	QlyRhOrderVO getQlyRhOrderLately(int userId);
	
	/**
	 * 企业支付接口
	 * @param appId
	 * @param mchId
	 * @param mchSecret
	 * @param ip
	 * @param paymentDTO
	 * @return
	 */
	Response enterprisePayment(String appId, String mchId, String mchSecret, String certPath, String ip, ReqEnterprisePaymentDTO paymentDTO);
	
	/**
	 * 查询用户的累计消费金额
	 * @param userId
	 * @return
	 */
	double getOrderTotalAmount(int userId);
	
	/**
	 * 查询提现结果。
	 * @param partner_trade_no
	 * @return
	 */
	Response queryCashWithdrawal(String appId, String mchId, String mchSecret, String certPath, String partner_trade_no);
}
