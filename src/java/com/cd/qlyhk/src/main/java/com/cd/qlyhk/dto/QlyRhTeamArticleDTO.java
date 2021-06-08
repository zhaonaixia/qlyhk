package com.cd.qlyhk.dto;

public class QlyRhTeamArticleDTO {

	// 队长的openId
	private String openId;
	// 队员的openId
	private String teamMemberOpenId;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getTeamMemberOpenId() {
		return teamMemberOpenId;
	}

	public void setTeamMemberOpenId(String teamMemberOpenId) {
		this.teamMemberOpenId = teamMemberOpenId;
	}

}
