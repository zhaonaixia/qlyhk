package com.cd.qlyhk.controller;

import java.util.Date;

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
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.dto.QlyRhArticleDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReqReptileArticleDetailDTO;
import com.cd.qlyhk.vo.ReqReptileArticleInfoDTO;

/**
 * 获客文章控制器
 * @author li
 *
 */
@Controller
@RequestMapping(value = "/openapi/articles")
public class ArticleController {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
	
	@Value("${wx.article.img.upload}")
	public String articleImgUpload;
	
	@Value("${wx.article.img.url}")
	public String articleImgUrl;
	
	@Resource(name = IArticleService.BEAN_ID)
	private IArticleService  articleService;
	
	@Resource(name = IUserService.BEAN_ID)
	private IUserService userService;
	
	@Resource(name = IDataCacheService.BEAN_ID)
	private IDataCacheService dataCacheService;
	
	/**
	 * 查询获客文章列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/getArticlesList.do")
    @ResponseBody
    public Response getArticlesList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhArticleDTO articleDTO) {
		logger.info("***************************查询获客文章列表********************************");
		logger.info("查询获客文章列表请求参数:"+ JSON.toJSONString(articleDTO));
        Response resp = Response.getDefaulTrueInstance();
        long time = System.currentTimeMillis();
        try {
        	resp = articleService.getArticlesList(articleDTO);
        	resp.setMessage("查询获客文章列表成功");
        	logger.info("***************************查询获客文章列表耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("查询获客文章列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询获客文章列表失败");
        }
        logger.info("查询获客文章列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }


    /**
	 *文章阅读情况。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryArticleReadCond.do")
	@ResponseBody
	public Response queryArticleReadCond(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
		logger.info("***************************文章阅读情况********************************");
		logger.info("文章阅读情况请求参数:{}", openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryArticleReadCond(openId);
		} catch (Exception e) {
			logger.error("获取文章阅读情况异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取文章阅读情况失败");
		}
		
		logger.info("获取文章阅读情况，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 *文章阅读情况详情。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryArticleReadCondDetails.do")
	@ResponseBody
	public Response queryArticleReadCondDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String shareId) {
		logger.info("***************************文章阅读情况详情********************************");
		logger.info("文章阅读情况详情请求参数:{}", shareId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryArticleReadCondDetails(shareId);
		} catch (Exception e) {
			logger.error("获取文章阅读情况详情异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取文章阅读情况详情失败");
		}
		
		logger.info("获取文章阅读情况详情，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 *访客情况。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRecordCondition.do")
	@ResponseBody
	public Response queryRecordCondition(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
		logger.info("***************************访客情况********************************");
		logger.info("访客情况请求参数:{}", openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryRecordCondition(openId);
		} catch (Exception e) {
			logger.error("获取访客情况异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取访客情况失败");
		}
		
		logger.info("获取访客情况，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 *访客情况详情。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRecordConditionDetails.do")
	@ResponseBody
	public Response queryRecordConditionDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId
			, @RequestParam String visitorId) {
		logger.info("***************************访客情况详情********************************");
		logger.info("访客情况详情请求参数:{},{}", openId, visitorId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryRecordConditionDetails(openId, visitorId);
		} catch (Exception e) {
			logger.error("获取访客情况详情异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取访客情况详情失败");
		}
		
		logger.info("获取访客情况详情，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 *访客情况文章详情。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRCArticleDetails.do")
	@ResponseBody
	public Response queryRCArticleDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId
			, @RequestParam String shareId) {
		logger.info("***************************访客情况文章详情********************************");
		logger.info("访客情况文章详情请求参数:{},{}", openId, shareId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryRecordConditionArticleDetails(openId, shareId);
		} catch (Exception e) {
			logger.error("获取访客情况文章详情异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取访客情况文章详情失败");
		}
		
		logger.info("获取访客情况文章详情，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 *文章详情。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getArticleInfo.do")
	@ResponseBody
	public Response getArticleInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String articleId) {
		logger.info("***************************文章详情********************************");
		logger.info("文章详情请求参数:{}", articleId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.getArticleInfo(articleId);
		} catch (Exception e) {
			logger.error("获取文章详情异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取文章详情失败");
		}
		
		logger.debug("获取文章详情，返回数据={}", JSON.toJSON(result));
	    return result;
	}
    
    /**
	 *获取文章内容。（html页面）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getReptileArticleInfo.do")
	@ResponseBody
	public Response getReptileArticleInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String articleId) {
		logger.info("***************************文章内容********************************");
		logger.info("文章内容请求参数:{}", articleId);
		Response result = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
			result = articleService.getArticleDetailInfo(articleId);
			logger.info("***************************查询文章内容耗时********************************" + (System.currentTimeMillis() - time));
		} catch (Exception e) {
			logger.error("获取文章内容异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取文章内容失败");
		}
		
		logger.debug("获取文章内容，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 获取我的文章
	 * @param request
	 * @param response
	 * @param openId
	 * @return
	 */
	@RequestMapping("/getMyArticle.do")
	@ResponseBody
	public Response getMyArticle(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId
			, @RequestParam String source) {
		logger.info("***************************我的文章********************************");
		logger.info("我的文章请求参数:{},{}", openId, source);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryMyArticle(openId, source);
		} catch (Exception e) {
			logger.error("获取我的文章异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取我的文章失败");
		}
		
