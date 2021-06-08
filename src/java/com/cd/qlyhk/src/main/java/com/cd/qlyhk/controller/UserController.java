package com.cd.qlyhk.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.api.prov.service.ICallCrmService;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.dto.QlyRhUserDTO;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.vo.QlyRhUserVO;

/**
 * 用户控制器
 * @author sailor
 *
 */
@Controller
@RequestMapping(value = "/openapi/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Value("${wx.user.ewm.upload}")
	public String ewmUploadUrl;
	
	@Value("${wx.user.ewm.url}")
	public String ewmUrl;
	
	@Value("${wx.appid}")
	public String appid;
	
	@Resource(name = IUserService.BEAN_ID)
	private IUserService   userService;
	
	@Resource(name = ICallCrmService.BEAN_ID)
	private ICallCrmService   callCrmService;
	
	/**
	 * 个人中心首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/main.do")
	@ResponseBody
	public Response main(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************个人中心首页********************************");
		String openId = request.getParameter("userId");
//		String openId = "oSVdLv05qzm-bo26hSxuFBZV-EoU";
		logger.info("个人中心首页请求参数:{}", openId);
		Response resp = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
    		resp = userService.queryUserHomePage(openId);
    		resp.setMessage("成功");
    		logger.info("***************************个人中心首页耗时********************************" + (System.currentTimeMillis() - time));
        } catch (Exception e) {
            logger.error("个人中心首页信息查询失败，错误原因：" + e.getMessage());
            resp.setSuccess(false);
            resp.setCode("1");
            resp.setMessage("个人中心首页信息查询失败");
        }
		logger.info("个人中心首页，返回数据={}", JSON.toJSON(resp));
        return resp;
	}
	
	/**
	 * 查询个人资料信息。
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/queryUserInfo.do")
	@ResponseBody
	public Response queryUserInfo(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************查询个人资料信息********************************");
		String openId = request.getParameter("userId");
//		String openId = "oSVdLv05qzm-bo26hSxuFBZV-EoU";
		logger.info("查询个人资料信息请求参数:{}", openId);
		Response result = Response.getDefaulTrueInstance();
		try {
    		
    		QlyRhUserVO userVO = userService.getUserVOByOpenId(openId, null, appid);
    		if(userVO != null) {
    			result.setData(userVO);
    		}
			
		} catch (Exception e) {
			logger.error("查询个人资料信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询个人资料信息失败");
		}
		
		logger.info("查询个人资料信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 保存设置名片信息
	 * @param request
	 * @param response
	 * @param userVO
	 * @return
	 */
	@RequestMapping("/saveEditUserInfo.do")
	@ResponseBody
	public Response saveEditUserInfo(HttpServletRequest request, HttpServletResponse response, QlyRhUserDTO userDTO) {
		logger.info("***************************保存设置名片信息********************************");
		logger.info("保存设置名片信息请求参数：openId={}", userDTO.getOpenId());
		logger.info("保存设置名片信息请求参数：user_name={}", userDTO.getUser_name());
		logger.info("保存设置名片信息请求参数：telphone={}", userDTO.getTelphone());
		logger.info("保存设置名片信息请求参数：position={}", userDTO.getPosition());
		logger.info("保存设置名片信息请求参数：company={}", userDTO.getCompany());
		logger.info("保存设置名片信息请求参数：city={}", userDTO.getCity());
		logger.info("保存设置名片信息请求参数：area_code={}", userDTO.getArea_code());
		logger.info("保存设置名片信息请求参数：personal_profile={}", userDTO.getPersonal_profile());
		Response result = Response.getDefaulTrueInstance();
		try {
            //使用UUID给图片重命名，并去掉四个“-”
            String ewmName = UUID.randomUUID().toString().replaceAll("-", "");
            if(userDTO.getEwmImg() != null) {
            	MultipartFile ewmImg = userDTO.getEwmImg();
            	double fileSize = ewmImg.getSize() / 1048576;// M
                if(fileSize > 5) {
                	logger.error("***************二维码图片大小超过5M****************" + fileSize);
                	result.setCode("1");
                	result.setSuccess(false);
                	result.setMessage("二维码图片大小超过5M");
                    return result;
                }
                //获取文件的扩展名
                String ewmExt = FilenameUtils.getExtension(ewmImg.getOriginalFilename());
                //根据日期来创建对应文件夹
                String datePath=new SimpleDateFormat("yyyyMMdd/").format(new Date());
                String path = ewmUploadUrl + datePath;
                //如果不存在,创建文件夹
                File f = new File(path);
                if(!f.exists()){
                    f.mkdirs();
                }
                //以绝对路径保存重名命后的图片
                String picName = path + ewmName + "." + ewmExt;
                ewmImg.transferTo(new File(picName));
                //把图片存储路径保存到数据库
                String dataBasePicName = ewmUrl + datePath + ewmName + "." + ewmExt;
                userDTO.setEwm_url(dataBasePicName);
            }   
            result = userService.saveEditUserInfo(userDTO);
		} catch (Exception e) {
			logger.error("保存设置名片信息失败，错误原因：" + e.getMessage());
			result.setSuccess(false);
			result.setCode("1");
			result.setMessage("保存设置名片信息失败");
		}
		logger.info("保存设置名片信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 更新个人资料信息。
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/updateUserInfo.do")
	@ResponseBody
	public Response updateUserInfo(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************更新个人资料信息********************************");
		String openId = request.getParameter("openId");
		logger.info("更新个人资料信息请求参数:{}", openId);
		Response result = Response.getDefaulTrueInstance();
		try {
			userService.updateWxUserInfo(openId, appid);
			result.setMessage("更新个人资料信息成功");
		} catch (Exception e) {
			logger.error("更新个人资料信息异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("更新个人资料信息失败");
		}
		
		logger.info("更新个人资料信息，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 同步会员到期日
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/syncUserMemberEndDate.do")
	@ResponseBody
	public Response syncUserMemberEndDate(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************同步会员到期日********************************");
		Response result = Response.getDefaulTrueInstance();
		long time = System.currentTimeMillis();
		try {
			userService.syncUserMemberEndDate();
			result.setMessage("同步会员到期日成功");
			logger.info("***************************同步会员到期日耗时********************************" + (System.currentTimeMillis() - time));
		} catch (Exception e) {
			logger.error("同步会员到期日异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("同步会员到期日失败");
		}
		
		logger.info("同步会员到期日，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * 查询用户是否是会员。
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/getIsMember.do")
	@ResponseBody
	public Response getIsMember(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************查询用户是否是会员********************************");
		String openId = request.getParameter("openId");
//		String openId = "oSVdLv05qzm-bo26hSxuFBZV-EoU";
		logger.info("查询用户是否是会员请求参数:{}", openId);
		Response result = Response.getDefaulTrueInstance();
		try {
    		String isMember = userService.getIsMember(openId);
    		Map<String, Object> resultMap = new HashMap<String, Object>();
    		resultMap.put("isMember", isMember);
    		
    		result.setData(resultMap);
		} catch (Exception e) {
			logger.error("查询用户是否是会员异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("查询用户是否是会员失败");
		}
		
		logger.info("查询用户是否是会员，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	@RequestMapping(value = "/test.do")
    @ResponseBody
	public Response test(HttpServletRequest request, HttpServletResponse response, QlyRhUserDTO userDTO) {
		logger.info("***************************测试测试测试********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			result = userService.saveEditUserInfo(userDTO);
            //result.setMessage("保存设置名片信息成功");
		} catch (Exception e) {
			logger.error("测试异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("测试失败");
		}
		logger.info("测试，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * testAddClue
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/testAddClue.do")
	@ResponseBody
	public Response testAddClue(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************testAddClue********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			callCrmService.addClue("wx2abd6374e47ec245", "o-gUw1jdT9QtCKTArDnmNuHy2834", "o-gUw1lqSbVfp3ALd8KjJH4_gVsw", "e7b88477-2751-4b39-9ace-a504598f5bea");
			result.setMessage("testAddClue成功");
		} catch (Exception e) {
			logger.error("testAddClue异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("testAddClue失败");
		}
		
		logger.info("testAddClue，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * testGetCustCount
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/testGetCustCount.do")
	@ResponseBody
	public Response testGetCustCount(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************testGetCustCount********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			callCrmService.getCustCount("o-gUw1jdT9QtCKTArDnmNuHy2834");
			result.setMessage("testGetCustCount成功");
		} catch (Exception e) {
			logger.error("testGetCustCount异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("testGetCustCount失败");
		}
		
		logger.info("testGetCustCount，返回数据={}", JSON.toJSON(result));
	    return result;
	}
	
	/**
	 * testSetUserCust
	 * @param request
	 * @param response
	 * @return Response
	 */
	@RequestMapping("/testSetUserCust.do")
	@ResponseBody
	public Response testSetUserCust(HttpServletRequest request, HttpServletResponse response) {
		logger.info("***************************testSetUserCust********************************");
		Response result = Response.getDefaulTrueInstance();
		try {
			callCrmService.setUserCust("o-gUw1jdT9QtCKTArDnmNuHy2834", 894);
			result.setMessage("testSetUserCust成功");
		} catch (Exception e) {
			logger.error("testSetUserCust异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("testSetUserCust失败");
		}
		
		logger.info("testSetUserCust，返回数据={}", JSON.toJSON(result));
	    return result;
	}
}
