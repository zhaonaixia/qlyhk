package com.cd.qlyhk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cd.qlyhk.constants.WXConstants;
/**
 * 调用微信接口工具类
 * @author sailor
 *
 */
@Component
public class WxUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(WxUtil.class);

	@Autowired
    private QlyhkConfigProperties qlyhkConfigProperties;
    private static QlyhkConfigProperties configProperties;
    @PostConstruct
    public void init(){
    	configProperties = this.qlyhkConfigProperties;
    }
    
	public static String GetToken() {
		String APP_ID = configProperties.getAPP_ID();
		String SECRET = configProperties.getSECRET();
		
		logger.info("读取配置文件appid:{},secret:{}", APP_ID, SECRET);
		String tokenUrl = WXConstants.Token_URL;
		tokenUrl = tokenUrl.replace("%appid%", APP_ID);
		tokenUrl = tokenUrl.replace("%secret%", SECRET);
		
		logger.info("url={}", tokenUrl);
		// 获取access_token
		String result = HttpRequestUtil.doGet(tokenUrl);// 返回结果字符串
		return result;
	}

	public static String GetTicket(String accessToken) {
		String ticketUrl = WXConstants.Ticket_URL;
		ticketUrl = ticketUrl.replace("%access_token%", accessToken);
		
		String result = HttpRequestUtil.doGet(ticketUrl);// 返回结果字符串
		return result;
	}
	
	public static String GetOpenid(String code) {
		String APP_ID = configProperties.getAPP_ID();
		String SECRET = configProperties.getSECRET();
		String openidUrl = WXConstants.Openid_URL;
		openidUrl = openidUrl.replace("%appid%", APP_ID);
		openidUrl = openidUrl.replace("%secret%", SECRET);
		openidUrl = openidUrl.replace("%code%", code);
		logger.info("*************获取微信用户openidURL*************" + openidUrl);
		String result = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
		return result;
	}
	
	public static String GetUserInfo(String accessToken, String openid) {
		String openidUrl = WXConstants.UserInfo_URL;
		openidUrl = openidUrl.replace("%access_token%", accessToken);
		openidUrl = openidUrl.replace("%openid%", openid);
		
		String result = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
		return result;
	}
	
	public static String GetUnconcernedUserInfo(String accessToken, String openid) {
		String openidUrl = WXConstants.Unconcerned_UserInfo_URL;
		openidUrl = openidUrl.replace("%access_token%", accessToken);
		openidUrl = openidUrl.replace("%openid%", openid);
		
		String result = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
		return result;
	}

	public static Map<String, String> GetReqParamSign(String jsapi_ticket, String url) {
		Map<String, String> ret = sign(jsapi_ticket, url);;
		logger.info("*************获取微信请求参数*************" + ret);
        return ret;
	}
	
	public static String GetCodeTicket(String accessToken, String jsonStr) {
		String qrcodeUrl = WXConstants.QRcode_URL;
		qrcodeUrl = qrcodeUrl.replace("%access_token%", accessToken);

		String result = HttpRequestUtil.doPostJSON(qrcodeUrl, jsonStr);// 返回结果字符串
		return result;
	}
	
	
	public static String SendMessage(String accessToken, String touser, String content) {
		String sendMsgUrl = WXConstants.SendMsg_URL;
		sendMsgUrl = sendMsgUrl.replace("%access_token%", accessToken);
		
		String jsonStr = "{\"touser\":\""+ touser +"\", \"msgtype\":\"text\",\"text\":{\"content\":\""+ content +"\"}}";
		
		String result = HttpRequestUtil.doPostJSON(sendMsgUrl, jsonStr);
		return result;
	}
	
	public static String SendNewsMessage(String accessToken, String jsonStr) {
		String sendMsgUrl = WXConstants.SendMsg_URL;
		sendMsgUrl = sendMsgUrl.replace("%access_token%", accessToken);
		
		String result = HttpRequestUtil.doPostJSON(sendMsgUrl, jsonStr);
		return result;
	}
	
	public static String SendImageMessage(String accessToken, String touser, String media_id) {
		String sendMsgUrl = WXConstants.SendMsg_URL;
		sendMsgUrl = sendMsgUrl.replace("%access_token%", accessToken);
		
		String jsonStr = "{\"touser\":\""+ touser +"\", \"msgtype\":\"image\",\"image\":{\"media_id\":\""+ media_id +"\"}}";
		
		String result = HttpRequestUtil.doPostJSON(sendMsgUrl, jsonStr);
		return result;
	}
	
	public static String SendPreviewMassMessage(String accessToken, String jsonStr) {
		String sendMsgUrl = WXConstants.Preview_SendMsg_URL;
		sendMsgUrl = sendMsgUrl.replace("%access_token%", accessToken);
		
		String result = HttpRequestUtil.doPostJSON(sendMsgUrl, jsonStr);
		return result;
	}
	
	public static String SendMassMessage(String accessToken, String jsonStr) {
		String sendMsgUrl = WXConstants.SendMassMsg_URL;
		sendMsgUrl = sendMsgUrl.replace("%access_token%", accessToken);
		
		String result = HttpRequestUtil.doPostJSON(sendMsgUrl, jsonStr);
		return result;
	}
	
	public static String DeleteSendMassMessage(String accessToken, String jsonStr) {
		String sendMsgUrl = WXConstants.Delete_SendMassMsg_URL;
		sendMsgUrl = sendMsgUrl.replace("%access_token%", accessToken);
		
		String result = HttpRequestUtil.doPostJSON(sendMsgUrl, jsonStr);
		return result;
	}
	
	public static int GetImageCount(String accessToken) {
		int count = 0;
		
		String getMaterialCountUrl = WXConstants.GET_MATERIAL_COUNT_URL;
		getMaterialCountUrl = getMaterialCountUrl.replace("%access_token%", accessToken);
		String result = HttpRequestUtil.doGet(getMaterialCountUrl);
		
		if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			count = json.getIntValue("image_count");
		}
		
		return count;
	}
	
	public static String GetMaterialList(String accessToken) {
		int count = GetImageCount(accessToken);
		logger.info("*********************获取素材总数********************************************" + count);
		
		String getMaterialsUrl = WXConstants.GET_MATERIALS_URL;
		getMaterialsUrl = getMaterialsUrl.replace("%access_token%", accessToken);
		
		String jsonStr = "{\"type\":\"image\", \"offset\":0,\"count\":" + count + "}";
		
		String result = HttpRequestUtil.doPostJSON(getMaterialsUrl, jsonStr);
		return result;
	}
	
	public static String SendTemplateMessage(String accessToken, String touser, String templateId, String url, String data) {
		String sendTemplateMsgUrl = WXConstants.SendTemplateMsg_URL;
		sendTemplateMsgUrl = sendTemplateMsgUrl.replace("%access_token%", accessToken);
		
		String jsonStr = "{\"touser\":\""+ touser +"\",\"template_id\":\""+ templateId +"\",\"url\":\""+ url +"\",\"data\":"+ data + "}";
		
		String result = HttpRequestUtil.doPostJSON(sendTemplateMsgUrl, jsonStr);
		return result;
	}
	
	private static Map<String, String> sign(String jsapi_ticket, String url) {
		String APP_ID = configProperties.getAPP_ID();
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";

		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
//		System.out.println(string1);

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		ret.put("appid", APP_ID);
		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("nonceStr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		
		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	/**
     * 获取当前时间戳，单位秒
     * @return
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis()/1000;
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    public static String inputStream2String(InputStream in) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    public static String GetRedirectURL(String url) {
    	String APP_ID = configProperties.getAPP_ID();
    	
		String authorizeUrl = WXConstants.Authorize_URL;
		authorizeUrl = authorizeUrl.replace("%appid%", APP_ID);
		authorizeUrl = authorizeUrl.replace("%redirect_uri%", url);
		
		return authorizeUrl;
	}
    
    /**
     * 
     * @param accessToken
     * @param mediaId(微信服务器上的图片ID)
     * @param filePath(上传到服务器的目录)
     * @param fileUrlPath(图片访问的URL)
     * @return
     */
    public static String GetImgUrl(String accessToken, String mediaId, String filePath, String fileUrlPath) {
		String getImgUrl = WXConstants.GET_IMG_URL;
		getImgUrl = getImgUrl.replace("%access_token%", accessToken);
		getImgUrl = getImgUrl.replace("%media_id%", mediaId);
		
		String result = HttpRequestUtil.saveImageToDisk(getImgUrl, filePath, fileUrlPath);
		return result;
	}
}
