package com.cd.qlyhk.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.dto.QlyRhMorningPaperArticleDTO;
import com.cd.qlyhk.dto.QlyRhReptileArticleDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IMorningPaperService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.vo.QlyRhUserVO;

@Controller
@RequestMapping(value = "/admin/morningpaper")
public class MorningPaperController {
	private static final Logger logger = LoggerFactory.getLogger(MorningPaperController.class);
	
	@Resource(name = IMorningPaperService.BEAN_ID)
	private IMorningPaperService  morningPaperService;
	
	@Resource(name = IUserService.BEAN_ID)
	private IUserService  userService;
	
	/**
	 * 获取财税早报清单
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
	@RequestMapping(value = "/getMorningpaperArticles.do")
    @ResponseBody
    public Response getMorningpaperArticles(HttpServletRequest request, HttpServletResponse response, @RequestParam String mp_date) {
		logger.debug("***************************获取财税早报清单********************************");
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = morningPaperService.getMorningpaperArticles(mp_date);
        	result.setMessage("获取财税早报清单成功");
        } catch (Exception e) {
            logger.error("获取财税早报清单失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode("1");
            result.setMessage("获取财税早报清单失败");
        }
        logger.debug("获取财税早报清单，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 获取文章选择列表
	 * @param request
	 * @param response
	 * @param reptileArticleDTO
	 * @return
	 */
	@RequestMapping(value = "/getArticlesList.do")
	@ResponseBody
    public Response getArticlesList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhReptileArticleDTO reptileArticleDTO) {
		logger.debug("***************************获取文章选择列表********************************");
		logger.debug("获取文章选择列表请求参数:"+ JSON.toJSONString(reptileArticleDTO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = morningPaperService.getArticlesList(reptileArticleDTO);
        	result.setMessage("获取文章选择列表成功");
        } catch (Exception e) {
            logger.error("获取文章选择列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode("1");
            result.setMessage("获取文章选择列表失败");
        }
        logger.debug("获取文章选择列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 *保存添加文章列表
	 * @param request
	 * @param response
	 * @param articlesList
	 * @return
	 */
	@RequestMapping(value = "/addMorningpaperArticles.do")
	@ResponseBody
    public Response addMorningpaperArticles(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhMorningPaperArticleDTO morningPaperArticleDTO) {
		logger.debug("***************************保存添加文章列表********************************");
		logger.debug("保存添加文章选择请求参数:"+ JSON.toJSONString(morningPaperArticleDTO));
        Response result = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	String createUser = userVO.getUser_name();
        	
        	result = morningPaperService.addMorningpaperArticles(morningPaperArticleDTO, createUser);
        } catch (Exception e) {
            logger.error("保存添加文章列表失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode("1");
            result.setMessage("保存添加文章列表失败");
        }
        logger.debug("保存添加文章列表，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 保存财税早报文章上移、下移
	 * @param request
	 * @param response
	 * @param morningPaperArticleDTO
	 * @return
	 */
	@RequestMapping(value = "/updateMorningpaperArticles.do")
	@ResponseBody
    public Response updateMorningpaperArticles(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhMorningPaperArticleDTO morningPaperArticleDTO) {
		logger.debug("***************************保存财税早报文章上移、下移********************************");
		logger.debug("保存财税早报文章上移、下移请求参数:"+ JSON.toJSONString(morningPaperArticleDTO));
        Response result = Response.getDefaulTrueInstance();
        //String modifyUser = "黎";
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	String modifyUser = userVO.getUser_name();
        	 
        	result = morningPaperService.updateMorningpaperArticles(morningPaperArticleDTO, modifyUser);
        	result.setMessage("保存财税早报文章上移、下移成功");
        } catch (Exception e) {
            logger.error("保存财税早报文章上移、下移失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode("1");
            result.setMessage("保存财税早报文章上移、下移失败");
        }
        logger.debug("保存财税早报文章上移、下移，返回数据={}", JSON.toJSON(result));
        return result;
    }
	
	/**
	 * 删除早报文章
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delMorningpaperArticles.do")
    @ResponseBody
    public Response delMorningpaperArticles(HttpServletRequest request, HttpServletResponse response, @RequestParam int id) {
		logger.debug("***************************删除早报文章********************************");
		logger.debug("删除早报文章请求参数:{}", id);
        Response result = Response.getDefaulTrueInstance();
        try {
        	result = morningPaperService.delMorningpaperArticles(id);
        } catch (Exception e) {
            logger.error("删除早报文章失败，错误原因：" + e.getMessage());
            result.setSuccess(false);
            result.setCode(Constants.RESPONSE_CODE_FAIL);
            result.setMessage("删除早报文章失败");
        }
        logger.debug("删除早报文章，返回数据={}", JSON.toJSON(result));
        return result;
    }
}
