package com.cd.qlyhk.admin.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.cd.qlyhk.common.cache.IDataCacheService;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.constants.DataCacheConst;
import com.cd.qlyhk.dto.QlyRhAdminUserDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IPubCommonService;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.vo.QlyRhUserVO;
import com.cd.qlyhk.vo.ReqUserCustomQueryDTO;
import com.cd.qlyhk.vo.ReqUserCustomTableDTO;

/**
 * 用户管理控制器
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/admin/user")
public class UserManageController {

	private static final Logger logger = LoggerFactory.getLogger(UserManageController.class);

	@Resource(name = IUserService.BEAN_ID)
	private IUserService   userService;
	
	@Resource(name = IPubCommonService.BEAN_ID)
	private IPubCommonService  pubCommonService;
	
	@Resource(name = IDataCacheService.BEAN_ID)
	private IDataCacheService  dataCacheService;
	
	/**
	 * 用户登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/login.do")
	@ResponseBody
	public Response login(HttpServletRequest request, HttpServletResponse response, @RequestParam String loginName, @RequestParam String password) {
		logger.debug("***************************用户登录********************************");
		logger.debug("用户登录请求参数:{},{}", loginName, password);
		Response resp = Response.getDefaulTrueInstance();
		try {
    		resp = userService.loginUser(loginName, password);
//    		if(resp.getData() != null) {
//    			QlyRhUserVO userVO = (QlyRhUserVO) resp.getData();
                // 将登录用户设置值session
//                request.getSession().setAttribute(Constants.LOGIN_SESSION_ADMIN_NAME, userVO);
    			
//    		}
        } catch (Exception e) {
            logger.error("用户登录失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("用户登录失败");
        }
		logger.debug("用户登录，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 查询用户自定义查询分类列表
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryUserCustomQuery.do")
    @ResponseBody
    public Response queryUserCustomQuery(HttpServletRequest request, HttpServletResponse response, @RequestParam String module_code) {
		logger.debug("***************************查询用户自定义查询分类列表********************************");
		logger.debug("查询用户自定义查询分类列表请求参数:{}", module_code);
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	resp = pubCommonService.queryUserCustomQuery(userVO.getUser_id(), module_code);
        	resp.setMessage("查询用户自定义查询分类列表成功");
        } catch (Exception e) {
            logger.error("查询用户自定义查询分类列表失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询用户自定义查询分类列表失败");
        }
        logger.debug("查询用户自定义查询分类列表，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 新增用户自定义查询分类
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/addUserCustomQuery.do")
    @ResponseBody
    public Response addUserCustomQuery(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqUserCustomQueryDTO customQueryDTO) {
		logger.debug("***************************新增用户自定义查询分类********************************");
		logger.debug("新增用户自定义查询分类请求参数:"+ JSON.toJSONString(customQueryDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	customQueryDTO.setUser_id(userVO.getUser_id());
        	pubCommonService.insertUserCustomQuery(customQueryDTO);
        	
        	Map<String, Object> retMap = new HashMap<String, Object>();
			retMap.put("id", customQueryDTO.getId());
			resp.setData(retMap);
        	resp.setMessage("新增用户自定义查询分类成功");
        } catch (Exception e) {
            logger.error("新增用户自定义查询分类失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("新增用户自定义查询分类失败");
        }
        logger.debug("新增用户自定义查询分类，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 根据用户自定义查询分类查询数据
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryDataForUserCustomQuery.do")
    @ResponseBody
    public Response queryDataForUserCustomQuery(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqUserCustomQueryDTO customQueryDTO) {
		logger.debug("***************************根据用户自定义查询分类查询数据********************************");
		logger.debug("根据用户自定义查询分类查询数据请求参数:"+ JSON.toJSONString(customQueryDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	customQueryDTO.setUser_id(userVO.getUser_id());
        	resp = pubCommonService.queryDataForUserCustomQuery(customQueryDTO);
        	resp.setMessage("根据用户自定义查询分类查询数据成功");
        } catch (Exception e) {
            logger.error("根据用户自定义查询分类查询数据失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("根据用户自定义查询分类查询数据失败");
        }
        logger.debug("根据用户自定义查询分类查询数据，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 删除用户自定义查询分类
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "/deleteUserCustomQuery.do")
    @ResponseBody
    public Response deleteUserCustomQuery(HttpServletRequest request, HttpServletResponse response, @RequestParam int customQueryId) {
		logger.debug("***************************删除用户自定义查询分类********************************");
		logger.debug("删除用户自定义查询分类请求参数:{}", customQueryId);
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	resp = pubCommonService.deleteUserCustomQuery(customQueryId, userVO.getUser_id());
        } catch (Exception e) {
            logger.error("删除用户自定义查询分类失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("删除用户自定义查询分类失败");
        }
        logger.debug("删除用户自定义查询分类，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 新增用户自定义表格列显示
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/addUserCustomTable.do")
    @ResponseBody
    public Response addUserCustomTable(HttpServletRequest request, HttpServletResponse response, @RequestBody ReqUserCustomTableDTO customTableDTO) {
		logger.debug("***************************新增用户自定义表格列显示********************************");
		logger.debug("新增用户自定义表格列显示请求参数:"+ JSON.toJSONString(customTableDTO));
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	customTableDTO.setUser_id(userVO.getUser_id());
        	pubCommonService.insertOrUpdateUserCustomTable(customTableDTO);
        	resp.setMessage("新增用户自定义表格列显示成功");
        } catch (Exception e) {
            logger.error("新增用户自定义表格列显示失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("新增用户自定义表格列显示失败");
        }
        logger.debug("新增用户自定义表格列显示，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询用户自定义表格列显示
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryUserCustomTable.do")
    @ResponseBody
    public Response queryUserCustomTable(HttpServletRequest request, HttpServletResponse response, @RequestParam String module_code) {
		logger.debug("***************************查询用户自定义表格列显示********************************");
		logger.debug("查询用户自定义表格列显示请求参数:{}", module_code);
        Response resp = Response.getDefaulTrueInstance();
        try {
        	String token = request.getHeader("token");
        	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
        	resp = pubCommonService.queryUserCustomTable(userVO.getUser_id(), module_code);
        	resp.setMessage("查询用户自定义表格列显示成功");
        } catch (Exception e) {
            logger.error("查询用户自定义表格列显示失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询用户自定义表格列显示失败");
        }
        logger.debug("查询用户自定义表格列显示，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
	 * 查询默认显示的表格列
	 * @param request
	 * @param response
	 * @param articleDTO
	 * @return
	 */
    @RequestMapping(value = "/queryDefaultCustomTable.do")
    @ResponseBody
    public Response queryDefaultCustomTable(HttpServletRequest request, HttpServletResponse response, @RequestParam String module_code) {
		logger.debug("***************************查询默认显示的表格列********************************");
		logger.debug("查询默认显示的表格列请求参数:{}", module_code);
        Response resp = Response.getDefaulTrueInstance();
        try {
        	resp = pubCommonService.queryDefaultCustomTable(module_code);
        	resp.setMessage("查询默认显示的表格列成功");
        } catch (Exception e) {
            logger.error("查询默认显示的表格列失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("查询默认显示的表格列失败");
        }
        logger.debug("查询默认显示的表格列，返回数据={}", JSON.toJSON(resp));
        return resp;
    }
    
    /**
                   *  查询用户列表
     * @param request
     * @param response
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/getUsersList.do")
    @ResponseBody
    public Response getUsersList(HttpServletRequest request, HttpServletResponse response, @RequestBody QlyRhAdminUserDTO userDTO) {
    	logger.debug("***************************查询用户列表********************************");
    	logger.debug("查询用户列表请求参数:" + JSON.toJSONString(userDTO));
    	Response resp = Response.getDefaulTrueInstance();
    	try {
    		resp = userService.getUsersList(userDTO);
    		resp.setMessage("查询用户列表成功");
    	} catch (Exception e) {
    		logger.error("查询用户列表失败，错误原因：" + e.getMessage());
    		resp.setSuccess(false);
    		resp.setCode("1");
    		resp.setMessage("查询用户列表失败");
    	}
    	logger.debug("查询用户列表，返回数据={}", JSON.toJSON(resp));
    	return resp;
    }
    
    /**
                  * 根据openId查询个人信息
     * @param request
     * @param response
     * @param openId
     * @return
     */
    @RequestMapping(value = "/getUserInfoByOpenId.do")
    @ResponseBody
    public Response getUserInfoByOpenId(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
    	logger.debug("***************************根据openId查询个人信息********************************");
    	logger.debug("根据openId查询个人信息请求参数:{}", openId);
    	Response resp = Response.getDefaulTrueInstance();
    	try {
    		resp = userService.getUserInfoByOpenId(openId);
    		resp.setMessage("根据openId查询个人信息成功");
    	} catch (Exception e) {
    		logger.error("根据openId查询个人信息失败，错误原因：" + e.getMessage());
    		resp.setSuccess(false);
    		resp.setCode("1");
    		resp.setMessage("根据openId查询个人信息失败");
    	}
    	logger.debug("根据openId查询个人信息，返回数据={}", JSON.toJSON(resp));
    	return resp;
    }
    
    /**
     * 获取用户分享过的文章列表
     * @param request
     * @param response
     * @param openId
     * @return
     */
    @RequestMapping(value = "/queryUserShareArticlesList.do")
    @ResponseBody
    public Response queryUserShareArticlesList(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
    	logger.debug("***************************获取用户分享过的文章列表********************************");
    	logger.debug("获取用户分享过的文章列表请求参数:{}", openId);
    	Response resp = Response.getDefaulTrueInstance();
    	try {
    		resp = userService.queryUserShareArticlesList(openId);
    		resp.setMessage("获取用户分享过的文章列表成功");
    	} catch (Exception e) {
    		logger.error("获取用户分享过的文章列表失败，错误原因：" + e.getMessage());
    		resp.setSuccess(false);
    		resp.setCode("1");
    		resp.setMessage("获取用户分享过的文章列表失败");
    	}
    	logger.debug("获取用户分享过的文章列表，返回数据={}", JSON.toJSON(resp));
    	return resp;
    }
    
    /**
     * 获取用户阅读过的文章列表
     * @param request
     * @param response
     * @param openId
     * @return
     */
    @RequestMapping(value = "/queryUserReadArticlesList.do")
    @ResponseBody
    public Response queryUserReadArticlesList(HttpServletRequest request, HttpServletResponse response, @RequestParam String openId) {
    	logger.debug("***************************获取用户阅读过的文章列表********************************");
    	logger.debug("获取用户阅读过的文章列表请求参数:{}", openId);
    	Response resp = Response.getDefaulTrueInstance();
    	try {
    		resp = userService.queryUserReadArticlesList(openId);
    		resp.setMessage("获取用户阅读过的文章列表成功");
    	} catch (Exception e) {
    		logger.error("获取用户阅读过的文章列表失败，错误原因：" + e.getMessage());
    		resp.setSuccess(false);
    		resp.setCode("1");
    		resp.setMessage("获取用户阅读过的文章列表失败");
    	}
    	logger.debug("获取用户阅读过的文章列表，返回数据={}", JSON.toJSON(resp));
    	return resp;
    }
    
    /**
	 * 用户退出
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/logout.do")
	@ResponseBody
	public Response logout(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("***************************用户退出********************************");
		Response resp = Response.getDefaulTrueInstance();
		try {
			String token = request.getHeader("token");
			logger.debug("用户退出请求的token={}", token);
			String cacheKey = String.format(DataCacheConst.CS_cache_user_session_key, token);
//    		request.getSession().removeAttribute(Constants.LOGIN_SESSION_ADMIN_NAME);
			dataCacheService.del(cacheKey);
    		resp.setMessage("用户已退出");
        } catch (Exception e) {
            logger.error("用户退出失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("用户退出失败");
        }
		logger.debug("用户退出，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
}
