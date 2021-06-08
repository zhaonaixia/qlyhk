package com.cd.qlyhk.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:qlyhk_wx_api.properties")
public class QlyhkConfigProperties {

	@Value("${wx.appid}")
	public String APP_ID;

	@Value("${wx.secret}")
	public String SECRET;

	@Value("${wx.mchid}")
	public String MCH_ID;

	@Value("${wx.mchsecret}")
	public String MCH_SECRET;

	@Value("${wx.callback.url}")
	public String RESOUT_CALLBACK_URL;

	public String getAPP_ID() {
		return APP_ID;
	}

	public String getSECRET() {
		return SECRET;
	}

	public String getMCH_ID() {
		return MCH_ID;
	}

	public String getMCH_SECRET() {
		return MCH_SECRET;
	}

	public String getRESOUT_CALLBACK_URL() {
		return RESOUT_CALLBACK_URL;
	}

}
