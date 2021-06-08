package com.cd.qlyhk.admin.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.dto.QlyRhAdminArticleDTO;
import com.cd.qlyhk.dto.QlyRhAdminUserDTO;
import com.cd.qlyhk.dto.QlyRhSysAreaDTO;
import com.cd.qlyhk.dto.QlyRhSysCategoryDTO;
import com.cd.qlyhk.dto.QlyRhUserDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IStatisticsService;
import com.cd.qlyhk.vo.QlyRhSysAreaVO;

@Controller
@RequestMapping(value = "/admin/common")
public class PubCommonManageController {
	private static final Logger logger = LoggerFactory.getLogger(PubCommonManageController.class);
	
	@Resource(name = IPubCommonService.BEAN_ID)
	private IPubCommonService  pubCommonService;
	
	@Resource(name = IStatisticsService.BEAN_ID)
	private IStatisticsService  statisticsService;
	
	@Value("${wx.article.img.upload}")
	public String imgUploadUrl;
	
	@Value("${wx.article.img.url}")
	public String imgUrl;
	
	/**
	 * 获取删除类别下的文章数
	 * @param request
	 * @param category_id
	 * @return
	 */
	@RequestMapping(value = "/selectArticleCategoryCount.do")
    @ResponseBody
    public Response selectArticleCategoryCount(HttpServletRequest request, HttpServletResponse response, @RequestParam int category_id) {
		logger.debug("***************************获取删除类别下的文章数********************************");
		logger.debug("获取删除类别下的文章数请求参数:{}", category_id);
        Response result = Response.getDefaulTrueInstance();
        try {
        	int count = pubCommonService.selectArticleCategoryCount(category_id);
        	Map<String, Object> resultMap = new HashMap<String, Object>();
        	resultMap.put("articlesCount", count);
        	result.setData(resultMap);
        } catch (Exception e) {
            logger.error("获取删除类别下的文章数失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取删除类别下的文章数失败");
        }
        logger.debug("获取删除类别下的文章数，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 删除类别列表
	 * @param request
	 * @param category_id
	 * @return
	 */
	@RequestMapping(value = "/deleteSysCategory.do")
    @ResponseBody
    public Response deleteSysCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam int category_id) {
		logger.debug("***************************删除类别列表********************************");
		logger.debug("删除类别列表请求参数:{}", category_id);
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = pubCommonService.deleteSysCategory(category_id);
        } catch (Exception e) {
            logger.error("删除类别列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("删除类别列表失败");
        }
        logger.debug("删除类别列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 添加类别列表
	 * @param request
	 * @param categoryList
	 * @return
	 */
	@RequestMapping(value = "/addSysCategoryList.do")
    @ResponseBody
    public Response addSysCategoryList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhSysCategoryDTO sysCategoryDTO) {
		logger.debug("***************************添加类别列表********************************");
		logger.debug("添加类别列表请求参数:{}", JSON.toJSONString(sysCategoryDTO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = pubCommonService.addSysCategoryList(sysCategoryDTO);
        } catch (Exception e) {
            logger.error("添加类别列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("添加类别列表失败");
        }
        logger.debug("添加类别列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 更新类别列表
	 * @param request
	 * @param categoryList
	 * @return
	 */
	@RequestMapping(value = "/updateSysCategory.do")
    @ResponseBody
    public Response updateSysCategory(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhSysCategoryDTO sysCategoryDTO) {
		logger.debug("***************************更新类别列表********************************");
		logger.debug("更新类别列表请求参数:{}", JSON.toJSONString(sysCategoryDTO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = pubCommonService.updateSysCategory(sysCategoryDTO);
        } catch (Exception e) {
            logger.error("更新类别列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("更新类别列表失败");
        }
        logger.debug("更新类别列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 查询月度分享时段分析列表
	 * @param request
	 * @param response
	 * @param yearPeriod
	 * @return
	 */
	@RequestMapping(value = "/queryMonthlyShareList.do")
    @ResponseBody
    public Response queryMonthlyShareList(HttpServletRequest request, HttpServletResponse response, @RequestParam int yearPeriodStart, @RequestParam int yearPeriodEnd) {
		logger.debug("***************************查询月度分享时段分析列表********************************");
		logger.debug("查询月度分享时段分析列表请求参数:{},{}", yearPeriodStart, yearPeriodEnd);
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.queryMonthlyShareList(yearPeriodStart, yearPeriodEnd);
        	result.setMessage("查询月度分享时段分析列表成功");
        } catch (Exception e) {
            logger.error("查询月度分享时段分析列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("查询月度分享时段分析列表失败");
        }
        logger.debug("查询月度分享时段分析列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 查询月度阅读时段分析列表
	 * @param request
	 * @param response
	 * @param yearPeriod
	 * @return
	 */
	@RequestMapping(value = "/queryMonthlyReadList.do")
    @ResponseBody
    public Response queryMonthlyReadList(HttpServletRequest request, HttpServletResponse response, @RequestParam int yearPeriodStart, @RequestParam int yearPeriodEnd) {
		logger.debug("***************************查询月度阅读时段分析列表********************************");
		logger.debug("查询月度阅读时段分析列表请求参数:{},{}", yearPeriodStart, yearPeriodEnd);
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.queryMonthlyReadList(yearPeriodStart, yearPeriodEnd);
        	result.setMessage("查询月度阅读时段分析列表成功");
        } catch (Exception e) {
            logger.error("查询月度阅读时段分析列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("查询月度阅读时段分析列表失败");
        }
        logger.debug("查询月度阅读时段分析列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 获取月度TOP10列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getMonthTOPList.do")
    @ResponseBody
    public Response getMonthTOPList(HttpServletRequest request, HttpServletResponse response, @RequestParam String month) {
		logger.debug("***************************获取月度TOP10列表********************************");
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.getMonthTOPList(month);
        	result.setMessage("获取月度TOP10列表成功");
        } catch (Exception e) {
            logger.error("获取月度TOP10列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取月度TOP10列表失败");
        }
        logger.debug("获取月度TOP10列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 获取更多热点文章列表
	 * @param request
	 * @param response
	 * @param adminArticleDTO
	 * @return
	 */
	@RequestMapping(value = "/getMoreHotArticlesList.do")
    @ResponseBody
    public Response getMoreHotArticlesList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhAdminArticleDTO articleDTO) {
		logger.debug("***************************获取更多热点文章列表********************************");
		logger.debug("获取更多热点文章列表请求参数:"+ JSON.toJSONString(articleDTO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.getMoreHotArticlesList(articleDTO);
        	result.setMessage("获取更多热点文章列表成功");
        } catch (Exception e) {
            logger.error("获取更多热点文章列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取更多热点文章列表失败");
        }
        logger.debug("获取更多热点文章列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 获取更多分享达人列表
	 * @param request
	 * @param response
	 * @param adminArticleDTO
	 * @return
	 */
	@RequestMapping(value = "/getMoreShareTalentsList.do")
    @ResponseBody
    public Response getMoreShareTalentsList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhAdminUserDTO userDTO) {
		logger.debug("***************************获取更多分享达人列表********************************");
		logger.debug("获取更多分享达人列表请求参数:"+ JSON.toJSONString(userDTO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.getMoreShareTalentsList(userDTO);
        	result.setMessage("获取更多分享达人列表成功");
        } catch (Exception e) {
            logger.error("获取更多分享达人列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取更多分享达人列表失败");
        }
        logger.debug("获取更多分享达人列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 获取更多阅读达人列表
	 * @param request
	 * @param response
	 * @param userDTO
	 * @return
	 */
	@RequestMapping(value = "/getMoreReadTalentsList.do")
    @ResponseBody
    public Response getMoreReadTalentsList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhAdminUserDTO userDTO) {
		logger.debug("***************************获取更多阅读达人列表********************************");
		logger.debug("获取更多阅读达人列表请求参数:"+ JSON.toJSONString(userDTO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.getMoreReadTalentsList(userDTO);
        	result.setMessage("获取更多阅读达人列表成功");
        } catch (Exception e) {
            logger.error("获取更多阅读达人列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取更多阅读达人列表失败");
        }
        logger.debug("获取更多阅读达人列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 获取首页统计数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getUserAnalysisList.do")
    @ResponseBody
    public Response getUserAnalysisList(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("***************************获取首页统计数据********************************");
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.getUserAnalysisList();
        	result.setMessage("获取首页统计数据成功");
        } catch (Exception e) {
            logger.error("获取首页统计数据失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取首页统计数据失败");
        }
        logger.debug("获取首页统计数据，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 获取注册用户城市分布列表
	 * @param request
	 * @param response
	 * @param areaVO
	 * @return
	 */
	@RequestMapping(value = "/getUserAreaDistributionList.do")
    @ResponseBody
    public Response getUserAreaDistributionList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhSysAreaDTO areaVO) {
		logger.debug("***************************获取注册用户城市分布列表********************************");
		logger.debug("获取注册用户城市分布列表请求参数:" + JSON.toJSONString(areaVO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.getUserAreaDistributionList(areaVO);
        	result.setMessage("获取注册用户城市分布列表成功");
        } catch (Exception e) {
            logger.error("获取注册用户城市分布列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("获取注册用户城市分布列表失败");
        }
        logger.debug("获取注册用户城市分布列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 上传图片并返回图片地址
	 * @param request
	 * @param response
	 * @param userDTO
	 * @return
	 */
	@RequestMapping("/uploadImg.do")
	@ResponseBody
	public Response uploadImg(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile image) {
		logger.debug("***************************上传图片并返回图片地址********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
            //使用UUID给图片重命名，并去掉四个“-”
            String imgName = UUID.randomUUID().toString().replaceAll("-", "");
            if(image != null) {
            	double fileSize = image.getSize() / 1048576;// M
                if(fileSize > 5) {
                	logger.error("***************图片大小超过5M****************" + fileSize);
                	result.setCode("1");
                	result.setSuccess(false);
                	result.setMessage("图片大小超过5M");
                    return result;
                }
                //获取文件的扩展名
                String imgExt = FilenameUtils.getExtension(image.getOriginalFilename());
                //根据日期来创建对应文件夹
                String datePath = "images/article/" + new SimpleDateFormat("yyyyMMdd/").format(new Date());
                String path = imgUploadUrl + datePath;
                //String path = "D:/" + datePath;
                //如果不存在,创建文件夹
                File f = new File(path);
                if(!f.exists()){
                    f.mkdirs();
                }
                //以绝对路径保存重名命后的图片
                String picName = path + imgName + "." + imgExt;
                image.transferTo(new File(picName));
                //返回图片的存储路径
                String picUrl = imgUrl + datePath + imgName + "." + imgExt;
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("picUrl", picUrl);
                result.setData(resultMap);
                result.setMessage("上传图片并返回图片地址成功");
            }   
		} catch (Exception e) {
			logger.error("上传图片并返回图片地址失败，错误原因：" + e.getMessage());
			result.setSuccess(false);
			result.setCode("1");
			result.setMessage("上传图片并返回图片地址失败");
		}
		logger.debug("上传图片并返回图片地址，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 查询月度报表情况列表
	 * @param request
	 * @param response
	 * @param year
	 * @return
	 */
	@RequestMapping(value = "/queryMonthlyStatistics.do")
    @ResponseBody
    public Response queryMonthlyStatistics(HttpServletRequest request, HttpServletResponse response, @RequestParam String year) {
		logger.debug("***************************查询月度报表情况列表********************************");
		logger.debug("*查询月度报表情况列表请求参数:" + year);
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = statisticsService.queryMonthlyStatisticsList(year);
        } catch (Exception e) {
            logger.error("*查询月度报表情况列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("*查询月度报表情况列表失败");
        }
        logger.debug("查询月度报表情况列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
}
