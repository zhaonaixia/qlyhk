package com.cd.qlyhk.dto;

import java.util.List;
import java.util.Map;

import com.cd.qlyhk.vo.QlyRhMessageRemindSetVO;

public class QlyRhMessageRemindSetDTO extends QlyRhMessageRemindSetVO {
	// 用户的唯一标识
	String userId;
	//热门文章推送设置
	List<PushTypeDTO> push_settings;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<PushTypeDTO> getPush_settings() {
		return push_settings;
	}

	public void setPush_settings(List<PushTypeDTO> push_settings) {
		this.push_settings = push_settings;
	}
}