		logger.info("获取我的文章，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 获取我编辑的文章
	 * @param request
	 * @param response
	 * @param openId
	 * @return
	 */
	@RequestMapping("/queryMyEditArticle.do")
	@ResponseBody
	public Response queryMyEditArticle(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
		logger.info("***************************我编辑的文章********************************");
		logger.info("我编辑的文章请求参数:{}", openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryMyEditArticle(openId);
		} catch (Exception e) {
			logger.error("获取我编辑的文章异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取我编辑的文章失败");
		}
		
		logger.info("获取我编辑的文章，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 修改文章内容
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/editArticleContent.do")
    @ResponseBody
    public Response editArticleContent(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqReptileArticleDetailDTO articleDetailDTO) {
		logger.info("***************************修改文章内容********************************");
		logger.info("修改文章内容请求参数:{},{},{}", articleDetailDTO.getUuid(), articleDetailDTO.getArticle_title(), articleDetailDTO.getPic_url());
        Response resp = Response.getDefaulTrueInstance();
        try {
//        	QlyRhUserVO userVO = dataCacheService.getQlyRhUserVO(articleDetailDTO.getOpenId());
        	articleDetailDTO.setModify_user(articleDetailDTO.getOpenId());
        	articleDetailDTO.setModify_datetime(new Date());
        	resp = articleService.saveEditorArticle(articleDetailDTO);
        } catch (Exception e) {
            logger.error("修改文章内容失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改文章内容失败");
        }
        logger.info("修改文章内容，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 修改文章信息
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 
    @RequestMapping(value = "/editArticleInfo.do")
    @ResponseBody
    public Response editArticleInfo(HttpServletRequest request, HttpServletResponse response, ReqReptileArticleInfoDTO articleInfoDTO) {
		logger.debug("***************************修改文章信息********************************");
		logger.debug("修改文章信息请求参数:{}", articleInfoDTO.getUuid());
        Response resp = Response.getDefaulTrueInstance();
        try {
        	articleInfoDTO.setModify_user(articleInfoDTO.getOpenId());
        	articleInfoDTO.setFilePath(articleImgUpload);
        	articleInfoDTO.setFileUrlPath(articleImgUrl);
        	resp = articleService.saveEditorArticleInfo(articleInfoDTO);
        } catch (Exception e) {
            logger.error("修改文章信息失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("修改文章信息失败");
        }
        logger.debug("修改文章信息，返回数据={}", JSON.toJSON(resp));
        return resp;
    }*/
    
    /**
	 *删除文章。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delReptileArticle.do")
	@ResponseBody
	public Response delReptileArticle(HttpServletRequest request, HttpServletResponse response, @RequestParam String articleId) {
		logger.info("***************************删除文章********************************");
		logger.info("删除文章请求参数:{}", articleId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.delArticle(articleId);
		} catch (Exception e) {
			logger.error("删除文章异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("删除文章失败");
		}
		
		logger.debug("删除文章，返回数据={}", JSON.toJSON(result));
	    return result;
	}
    
}
