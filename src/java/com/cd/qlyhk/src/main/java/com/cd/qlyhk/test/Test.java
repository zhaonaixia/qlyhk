package com.cd.qlyhk.test;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.JMException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.oss.OSSClient;
import com.cd.qlyhk.constants.MessageConstant;
import com.cd.qlyhk.constants.WXConstants;
import com.cd.qlyhk.reptile.WeixinArticePipeline;
import com.cd.qlyhk.reptile.WeixinArticeProcessor;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.utils.DateHelper;
import com.cd.qlyhk.utils.DateUtil;
import com.cd.qlyhk.utils.HttpRequestUtil;
import com.cd.qlyhk.utils.JwtTokenUtil;
import com.cd.qlyhk.utils.WxUtil;
import com.cd.qlyhk.vo.Audience;
import com.cd.qlyhk.vo.BaseNews;
import com.cd.qlyhk.vo.Kfnews;
import com.cd.qlyhk.vo.News;
import com.cd.qlyhk.vo.SendMessageVO;

import sun.misc.BASE64Encoder;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.monitor.SpiderMonitor;

public class Test {

	public static void WriteStringToFile5(String filePath, String str) {

		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			fos.write(str.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testGetPage3() {
//		String url = "https://mp.weixin.qq.com/s?__biz=MzAxOTczODg4NA==&amp;mid=2650442457&amp;idx=2&amp;sn=d2fd691fa470bc80d2066302b3cfc451&amp;chksm=83cc52b2b4bbdba4954663d79fa63dac80c0cf1c5766c6df385925cecfa9f58b2bb761123906&amp;scene=27#wechat_redirect";
		String url = "https://mp.weixin.qq.com/s/ZQDkPw37Oxmxpl7g9tiCqw";
//		String url = "https://mp.weixin.qq.com/s/9NNvb9MKXUvbxKNoKUxaOg";// 龙达

		long startTime, endTime;
		startTime = System.currentTimeMillis();
//        postMapper = myPostMapper;
//        posts = inposts;

		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		SpiderModel spiderModel = new SpiderModel();
		Spider mySpider = Spider.create(spiderModel).addUrl(url);
		mySpider.setDownloader(httpClientDownloader);
		try {
			SpiderMonitor.instance().register(mySpider);
			mySpider.thread(1).run();
		} catch (JMException e) {
			e.printStackTrace();
		}

		endTime = System.currentTimeMillis();
		System.out.println("爬取时间" + ((endTime - startTime) / 1000) + "秒--");
	}

	public static void testReptile() {
		String urls[] = { "https://mp.weixin.qq.com/s/OvkyqCE8sKz193nJiGlY8g" };
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("openId", "");
		param.put("filePath", "D:/share/wx/");
		param.put("fileUrlPath", "file:///D:/share/wx/");

		Spider.create(new WeixinArticeProcessor()).addPipeline(new WeixinArticePipeline(param)).addUrl(urls).thread(1)
				.run();

	}

	/*
	 * 上传图片到腾讯云万象优图（不可用） public static String doJsonPost() { int appId = 1300769320;
	 * String secretId = "AKIDMWM2e2fH8krlQwrGFzUnwuiKhrmVKJNH"; String secretKey =
	 * "oOoyuc4HLCoHUR9cptiIjvsa3HLAkAPP"; String bucketName = "sai-1300769320";
	 * 
	 * String result = ""; BufferedReader reader = null; String respContent = null;
	 * String urlimg =
	 * "https://mmbiz.qpic.cn/mmbiz_gif/VbrZzwePa2C0kApU1giazicUfqzMteJmMTTwB3rCqAMhY7U5hDQgZ8dOiar2dXsgtLhpEjDe7Yzhkgt8iaPA6QY7Og/640?wx_fmt=gif"
	 * ;// 图片地址 Credentials cred = new Credentials(appId, secretId, secretKey); try
	 * { CloseableHttpClient client = HttpClients.createDefault(); HttpPost post =
	 * new HttpPost("https://sai-1300769320.cos.ap-guangzhou.myqcloud.com"); String
	 * sign = Sign.appSign(cred, bucketName, 300);// 签名
	 * post.addHeader("Authorization", sign); String json1 = "{\"app_id\":\"" +
	 * appId + "\",\"bucket\":\"" + bucketName + "\",\"url\":\"" + urlimg + "\"}";//
	 * 参数格式要正确 StringEntity entity = new StringEntity(json1.toString(), "utf-8");
	 * entity.setContentType("application/json;charset=UTF-8");
	 * post.setEntity(entity); System.out.println(json1.toString()); String Json =
	 * json1.toString(); if (Json != null) { byte[] writebytes = Json.getBytes();//
	 * 设置文件长度 System.out.println(1); } HttpResponse resp = client.execute(post); if
	 * (resp.getStatusLine().getStatusCode() == 200) { HttpEntity he =
	 * resp.getEntity(); respContent = EntityUtils.toString(he, "UTF-8");
	 * System.out.println(respContent); } else { Header[] heads =
	 * resp.getAllHeaders(); StringBuffer sb = new StringBuffer(); for (Header
	 * header : heads) {
	 * sb.append("<p>").append(header.getName()).append(" : ").append(header.
	 * getValue()).append("</p>"); } System.out.println(sb.toString());
	 * System.out.println(resp.getStatusLine().getStatusCode()); HttpEntity he =
	 * resp.getEntity(); respContent = EntityUtils.toString(he, "UTF-8");
	 * System.out.println(respContent); } } catch (Exception e) {
	 * e.printStackTrace(); } return result; }
	 */

	// 链接url下载图片
	private static void downloadPicture(String urlList, String path) {
		URL url = null;
		try {
			url = new URL(urlList);
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			BASE64Encoder encoder = new BASE64Encoder();
			String encode = encoder.encode(buffer);// 返回Base64编码过的字节数组字符串
//			System.out.println(encode);
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 上传图片到阿里云OSS
	public static void doUploadImageToAliyun() {
		try {
			String urlimg = "https://mmbiz.qpic.cn/mmbiz_gif/VbrZzwePa2C0kApU1giazicUfqzMteJmMTTwB3rCqAMhY7U5hDQgZ8dOiar2dXsgtLhpEjDe7Yzhkgt8iaPA6QY7Og/640?wx_fmt=gif";
			// Endpoint以杭州为例，其它Region请按实际情况填写。
			String endpoint = "";
			// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录
			// https://ram.console.aliyun.com 创建RAM账号。
			String accessKeyId = "";
			String accessKeySecret = "";
			String bucketName = "";

			// 创建OSSClient实例。
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

			// 上传文件。
			String filePath = "D:/share/wx/2.gif";
			downloadPicture(urlimg, filePath);
			File f = new File(filePath);
			Date dt = new Date();
			String filleName = System.currentTimeMillis() + ".gif";
			String key = "WxImage/" + DateUtil.formatDateByFormat(dt, "yyyyMMdd") + "/" + filleName;
			ossClient.putObject(bucketName, key, f);

			// 生成URL
			// 设置URL过期时间为10年 3600l* 1000*24*365*10
			Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
			URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
			if (url != null) {
				System.out.println(url.toString());
			}
			// 关闭OSSClient。
			ossClient.shutdown();
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void testSendPassengerFlowMsg() {
		Date dt = new Date();
		String accessToken = "28_M0Ak5lJGZJ_GcL-Y8Ul9Ry1OIzDIVe04Xj8EYTUjPXegbnaDzDW5WI3WDXoHh2YgLyIyI1vkNkmRiQsUUVQJOKAxQT6qkIs0rQD6oMxYxHysygx5Goj5PSFXtQ2CkxVxKinHfy-yp4v7UYBiXOVgADAIZG";
		String passengerFlowTemplateId = "Jg3zKln1xV9yZPQa_2F9j9_XhIOWgLpADe_3O8X-FDQ";
		String url = "";
		String touser = "oSVdLv05qzm-bo26hSxuFBZV-EoU";

		int num = getRandomNumber();
		int num2 = getRandomNumber();
		String content = "昨天共有" + num + "位老师通过谁看过我功能，获取了" + num2 + "位准意向客户";
		JSONObject resultJson = new JSONObject();
		resultJson.put("value", content);
		resultJson.put("color", "#173177");

		JSONObject dataTimeJson = new JSONObject();
		dataTimeJson.put("value", DateUtil.formatDateByFormat(dt, "yyyy-MM-dd HH:mm:ss"));
		dataTimeJson.put("color", "#459ae9");

		JSONObject passengerFlowJson = new JSONObject();
		String passengerFlowContent = "昨天共有" + num + "位老师通过【文章-谁看过我】功能，获取了" + num2 + "位准客户";
		passengerFlowJson.put("value", passengerFlowContent);
		passengerFlowJson.put("color", "#459ae9");

		JSONObject remarkJson = new JSONObject();
		remarkJson.put("value", "点击下方【谁看过我】菜单，追踪客户");
		remarkJson.put("color", "#173177");

		JSONObject data = new JSONObject();
//		data.put("result", resultJson);
//		data.put("dataTime", dataTimeJson);
//		data.put("passengerFlow", passengerFlowJson);
//		data.put("remark", remarkJson);
		data.put("first", resultJson);
		data.put("keyword1", dataTimeJson);
		data.put("keyword2", passengerFlowJson);
		data.put("remark", remarkJson);

		String dateStr = data.toJSONString();
		String result = WxUtil.SendTemplateMessage(accessToken, touser, passengerFlowTemplateId, url, dateStr);
		System.out.println(result);
	}

	public static void testOpenMemberMsg() {
		Date dt = new Date();
		String accessToken = "31_iSaOst7M2YtrytJMOqf4XYjBmZUhJ_YQr1HYqPngRpJFPkremOvVgUDQTwt9r9lpfk3FwERuFPbQUvDKYpkO8dyXg2UjRXjKuiV87XiZhVMokV6nJ7bdOCBRWtu3Gl7dBFJ2-bR3uO0sOEMlSMKaAGAMGE";
		String passengerFlowTemplateId = "XeeGRBqjOYmhkG7tTywBsnH3malqyyUvbV14cfHR03o";
		String url = "";
//		String touser = "oaxwFwpu7ms9YfjDQPIJ90rdM0xs";//oaxwFwoAvRxtMIxB_-BXp7hx6F3g
		String touser = "oaxwFwre47GtQi6-i8y4bv5ajoqo";

		String content = "恭喜您，成为会员";
		JSONObject resultJson = new JSONObject();
		resultJson.put("value", content);
		resultJson.put("color", "#173177");

		JSONObject memberNumJson = new JSONObject();
		String memberNumContent = "00000001";
		memberNumJson.put("value", memberNumContent);
		memberNumJson.put("color", "#459ae9");

		JSONObject dataTimeJson = new JSONObject();
		dataTimeJson.put("value", DateUtil.formatDateByFormat(dt, "yyyy年MM月dd日 HH:mm"));
		dataTimeJson.put("color", "#459ae9");

		JSONObject remarkJson = new JSONObject();
		String remarkContent = "亲爱的会员您好~\n";
		remarkContent += "十分抱歉！\n";
		remarkContent += "服务器开了个小差，导致您充值的千里眼会员服务延迟发放。\n";
		remarkContent += "给各位大大带来的不便，我们深感抱歉（鞠躬）\n";
		remarkContent += "已为您免费升级成1年千里眼会员套餐，以表歉意\n";
		remarkContent += "感谢各位大大的支持与理解\n";
		remarkContent += "顺颂商祺~";

		remarkJson.put("value", remarkContent);
		remarkJson.put("color", "#173177");

		JSONObject data = new JSONObject();
//		data.put("result", resultJson);
//		data.put("dataTime", dataTimeJson);
//		data.put("passengerFlow", passengerFlowJson);
//		data.put("remark", remarkJson);
		data.put("first", resultJson);
		data.put("keyword1", memberNumJson);
		data.put("keyword2", dataTimeJson);
		data.put("remark", remarkJson);

		String dateStr = data.toJSONString();
		System.out.println(dateStr);
//		String result = WxUtil.SendTemplateMessage(accessToken, touser, passengerFlowTemplateId, url, dateStr);
//		System.out.println(result);
	}

	public static void testFollowUpAccuratelyMsg() {
		String accessToken = "27_szGHxDG4hnd8oKXQtKZM4rz1KV1S1Z0UcdzGy7kyQanGjJyHd7oe1B2Uxox24egeS15aPaY9VBIrKfU6pyV_atGXyKgLjHGqpwlQTGwFn6-qyd4YE5PiOn2pivsWFNbAEAOXF";
		String frontendUrl = "http://www.finway.com.cn/qlyhk-rh/";
		String toOpenId = "oSVdLv05qzm-bo26hSxuFBZV-EoU";

		String localUrl = frontendUrl + "#/buymember?userId=" + toOpenId;
		String content = " <a href='" + localUrl + "'>总有人偷偷关注您的产品，找到ta，不要错过这个精准跟进的机会哦！</a>\n\n";
		content += " <a href='" + localUrl + "'>开通会员，精准跟进>></a>";

		String result = WxUtil.SendMessage(accessToken, toOpenId, content);
		System.out.println(result);
	}

	private static int getRandomNumber() {
		Random r = new Random();
		int num = r.nextInt(100000);
		return num;
	}

	public static void test1() {
//		double bfb = 10 / 10;
//		NumberFormat nf = new DecimalFormat("0.##");
//		System.out.println(nf.format(bfb));
//		final Calendar c = Calendar.getInstance();
//		int d = c.get(Calendar.DATE);
//		System.out.println(d);

//		double bfb1 = 3;
//		double bfb2 = 10;
//		double bfb = bfb1/bfb2;
//		System.out.println(bfb);
//		NumberFormat nf = new DecimalFormat("0.##");
//		System.out.println(nf.format(bfb));

		Date dt = new Date();
		String str = DateUtil.formatDateByFormat(dt, "yyyy-MM-dd HH:mm:ss");
		System.out.println(str);

		Date s = DateUtil.afterNHours(dt, -47);
		String str1 = DateUtil.formatDateByFormat(s, "yyyy-MM-dd HH:mm:ss");
		System.out.println(str1);
	}

	public static void testUserinfo() {
//		String accessToken = "27_tUbSqtWhnzdhxlgsEVUn7rgoDoVfqXsgff5FdUOcy2XAHfPak5yKgAeDAn1MwNOp1DP6VRsVc2Cus-C7dkOF5XbHVvZYrXkdks1yu97CInY";
//		String openid = "oTxlI6DnvOAiILF70ouqEnehye5k";
		String accessToken = "33_EnI7-VpjsVJOymTeAxPmGlsqPlasd6K8kkArawvNgc0L1f_3tToLe21xJewrFyaCSjorO_AQtK6e2GrqHOWhF4WbYRt4qtj4ibWskdD2hCyoFspLpxykEyiDabvo-ssP2vfRZ40bps2PytCuZNMaAGAIWV";
		String openid = "oaxwFwmnKEmwugM57xD5flJJjGis";

		String openidUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%access_token%&openid=%openid%";
		openidUrl = openidUrl.replace("%access_token%", accessToken);
		openidUrl = openidUrl.replace("%openid%", openid);

		String result = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
		System.out.println(result);
	}
	
	public static void testAccessToken() {
		String tokenUrl = WXConstants.Token_URL;
		tokenUrl = tokenUrl.replace("%appid%", "wx2abd6374e47ec245");
		tokenUrl = tokenUrl.replace("%secret%", "0af18d94aa0397f91baeebef2e27b45a");

		// 获取access_token
		String result = HttpRequestUtil.doGet(tokenUrl);// 返回结果字符串
		System.out.println(result);
	}
	

	public static void testUserinfo2() {
		String APP_ID = "wx5c553df1562e396f";
		String SECRET = "ae4a0cfe5afd3527b4e455ab88ce33a6";

		String tokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%appid%&secret=%secret%";
		tokenUrl = tokenUrl.replace("%appid%", APP_ID);
		tokenUrl = tokenUrl.replace("%secret%", SECRET);

		String data = HttpRequestUtil.doGet(tokenUrl);// 返回结果字符串
		System.out.println(data);

		if (StringUtils.isNotBlank(data)) {
			JSONObject json = JSONObject.parseObject(data);
			String accessToken = json.getString("access_token");

//			String accessToken = "27_tUbSqtWhnzdhxlgsEVUn7rgoDoVfqXsgff5FdUOcy2XAHfPak5yKgAeDAn1MwNOp1DP6VRsVc2Cus-C7dkOF5XbHVvZYrXkdks1yu97CInY";
			String openid = "oSVdLv05qzm-bo26hSxuFBZV-EoU";

			String openidUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%access_token%&openid=%openid%";
			openidUrl = openidUrl.replace("%access_token%", accessToken);
			openidUrl = openidUrl.replace("%openid%", openid);

			String result = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
			System.out.println(result);
		}

	}

	public static void testUserinfoByAccessToken() {
		String accessToken = "35_z3RiIPo13-Qnstp3q49aygmCmOAKqvHxyVzeLdnVD4_s6PnyMpqN8wtWu6pB6su3939ah8G6L_XpIHDn2lS6wjp-BDkCl2kRDDJgTYmPauDkJJZ0DCKK_Zkmbjl7OGn5-J0c1zLN8EW0RG1vBHPhAIAWCK";
		String openid = "od0iW1ffoI1vAcsq-ekHf4Iw3Rl0";

		String openidUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%access_token%&openid=%openid%";
		openidUrl = openidUrl.replace("%access_token%", accessToken);
		openidUrl = openidUrl.replace("%openid%", openid);

		String result = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
		System.out.println(result);
	}

	public static void testGetToken() {
		String appid = "wx08522d97f4739523";
		String secret = "f0f5354983ae8a67d42081be79561389";

		String openidUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%appid%&secret=%secret%";
		openidUrl = openidUrl.replace("%appid%", appid);
		openidUrl = openidUrl.replace("%secret%", secret);

		String result = HttpRequestUtil.doGet(openidUrl);// 返回结果字符串
		System.out.println(result);
	}

	public static void testSendMessage() {
		String openId = "oaxwFwpu7ms9YfjDQPIJ90rdM0xs";
		String accessToken = "43_IJSvowbzyMWbTHvLtLgI8mnjJgaHzVbIrVK2o_R9A2w3TuvJHvv08tVlGqAdkepTWKfz8INR8MKAp37YgTvzXDGHuteG-e16ig8vV08_KBivUhI8H-c6qk3vBNJgFBOKGNNnSkS4Gt0NHZVePZYcAHAKUT";
//		String uuid = UUID.randomUUID().toString();

//		String article_url = "http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=b667fc9b-d3fb-4cf5-ad2c-306192113c29&articleId=" + uuid + "&userId=" + openId + "&getInto=listGetInto";
//		String content = " <a href='"+ article_url +"'>文章《测试文章》</a>\n\n";
//		content += " <a href='"+ article_url +"'>点击分享>>></a>";

//		String content = " <a href='"+ article_url +"'>总有人偷偷关注您的产品，找到ta，不要错过这个精准跟进的机会哦！</a>\n\n";
//		content += " <a href='"+ article_url +"'>开通会员，精准跟进>></a>";
		
		String content = "测试121212121";

//		String content = "今日财税头条\r\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=f684d727-035e-4da5-ac63-b1f2d11c1eb5&articleId=5b9ade0d-6353-47a7-9bf5-40bde7d2b59f&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 零售出口企业所得税核定应注意这些问题~ </a>\r\n"
//				+ "\r\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=a8cc3f23-6441-4047-b177-0db9b8047cfb&articleId=4623f1c5-f30a-4db5-9600-6738f41bb60c&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 都是“虚开”惹的祸，小窍门告诉你如何避“坑” </a>\r\n"
//				+ "\r\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=635088a0-1672-445c-a2b6-06c29151de1c&articleId=2d7ca509-0665-455c-9ff2-13da3e03824a&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 近期网友最关心的14个个税问题答案都在这里了~ </a>\r\n"
//				+ "\r\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=bfdb23ab-18a1-4690-b42d-7f43bd440a5d&articleId=c6c2bc6e-7241-4dd3-a1ba-d5eaf08721c8&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 资深销售总监：有没有办法做到平时不打电话，月底业绩又不会太差？ </a>\r\n"
//				+ "\r\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=6915d831-9fc8-4cfc-9b8f-cb60cd54b217&articleId=975c4a26-f1d5-46e7-9d34-7a247f354b2e&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 财税图解 | 一图看懂小微企业“六税两费”减征优惠 </a>\r\n"
//				+ "\r\n";
//		content += "👇👇👇 更多点我【获客好文】\n";
		
//		String content = "今日财税头条\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=f684d727-035e-4da5-ac63-b1f2d11c1eb5&articleId=5b9ade0d-6353-47a7-9bf5-40bde7d2b59f&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 零售出口企业所得税核定应注意这些问题~ </a>\n\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=a8cc3f23-6441-4047-b177-0db9b8047cfb&articleId=4623f1c5-f30a-4db5-9600-6738f41bb60c&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 都是“虚开”惹的祸，小窍门告诉你如何避“坑” </a>\n\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=635088a0-1672-445c-a2b6-06c29151de1c&articleId=2d7ca509-0665-455c-9ff2-13da3e03824a&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 近期网友最关心的14个个税问题答案都在这里了~ </a>\n\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=bfdb23ab-18a1-4690-b42d-7f43bd440a5d&articleId=c6c2bc6e-7241-4dd3-a1ba-d5eaf08721c8&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 资深销售总监：有没有办法做到平时不打电话，月底业绩又不会太差？ </a>\n\n"
//				+ "<a href='http://www.finway.com.cn/qlyhk-rh/#/contents?rh_articleId=6915d831-9fc8-4cfc-9b8f-cb60cd54b217&articleId=975c4a26-f1d5-46e7-9d34-7a247f354b2e&userId=oSVdLv26rbyJdothxjqQ-op61c98&getInto=listGetInto'>✬ 财税图解 | 一图看懂小微企业“六税两费”减征优惠 </a>\n\n";
//		content += "👇👇👇 更多点我【获客好文】\n";

//		String content = "感谢您订购会员，送您2500+份企业管理制度模板\n" + "百度网盘链接：https://pan.baidu.com/s/17oVTG6UGcvw1ttdpMg0G-A \n"
//				+ "提取码：ltwd";
		String result = WxUtil.SendMessage(accessToken, openId, content);
		System.out.println(result);
	}

	public static void testSendNewsMessage() {
		String openId = "oSVdLv05qzm-bo26hSxuFBZV-EoU";
		String accessToken = "31_h9uGhwccdB6ZBWLppuvQzfyU6KiID-Qv7kR7EUrK3zErXqpDP8l9oDoHdVu_C6mPclS2SrH3p1N_kkG_DlcWZpdx9h_Vw8Hr0hoas_oTddhQFFQA5hzxVNbQ62zsWvxUCZKGsDjqbHjv3R_NCLDgAHAPYQ";

		News news = new News();
		news.setDescription("测试测试测试11111111111");
		news.setPicurl(
				"http://47.107.46.219:8089/qlyhk/images/article/20191211/7b914abd-1b1e-46e3-a390-a5f217e05d59.jpg");
		news.setTitle("测试消息");
		news.setUrl("http://www.baidu.com");

//		News news2 = new News();
//		news2.setDescription("测试测试测试22222222222");
//		news2.setPicurl("http://47.107.46.219:8089/qlyhk/images/article/20191211/19e4b984-862f-466c-a42b-e9b54a95b2ca.png");
//		news2.setTitle("测试消息2");
//		news2.setUrl("http://www.baidu.com");

		List<News> list = new ArrayList<News>();
		list.add(news);
//		list.add(news2);
		Kfnews kfNews = new Kfnews();
		kfNews.setArticles(list);

		BaseNews baseNews = new BaseNews();
		baseNews.setNews(kfNews);
		baseNews.setTouser(openId);
		baseNews.setMsgtype("news");

		String jsonkfbean = JSONObject.toJSONString(baseNews);

		String result = WxUtil.SendNewsMessage(accessToken, jsonkfbean);
		System.out.println(result);
	}

	public static void testSendImageMessage() {
		String openId = "oaxwFwpu7ms9YfjDQPIJ90rdM0xs";
		String accessToken = "31_9qcFG9JKLi50A2llWfBcCdGMzjZ7wccmFhGlJI1DQJTB_IQxqnNFNBjYQp_PC-S8MWyECCM-t0Ty4Df_FDRolZdaKJV6VzBrkasPJa9B7TyOWOyvxYv3BsWT3tgMoCrsYZTpKjLT71YiYifUGLBaAHALQG";
		String media_id = "cVUE6NzT8fMCRMuw3LyZj40Z4vsIVTrxYPlTJ0sywvk";

		String result = WxUtil.SendImageMessage(accessToken, openId, media_id);
		System.out.println(result);
	}

	public static void testGetImageCount() {
		String accessToken = "32_-4gRFH74jxH2V5lILvsPnEJ4dO6KC74KtXlXHKLuP5Q7C92OzFNT5O60TJ0Ya2IenCbdQzGuYLoRTNhPXyQi8g6MjxIRiO7YMAIrW1rjfwrv4IQpDVHzsZXpGU72WIFA9ZX9cdtr_esZwaNsSFYeAGAXKD";

		String url = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=" + accessToken;

		String result = HttpRequestUtil.doGet(url);
		System.out.println(result);
	}

	public static void testGetImageList() {
		String accessToken = "36_i82VQ-IOhNvV9LYbOF7mZrM2alN11DgZnL2aZIAl2AFxD6m8-IEs0u4aDVSwYXEIE-2JhDM1_qJH_YUcyrPscrOwQ8KLsXxQ_mmVx_7r7WQTcE_h26nyXfY-KrHIv1H0cXEzOUWV0BvvxAApAOXcAGAESB";

		String url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=" + accessToken;

		String jsonStr = "{\"type\":\"image\", \"offset\":0,\"count\":12}";

		String result = HttpRequestUtil.doPostJSON(url, jsonStr);
		System.out.println(result);
	}
	
	public static void testSendPreviewMassMessage() {
		String openId = "od0iW1ffoI1vAcsq-ekHf4Iw3Rl0";
		String accessToken = "36_WI6a4gGR0osvwZG-Ap5rVqM2pZq0hV5aQNBGsMAIyTkg3ZRTRYL4E1DiOmP7HQiqN_ROxY9elPCxlHxVV2lewhvir6iL8peCn2chidO-egJVwFLo5YwDXERcPGNPry85u7SyywFuS8yGycIbWVFdAJAONX";
		
		JSONObject json = new JSONObject();
		json.put("touser", openId);
		json.put("msgtype", "text");
//		json.put("msgtype", "image");

		String content = "今日财税头条\n"
				+ "✬ 零售出口企业所得税核定应注意这些问题~ \n"
				+ "✬ 都是“虚开”惹的祸，小窍门告诉你如何避“坑” \n"
				+ "✬ 近期网友最关心的14个个税问题答案都在这里了~ \n"
				+ "✬ 资深销售总监：有没有办法做到平时不打电话，月底业绩又不会太差？ \n"
				+ "✬ 财税图解 | 一图看懂小微企业“六税两费”减征优惠 \n";
		content += "👇👇👇 更多点我【今日好文】\n";
//		String content = "今日财税头条";
		
		JSONObject textJson = new JSONObject();
		textJson.put("content", content);
		json.put("text", textJson);
		
//		JSONObject imageJson = new JSONObject();
//		imageJson.put("media_id", "FCpNMZONJofXPhNsss1r7EuLpZmv3Bmxcm6FEcrkN7k");
//		
//		json.put("image", imageJson);
		
		System.out.println(json.toJSONString());
		String result = WxUtil.SendPreviewMassMessage(accessToken, json.toString());
		System.out.println(result);
	}
	
	public static void testSendMassMessage() {
		String accessToken = "36_i82VQ-IOhNvV9LYbOF7mZrM2alN11DgZnL2aZIAl2AFxD6m8-IEs0u4aDVSwYXEIE-2JhDM1_qJH_YUcyrPscrOwQ8KLsXxQ_mmVx_7r7WQTcE_h26nyXfY-KrHIv1H0cXEzOUWV0BvvxAApAOXcAGAESB";

		List<String> openIds = new ArrayList<String>();
		openIds.add("od0iW1ffoI1vAcsq-ekHf4Iw3Rl0");
		openIds.add("od0iW1YH3LFmXSTPHJ3hH1yEbdJQ");
		
		JSONObject json = new JSONObject();
		json.put("touser", openIds);
		json.put("msgtype", "text");

		String content = "今日财税头条\n"
				+ "✬ 零售出口企业所得税核定应注意这些问题~ \n"
				+ "✬ 都是“虚开”惹的祸，小窍门告诉你如何避“坑” \n"
				+ "✬ 近期网友最关心的14个个税问题答案都在这里了~ \n"
				+ "✬ 资深销售总监：有没有办法做到平时不打电话，月底业绩又不会太差？ \n"
				+ "✬ 财税图解 | 一图看懂小微企业“六税两费”减征优惠 \n";
		content += "👇👇👇 更多点我【今日好文】\n";
		
		JSONObject textJson = new JSONObject();
		textJson.put("content", content);
		json.put("text", textJson);
		
		System.out.println(json.toString());
		String result = WxUtil.SendMassMessage(accessToken, json.toString());
		System.out.println(result);
	}
	
	public static void testDeleteSendMassMessage() {
		String accessToken = "36_WI6a4gGR0osvwZG-Ap5rVqM2pZq0hV5aQNBGsMAIyTkg3ZRTRYL4E1DiOmP7HQiqN_ROxY9elPCxlHxVV2lewhvir6iL8peCn2chidO-egJVwFLo5YwDXERcPGNPry85u7SyywFuS8yGycIbWVFdAJAONX";

		long msg_id = 3147483651L;
		
		JSONObject json = new JSONObject();
		json.put("msg_id", msg_id);
		
		System.out.println(json.toString());
		String result = WxUtil.DeleteSendMassMessage(accessToken, json.toString());
		System.out.println(result);
	}

	public static void test2() {
		String str = "https://mp.weixin.qq.com/mp/readtemplate?t=pages/video_player_tmpl&action=mpvideo&auto=0&vid=wxv_1106268093006938113";
		String key = "vid";
		String regex = "(^|&)" + key + "=([^&]*)(&|$)";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		while (m.find()) {
			System.out.println(m.group());
			System.out.println(m.group(0));
			System.out.println(m.group(2));
		}
	}

	public static void test3() {
		String str = "<div id=\"page-content\" class=\"rich_media_area_primary\"> \r\n"
				+ "     <div class=\"rich_media_area_primary_inner\"> \r\n" + "      <div id=\"img-content\"> \r\n"
				+ "       <h2 class=\"rich_media_title\" id=\"activity-name\"> 年底查账！税局将重点稽查会计凭证中这8大问题！ </h2> \r\n"
				+ "       <div id=\"meta_content\" class=\"rich_media_meta_list\"> \r\n"
				+ "        <span class=\"rich_media_meta rich_media_meta_nickname\" id=\"profileBt\"> <a href=\"javascript:void(0);\" id=\"js_name\"> 金财互联 </a> \r\n"
				+ "         <div id=\"js_profile_qrcode\" class=\"profile_container\" style=\"display:none;\"> \r\n"
				+ "          <div class=\"profile_inner\"> \r\n"
				+ "           <strong class=\"profile_nickname\">金财互联</strong> \r\n"
				+ "           <img class=\"profile_avatar\" id=\"js_profile_qrcode_img\" src=\"\" alt=\"\"> \r\n"
				+ "           <p class=\"profile_meta\"> <label class=\"profile_meta_label\">微信号</label> <span class=\"profile_meta_value\">jincaihulian</span> </p> \r\n"
				+ "           <p class=\"profile_meta\"> <label class=\"profile_meta_label\">功能介绍</label> <span class=\"profile_meta_value\">智慧财税综合服务平台</span> </p> \r\n"
				+ "          </div> \r\n"
				+ "          <span class=\"profile_arrow_wrp\" id=\"js_profile_arrow_wrp\"> <i class=\"profile_arrow arrow_out\"></i> <i class=\"profile_arrow arrow_in\"></i> </span> \r\n"
				+ "         </div> </span> \r\n"
				+ "        <em id=\"publish_time\" class=\"rich_media_meta rich_media_meta_text\"></em> \r\n"
				+ "       </div> \r\n" + "       <div class=\"rich_media_content \" id=\"js_content\"> \r\n"
				+ "        <p style=\"text-align: center;\"><img class=\"rich_pages\" data-ratio=\"0.23981191222570533\" src=\"file:///D:/share/wx/images/article\\20191210\\3145f684-1aaf-4b0a-8abb-47e1b9624435.gif\" data-type=\"gif\" data-w=\"638\" style=\"\"></p>\r\n"
				+ "        <section class=\"_editor\">";
		String s1 = str.substring(0, str.indexOf("<div id=\"img-content\">"));
		System.out.println(s1);

		String s2 = str.substring(str.indexOf("<div class=\"rich_media_content"));
		System.out.println(s2);
	}

	public static void test4() {
		Response result = Response.getDefaulTrueInstance();
		System.out.println(JSON.toJSONString(result));
		System.out.println(
				JSON.toJSONString(result, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty));
	}

	public static void test5() {
		String time = "2020-02-15";
		Date dt = DateUtil.parseDate(time);
		System.out.println(DateHelper.getPastTime(dt));
	}

	public static void test6() {
//		String time = "2020-03-10";
//		String time2 = "2020-03-13";
//		String time3 = "2020-03-11";
//		Date dt = DateUtil.parseDate(time);
//		Date dt2 = DateUtil.parseDate(time2);
//		Date dt3 = DateUtil.parseDate(time3);
//
//
//		long t1 = dt.getTime();
//		long t2 = dt2.getTime();
//		long t3 = dt3.getTime();
//		
//		if(t3 >= t1 && t3 <= t2) {
//			System.out.println("订单快到期了");
//		} else {
//			System.out.println("订单没到期");
//		}

//		NumberFormat nf = new DecimalFormat("00000000");
//		int i = 12;
//		System.out.println(nf.format(i));
		
		String s = "广东省.广州市.天河区";
		s = s.replaceAll("\\.", "-");
		System.out.println(s);
	}

	private static void sendMemberOpenMsg(String isMember, String openId, int userId, String endDate) {
		Date dt = new Date();
		Date endDt = DateUtil.parseDate(endDate);
		SendMessageVO msgVO = new SendMessageVO();
		msgVO.setMsgType(MessageConstant.MSG_TYPE_MBXX);
		msgVO.setTemplateId("11111111111111111111");
		msgVO.setUrl("");

		JSONObject data = new JSONObject();
		JSONObject contentJson = new JSONObject();
		JSONObject memberNumJson = new JSONObject();
		JSONObject dataTimeJson = new JSONObject();
		JSONObject remarkJson = new JSONObject();
		if ("1".equals(isMember)) {
			String content = "恭喜您，成功续费千里眼会员~";
			contentJson.put("value", content);
			contentJson.put("color", "#173177");

			String remarkContent = "您的会员服务已延长至" + DateUtil.formatDateByFormat(endDt, "yyyy年MM月dd日");

			remarkJson.put("value", remarkContent);
			remarkJson.put("color", "#173177");
		} else {
			String content = "恭喜您，成为会员~";
			contentJson.put("value", content);
			contentJson.put("color", "#173177");

			String remarkContent = "您已开通会员\n";
			remarkContent += "快去【谁看过我】功能\n";
			remarkContent += "获取您的意向客户吧~";

			remarkJson.put("value", remarkContent);
			remarkJson.put("color", "#173177");
		}
		NumberFormat nf = new DecimalFormat("00000000");
		String memberNumContent = nf.format(userId);
		memberNumJson.put("value", memberNumContent);
		memberNumJson.put("color", "#459ae9");

		dataTimeJson.put("value", DateUtil.formatDateByFormat(dt, "yyyy年MM月dd日 HH:mm"));
		dataTimeJson.put("color", "#459ae9");

		data.put("first", contentJson);
		data.put("keyword1", memberNumJson);
		data.put("keyword2", dataTimeJson);
		data.put("remark", remarkJson);

		System.out.println(data.toJSONString());
		msgVO.setData(data.toJSONString());
		msgVO.setTouser(openId);
	}

	/**
	 * 
	 * 根据文件id下载文件
	 * 
	 * 
	 * 
	 * @param mediaId
	 * 
	 *                媒体id
	 * 
	 * @throws Exception
	 * 
	 */

	public static InputStream getInputStream(String mediaId) {
		String accessToken = "31_lDAvZT7n6EJMmcwVbTbh-gJrSrBhx-5urFZDQliIp6K2c3GnqReR2d5VqlCaSa7HSOKYw9iIC4sEcURGBumKYJcI2I21NgDmIZwKd9lfL-u8I2wT8mlOJLDYNqzgZV8fhv99E_aEWZsadjFeCBEjAEAUTW";
		InputStream is = null;
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + accessToken + "&media_id="
				+ mediaId;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			// 设置连接主机服务器的超时时间：30000毫秒
			http.setConnectTimeout(30000);
			// 设置读取远程返回的数据时间：30000毫秒
			http.setReadTimeout(30000);
			http.connect();
			// 获取文件转化为byte流
			is = http.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}

	/**
	 * 
	 * 获取下载图片信息（jpg）
	 * 
	 * 
	 * 
	 * @param mediaId
	 * 
	 *                文件的id
	 * 
	 * @throws Exception
	 * 
	 */
	public static void saveImageToDisk(String mediaId) {
		InputStream inputStream = getInputStream(mediaId);
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream("D:\\share\\wx\\images\\test1.jpg");
			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void testLOCATION() {
		try {
//          String sg = SignKit.md5("/ws/geocoder/v1?key=CAQBZ-HJXWR-T6LW2-WYPN2-UD353-GTB6F&location=28.7033487,115.8660847XuiMdFcGWZMx60jhyxBOJxf7lVjQivEd");
			String sg = DigestUtils.md5Hex("/ws/geocoder/v1?key=CAQBZ-HJXWR-T6LW2-WYPN2-UD353-GTB6F&location=23.137709,113.404938XuiMdFcGWZMx60jhyxBOJxf7lVjQivEd");

			System.out.println(sg.toLowerCase());
			String params = "key=CAQBZ-HJXWR-T6LW2-WYPN2-UD353-GTB6F&location=23.137709,113.404938&sig=" + sg.toLowerCase();
			
			String result = HttpRequestUtil.doGet("https://apis.map.qq.com/ws/geocoder/v1?" + params);
			
			System.out.println(result);

			JSONObject jsonObject = JSONObject.parseObject(result);
			int status = (int) jsonObject.get("status");
			if (status == 0) {
				JSONObject resultJson = jsonObject.getJSONObject("result");
				JSONObject addrJson = resultJson.getJSONObject("address_component");
				String city = (String) addrJson.get("city");
				String district = (String) addrJson.get("district");
				System.out.println("city:" + city);
				System.out.println("district:" + district);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void testJWT() {
		Audience audience = new Audience();
		audience.setClientId("098f6bcd4621d373cade4e832627b4f6");
		audience.setBase64Secret("caidaokeji2020");
		audience.setName("restapiuser");
		audience.setExpiresSecond(86400000);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("userId", 77);
		try {
			String jwt = JwtTokenUtil.createJWT(jsonObj, audience);
			System.out.println("jwt:" + jwt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void test7() {
		String url = "https://restapi.amap.com/v3/config/district?keywords=%E5%8C%97%E4%BA%AC&subdistrict=3&key=68773403651236c9f6883c1e0c7043e9";

		String result = HttpRequestUtil.doGet(url);
		System.out.println(result);
	}
	
	public static void test8() {
		Date dt = new Date();
		// 上周累计分享数
		Date startDt = DateUtil.afterNDay(dt, -7);
		Date endDt = DateUtil.afterNDay(dt, -1);
		String startDate = DateUtil.formatDateByFormat(startDt, "yyyy-MM-dd");
		String endDate = DateUtil.formatDateByFormat(endDt, "yyyy-MM-dd");
		System.out.println(startDate);
		System.out.println(endDate);
	}

	public static void main(String[] args) {
//		String s = "asdsad%ccc%dddd";
//		String s1 = s.replace("%ccc%", "测试");
//		System.out.println(s1);

//		String s2 = "oSVdLv05qzm-bo26hSxuFBZV-EoU";
//		System.out.println(s2.length());

//		String accessToken = "27_EPriK7kyVaSmsg3jX9UotaA5IAdVa_Vxe3Qlcjx4LDBpopXa1wf58gImoetP6evILmlbOtb-u77ejwcDIGl-b943j3715SY9Z3EM57k1jT9rE3LwoyiHIKvb_mw9dT4SOfHqq_EYkpK5jQk-JENcAAAVQZ";
//		String openid = "oSVdLv05qzm-bo26hSxuFBZV-EoU";
//		System.out.println(WxUtil.GetUserInfo(accessToken, openid));

//		testGetPage3();
//		testReptile();
//		downloadPicture("https://mmbiz.qpic.cn/mmbiz_gif/VbrZzwePa2C0kApU1giazicUfqzMteJmMTTwB3rCqAMhY7U5hDQgZ8dOiar2dXsgtLhpEjDe7Yzhkgt8iaPA6QY7Og/640?wx_fmt=gif", "D:/share/wx/1.gif");
//		doUploadImageToAliyun();

//		testSendPassengerFlowMsg();
//		test1();
//		testFollowUpAccuratelyMsg();

//		testUserinfo();
//		testUserinfo2();
//		testUserinfoByAccessToken();
//		testGetToken();
//		testSendMessage();
//		testSendNewsMessage();
//		testOpenMemberMsg();
//		sendMemberOpenMsg("0", "oaxwFwre47GtQi6-i8y4bv5ajoqo", 1324, "2020-03-30");
//		testSendPreviewMassMessage();
//		testSendMassMessage();
//		testDeleteSendMassMessage();

//		testGetImageCount();
//		testGetImageList();
//		testSendImageMessage();

//		test2();
//		test3();
//		test4();
//		test5();
//		saveImageToDisk("E8rtp5UxAQ51IlwuQcvWCsnKueuKvT7qO-nMd15bAFPUc7r0NIxOymxITXx9SV4H");
//		test6();
//		testLOCATION();
		
//		testAccessToken();
//		testJWT();
//		test7();
		test8();
	}

}
