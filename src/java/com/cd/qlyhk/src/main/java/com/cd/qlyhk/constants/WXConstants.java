package com.cd.qlyhk.constants;

/**
 * 微信接口URL常量
 * @author sailor
 *
 */
public class WXConstants {

	// 获取微信access_token的url（用于获取关注了公众号的才能调用接口）
	public static String Token_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%appid%&secret=%secret%";
	
	// 获取微信ticket的url
	public static String Ticket_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%access_token%&type=jsapi";
	
	// 获取微信openid的url
	public static String Openid_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%appid%&secret=%secret%&code=%code%&grant_type=authorization_code";
	
	// 获取微信未关注的userinfo的url
	public static String Unconcerned_UserInfo_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%access_token%&openid=%openid%";
		
	// 获取微信userinfo的url
	public static String UserInfo_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%access_token%&openid=%openid%";
	
	// 获取微信二维码的url
	public static String QRcode_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%access_token%";
	
	// 发送客服消息（普通消息）的url
	public static String SendMsg_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%access_token%";
		
	// 发送模板消息的url
	public static String SendTemplateMsg_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%access_token%";
	
	// 发送群发消息（预览）的url
	public static String Preview_SendMsg_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=%access_token%";
	
	// 发送群发消息的url
	public static String SendMassMsg_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=%access_token%";
		
	// 删除群发消息的url
	public static String Delete_SendMassMsg_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=%access_token%";
		
	// 获取临时二维码的url
	public static String ShowQrCode_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
	
	// 获取授权的url
	public static String Authorize_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%appid%&redirect_uri=%redirect_uri%?type=weixin&scope=snsapi_base&response_type=code&state=1#wechat_redirect";
	
	// 获取微信图片的url
	public static String GET_IMG_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%access_token%&media_id=%media_id%";
	
	// 企业支付接口的url
	public static String Pay_Transfers_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
	
	// 获取素材总数的url
	public static String GET_MATERIAL_COUNT_URL = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=%access_token%";
	
	// 获取素材列表的url
	public static String GET_MATERIALS_URL = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=%access_token%";
}
