package com.cd.qlyhk.controller;

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
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.dto.QlyRhCompanyDTO;
import com.cd.qlyhk.dto.QlyRhReleaseArticleDTO;
import com.cd.qlyhk.dto.QlyRhTeamArticleDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IArticleService;
import com.cd.qlyhk.service.ICompanyService;

/**
 * 公司控制器
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/openapi/company")
public class CompanyController {

	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	
	@Value("${wx.article.img.upload}")
	public String articleImgUpload;
	
	@Value("${wx.article.img.url}")
	public String articleImgUrl;
	
	@Value("${wx.appid}")
	public String appid;
	
	@Resource(name = ICompanyService.BEAN_ID)
	private ICompanyService   companyService;
	
	@Resource(name = IArticleService.BEAN_ID)
	private IArticleService  articleService;
	
	/**
	 * 创建公司
	 * @param request
	 * @param response
	 * @param companyVO
	 * @return
	 */
	@RequestMapping("/createCompany.do")
	@ResponseBody
	public Response createCompany(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************创建公司信息********************************");
		logger.info("创建公司信息请求参数：{}", JSON.toJSONString(companyDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = companyService.createCompany(companyDTO);
		} catch (Exception e) {
			logger.error("创建公司信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("创建公司信息失败");
		}
		
		logger.info("创建公司信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 加入公司
	 * @param request
	 * @param response
	 * @param companyVO
	 * @return
	 */
	@RequestMapping("/joinCompany.do")
	@ResponseBody
	public Response joinCompany(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************加入公司信息********************************");
		logger.info("加入公司信息请求参数：{}", JSON.toJSONString(companyDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = companyService.joinCompany(companyDTO);
		} catch (Exception e) {
			logger.error("加入公司信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("加入公司信息失败");
		}
		
		logger.info("加入公司信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 查询公司信息
	 * @param request
	 * @param response
	 * @param companyVO
	 * @return
	 */
	@RequestMapping("/getCompanyInfo.do")
	@ResponseBody
	public Response getCompanyInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************查询公司信息********************************");
		logger.info("查询公司信息请求参数：{}", JSON.toJSONString(companyDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = companyService.getCompanyInfo(companyDTO);
		} catch (Exception e) {
			logger.error("查询公司信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询公司信息失败");
		}
		
		logger.info("查询公司信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 修改公司信息
	 * @param request
	 * @param response
	 * @param companyVO
	 * @return
	 */
	@RequestMapping("/editCompany.do")
	@ResponseBody
	public Response editCompany(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************修改公司信息********************************");
		logger.info("修改公司信息请求参数：{}", JSON.toJSONString(companyDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = companyService.editCompany(companyDTO);
		} catch (Exception e) {
			logger.error("修改公司信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("修改公司信息失败");
		}
		
		logger.info("修改公司信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 发布文章
	 * @param request
	 * @param response
	 * @param companyVO
	 * @return
	 */
	@RequestMapping("/releaseArticle.do")
	@ResponseBody
	public Response releaseArticle(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhReleaseArticleDTO releaseArticleDTO) {
		logger.info("***************************发布文章信息********************************");
		logger.info("发布文章信息请求参数：{}", JSON.toJSONString(releaseArticleDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = companyService.addReleaseArticle(releaseArticleDTO, articleImgUpload, articleImgUrl);
		} catch (Exception e) {
			logger.error("发布文章信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("发布文章信息失败");
		}
		
		logger.info("发布文章信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 查询公司文章列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryReleaseArticle.do")
    @ResponseBody
    public Response queryReleaseArticle(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhReleaseArticleDTO releaseArticleDTO) {
		logger.info("***************************查询公司文章列表********************************");
		logger.info("查询公司文章列表请求参数:"+ JSON.toJSONString(releaseArticleDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = companyService.queryReleaseArticleList(releaseArticleDTO);
        } catch (Exception e) {
            logger.error("查询公司文章列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询公司文章列表失败");
        }
        logger.info("查询公司文章列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 成员管理
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryCompanyStaffShare.do")
    @ResponseBody
    public Response queryCompanyStaff(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************查询成员管理列表********************************");
		logger.info("查询成员管理列表请求参数:"+ JSON.toJSONString(companyDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	companyDTO.setAppId(appid);
        	resp = companyService.queryCompanyStaffShareList(companyDTO);
        } catch (Exception e) {
            logger.error("查询成员管理列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询成员管理列表失败");
        }
        logger.info("查询成员管理列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 获取公司下的员工列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryCompanyAllUser.do")
    @ResponseBody
    public Response queryCompanyAllUser(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************获取公司下的员工列表********************************");
		logger.info("获取公司下的员工列表请求参数:"+ JSON.toJSONString(companyDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = companyService.queryCompanyAllUser(companyDTO);
        } catch (Exception e) {
            logger.error("获取公司下的员工列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("获取公司下的员工列表失败");
        }
        logger.info("获取公司下的员工列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 公司转让
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/doTeamTransfer.do")
    @ResponseBody
    public Response doTeamTransfer(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************公司转让********************************");
		logger.info("公司转让请求参数:"+ JSON.toJSONString(companyDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
//        	companyService.teamTransfer(companyDTO);
//        	resp.setMessage("公司转让成功");
        	resp = companyService.teamTransfer(companyDTO);
        } catch (Exception e) {
            logger.error("公司转让失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("公司转让失败");
        }
        logger.info("公司转让，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 公司文章阅读详情列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryTeamArticleReadDetails.do")
    @ResponseBody
    public Response queryTeamArticleReadDetails(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************公司文章阅读详情列表********************************");
		logger.info("公司文章阅读详情列表请求参数:"+ JSON.toJSONString(companyDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = companyService.queryTeamArticleReadDetails(companyDTO);
        } catch (Exception e) {
            logger.error("公司文章阅读详情列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("公司文章阅读详情列表失败");
        }
        logger.info("公司文章阅读详情列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 公司文章分享详情列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryTeamArticleShareDetails.do")
    @ResponseBody
    public Response queryTeamArticleShareDetails(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************公司文章分享详情列表********************************");
		logger.info("公司文章分享详情列表请求参数:"+ JSON.toJSONString(companyDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = companyService.queryTeamArticleShareDetails(companyDTO);
        } catch (Exception e) {
            logger.error("公司文章分享详情列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("公司文章分享详情列表失败");
        }
        logger.info("公司文章分享详情列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 队员分享详情列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryTeamArticleReadCond.do")
    @ResponseBody
    public Response queryTeamArticleReadCond(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhTeamArticleDTO teamArticleDTO) {
		logger.info("***************************队员分享详情列表********************************");
		logger.info("队员分享详情列表请求参数:"+ JSON.toJSONString(teamArticleDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = companyService.queryTeamArticleReadCond(teamArticleDTO);
        } catch (Exception e) {
            logger.error("队员分享详情列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("队员分享详情列表失败");
        }
        logger.info("队员分享详情列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 *队员分享效果。
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryTeamArticleReadCondDetails.do")
	@ResponseBody
	public Response queryArticleReadCondDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String shareId) {
		logger.info("***************************队员分享效果********************************");
		logger.info("队员分享效果请求参数:{}", shareId);
		Response result = Response.getDefaulTrueInstance();
		try {
			result = articleService.queryArticleReadCondDetails(shareId);
		} catch (Exception e) {
			logger.error("获取队员分享效果异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取队员分享效果失败");
		}
		
		logger.info("获取队员分享效果，返回数据={}", JSON.toJSON(result));
	    return result;
	}
    
	/**
	 * 获取公司列表
	 * @param request
	 * @param response
	 * @param companyVO
	 * @return
	 */
	@RequestMapping("/queryCompanys.do")
	@ResponseBody
	public Response queryCompanys(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************获取公司列表信息********************************");
		logger.info("获取公司列表信息请求参数：{}", JSON.toJSONString(companyDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = companyService.queryCompanys(companyDTO);
		} catch (Exception e) {
			logger.error("获取公司列表信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("获取公司列表信息失败");
		}
		
		logger.info("获取公司列表信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 删除公司成员
	 * @param request
	 * @param response
	 * @param companyVO
	 * @return
	 */
	@RequestMapping("/delCompanyUser.do")
	@ResponseBody
	public Response delCompanyUser(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************删除公司成员********************************");
		logger.info("删除公司成员请求参数：{}", JSON.toJSONString(companyDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = companyService.delCompanyUser(companyDTO);
		} catch (Exception e) {
			logger.error("删除公司成员异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("删除公司成员失败");
		}
		
		logger.info("删除公司成员，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 公司成员信息
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/getCompanyStaffInfo.do")
    @ResponseBody
    public Response getCompanyStaffInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhCompanyDTO companyDTO) {
		logger.info("***************************查询成员信息********************************");
		logger.info("查询成员信息请求参数:"+ JSON.toJSONString(companyDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = companyService.getCompanyStaffInfo(companyDTO);
        } catch (Exception e) {
            logger.error("查询成员信息失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询成员信息失败");
        }
        logger.info("查询成员信息，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
}
