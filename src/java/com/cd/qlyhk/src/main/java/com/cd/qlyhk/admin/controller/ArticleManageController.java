package com.cd.qlyhk.admin.controller;

import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.constants.ArticleConstants;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.dto.QlyRhAdminArticleDTO;
import com.cd.qlyhk.dto.QlyRhArticleDTO;
import com.cd.qlyhk.dto.ReqArticleUseLogDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.vo.QlyRhArticleUselogVO;
import com.cd.qlyhk.vo.QlyRhReptileArticleVO;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReqArticleShareReadDTO;
import com.cd.qlyhk.vo.ReqReptileArticleDetailDTO;
import com.cd.qlyhk.vo.ReqReptileArticleInfoDTO;

/**
 * 文章管理控制器
 * @author sailor_jsb
 *
 */
@Controller
@RequestMapping(value = "/admin/articles")
public class ArticleManageController {

	private static final Logger logger = LoggerFactory.getLogger(ArticleManageController.class);
	
	@Resource(name = IArticleService.BEAN_ID)
	private IArticleService  articleService;
	
	@Resource(name = IUserService.BEAN_ID)
	private IUserService  userService;
	
	@Value("${wx.article.img.upload}")
	public String articleImgUpload;
	
	@Value("${wx.article.img.url}")
	public String articleImgUrl;
	
