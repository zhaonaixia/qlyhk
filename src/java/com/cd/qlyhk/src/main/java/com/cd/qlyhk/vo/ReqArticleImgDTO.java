package com.cd.qlyhk.vo;

/**
 * 修改文章图片请求DTO
 * 
 * @author sailor
 *
 */
public class ReqArticleImgDTO {

	// 图片ID
	private String[] ids;

	private String filePath;
	private String fileUrlPath;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileUrlPath() {
		return fileUrlPath;
	}

	public void setFileUrlPath(String fileUrlPath) {
		this.fileUrlPath = fileUrlPath;
	}

}