	/**
	 * 查询文章列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/getArticlesList.do")
    @ResponseBody
    public Response getArticlesList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhAdminArticleDTO articleDTO) {
		logger.debug("***************************查询文章列表********************************");
		logger.debug("查询文章列表请求参数:"+ JSON.toJSONString(articleDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = articleService.queryAdminArticlesList(articleDTO);
        	resp.setMessage("查询文章列表成功");
        } catch (Exception e) {
            logger.error("查询文章列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询文章列表失败");
        }
        logger.debug("查询文章列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询文章详情
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/getArticleInfo.do")
    @ResponseBody
    public Response getArticleInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String uuid) {
		logger.debug("***************************查询文章详情********************************");
		logger.debug("查询文章详情请求参数:{}", uuid);
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = articleService.queryAdminArticleInfo(uuid);
        } catch (Exception e) {
            logger.error("查询文章详情失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询文章详情失败");
        }
        logger.debug("查询文章详情，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 修改文章置顶标识
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/editArticleTop.do")
    @ResponseBody
    public Response editArticleTop(HttpServletRequest request, HttpServletResponse response, @RequestParam String uuid
    		, @RequestParam String topFlag, @RequestParam(required=false) String categoryId) {
		logger.debug("***************************修改文章置顶标识********************************");
		logger.debug("修改文章置顶标识请求参数:{},{},{}", uuid, topFlag, categoryId);
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	articleService.updateArticleTop(uuid, topFlag, categoryId, userVO.getUser_name());
        	resp.setMessage("修改文章置顶标识成功");
        } catch (Exception e) {
            logger.error("修改文章置顶标识失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改文章置顶标识失败");
        }
        logger.debug("修改文章置顶标识，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 修改文章采用状态
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/editArticleStatus.do")
    @ResponseBody
    public Response editArticleStatus(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqArticleUseLogDTO articleUseLogDTO) {
		logger.debug("***************************修改文章采用状态********************************");
		logger.debug("修改文章采用状态请求参数:{}", JSON.toJSONString(articleUseLogDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	QlyRhReptileArticleVO articleVO = new QlyRhReptileArticleVO();
        	articleVO.setUuid(articleUseLogDTO.getUuid());
        	if("Y".equals(articleUseLogDTO.getStatus())) {
        		articleVO.setAudit_status(ArticleConstants.ARTICLE_STATUS_SUCCESS);
        	} else {
        		articleVO.setAudit_status(ArticleConstants.ARTICLE_STATUS_FAIL);
        	}
        	articleVO.setModify_user(userVO.getUser_name());
        	articleVO.setAudit_user(userVO.getUser_name());
        	articleService.updateReptileArticle(articleVO);
        	
        	// 只有采用的文章才加入到列表里面
        	if(ArticleConstants.ARTICLE_STATUS_SUCCESS.equals(articleVO.getAudit_status())) {
        		QlyRhArticleUselogVO useLogVO = new QlyRhArticleUselogVO();
            	useLogVO.setArticle_uuid(articleUseLogDTO.getUuid());
            	useLogVO.setArticle_title(articleUseLogDTO.getArticle_title());
            	useLogVO.setCreate_user(userVO.getUser_name());
            	articleService.insertArticleUselog(useLogVO);
        	}
        	
        	resp.setMessage("修改文章采用状态成功");
        } catch (Exception e) {
            logger.error("修改文章采用状态失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改文章采用状态失败");
        }
        logger.debug("修改文章采用状态，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询文章所属类别列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/getArticleCategorys.do")
    @ResponseBody
    public Response getArticleCategorys(HttpServletRequest request, HttpServletResponse response, @RequestParam String uuid) {
		logger.debug("***************************查询文章所属类别列表********************************");
		logger.debug("查询文章所属类别列表请求参数:{}", uuid);
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = articleService.queryArticleCategoryList(uuid);
        	resp.setMessage("查询文章所属类别列表成功");
        } catch (Exception e) {
            logger.error("查询文章所属类别列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询文章所属类别列表失败");
        }
        logger.debug("查询文章所属类别列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 修改文章所属类别
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/editArticleCategory.do")
    @ResponseBody
    public Response editArticleCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam String uuid
    		, @RequestParam String categoryId, @RequestParam(required=false) String mpflag) {
		logger.debug("***************************修改文章所属类别********************************");
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	String userName = userVO.getUser_name();
    		logger.debug("修改文章所属类别请求参数:{},{},{}", uuid, categoryId, mpflag);
    		
        	articleService.updateReptileArticleCategory(uuid, categoryId, mpflag, userName);
        	resp.setMessage("修改文章所属类别成功");
        } catch (Exception e) {
            logger.error("修改文章所属类别失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改文章所属类别失败");
        }
        logger.debug("修改文章所属类别，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 修改文章内容
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/editArticleDetail.do")
    @ResponseBody
    public Response editArticleDetail(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqReptileArticleDetailDTO articleDetailDTO) {
		logger.debug("***************************修改文章内容********************************");
		logger.debug("修改文章内容请求参数:{}", articleDetailDTO.getUuid());
        Response resp = Response.getDefaulTrueInstance();
        try {
//        	String token = request.getHeader("token");
//        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
//        	articleDetailDTO.setModify_user(userVO.getUser_name());
        	articleService.updateReptileArticleDetail(articleDetailDTO);
        	resp.setMessage("修改文章内容成功");
        } catch (Exception e) {
            logger.error("修改文章内容失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改文章内容失败");
        }
        logger.debug("修改文章内容，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询文章分享及阅读情况列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/getArticlesShareAndReadList.do")
    @ResponseBody
    public Response getArticlesShareAndReadList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhAdminArticleDTO articleDTO) {
		logger.debug("***************************查询文章分享及阅读情况列表********************************");
		logger.debug("查询文章分享及阅读情况列表请求参数:"+ JSON.toJSONString(articleDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = articleService.queryAdminArticlesShareAndReadList(articleDTO);
        	resp.setMessage("查询文章分享及阅读情况列表成功");
        } catch (Exception e) {
            logger.error("查询文章分享及阅读情况列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询文章分享及阅读情况列表失败");
        }
        logger.debug("查询文章分享及阅读情况列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询分享过文章的用户列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryArticlesShareUserList.do")
    @ResponseBody
    public Response queryArticlesShareUserList(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqArticleShareReadDTO shareReadDTO) {
		logger.debug("***************************查询分享过文章的用户列表********************************");
		logger.debug("查询分享过文章的用户列表请求参数:{}", JSON.toJSONString(shareReadDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = articleService.queryAdminArticlesShareUserList(shareReadDTO);
        	resp.setMessage("查询分享过文章的用户列表成功");
        } catch (Exception e) {
            logger.error("查询分享过文章的用户列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询分享过文章的用户列表失败");
        }
        logger.debug("查询分享过文章的用户列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询阅读过文章的用户列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryArticlesReadUserList.do")
    @ResponseBody
    public Response queryArticlesReadUserList(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqArticleShareReadDTO shareReadDTO) {
		logger.debug("***************************查询阅读过文章的用户列表********************************");
		logger.debug("查询阅读过文章的用户列表请求参数:{}", JSON.toJSONString(shareReadDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = articleService.queryAdminArticlesReadUserList(shareReadDTO);
        	resp.setMessage("查询阅读过文章的用户列表成功");
        } catch (Exception e) {
            logger.error("查询阅读过文章的用户列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询阅读过文章的用户列表失败");
        }
        logger.debug("查询阅读过文章的用户列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 *访客情况（后台管理）。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRecordCondition.do")
	@ResponseBody
	public Response queryRecordCondition(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
		logger.debug("***************************访客情况（后台管理）********************************");
		logger.debug("访客情况（后台管理）请求参数:{}", openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryRecordCondition(openId);
		} catch (Exception e) {
			logger.error("获取访客情况（后台管理）异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取访客情况（后台管理）失败");
		}
		
		logger.debug("获取访客情况（后台管理），返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 *访客情况详情（后台管理）。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRecordConditionDetails.do")
	@ResponseBody
	public Response queryRecordConditionDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId
			, @RequestParam String visitorId) {
		logger.debug("***************************访客情况详情（后台管理）********************************");
		logger.debug("访客情况详情（后台管理）请求参数:{},{}", openId, visitorId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryRecordConditionDetails(openId, visitorId);
		} catch (Exception e) {
			logger.error("获取访客情况详情（后台管理）异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取访客情况详情（后台管理）失败");
		}
		
		logger.debug("获取访客情况详情（后台管理），返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 修改文章信息
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/editArticleInfo.do")
    @ResponseBody
    public Response editArticleInfo(HttpServletRequest request, HttpServletResponse response, ReqReptileArticleInfoDTO articleInfoDTO) {
		logger.debug("***************************修改文章信息********************************");
		logger.debug("修改文章信息请求参数:{}", articleInfoDTO.getUuid());
        Response resp = Response.getDefaulTrueInstance();
        try {
//        	String token = request.getHeader("token");
//        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
//        	articleInfoDTO.setModify_user(userVO.getUser_name());
        	articleInfoDTO.setFilePath(articleImgUpload);
        	articleInfoDTO.setFileUrlPath(articleImgUrl);
        	resp = articleService.updateArticleInfo(articleInfoDTO);
        } catch (Exception e) {
            logger.error("修改文章信息失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改文章信息失败");
        }
        logger.debug("修改文章信息，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 创建文章
	 * @param request
	 * @param response
	 * @param articleInfoDTO
	 * @return
	 */
    @RequestMapping(value = "/newArticle.do")
    @ResponseBody
    public Response newArticle(HttpServletRequest request, HttpServletResponse response, ReqReptileArticleInfoDTO articleInfoDTO) {
		logger.debug("***************************创建文章********************************");
//		logger.debug("创建文章请求参数:"+ JSON.toJSONString(articleInfoDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	articleInfoDTO.setModify_user(userVO.getUser_name());
        	articleInfoDTO.setFilePath(articleImgUpload);
        	articleInfoDTO.setFileUrlPath(articleImgUrl);
        	
        	resp = articleService.addArticle(articleInfoDTO);
        } catch (Exception e) {
            logger.error("创建文章失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("创建文章失败");
        }
        return resp;
    }
}
