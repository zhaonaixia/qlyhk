package com.cd.qlyhk.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.ISignUpService;
import com.cd.qlyhk.vo.SignUpDTO;

@Controller
@RequestMapping(value = "/openapi/signup")
public class SignUpController {

	private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

	@Resource(name = ISignUpService.BEAN_ID)
	private ISignUpService  signUpService;
	
	/**
	 * 报名提交。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/submitSignUp.do")
	@ResponseBody
	public Response submitSignUp(HttpServletRequest request, HttpServletResponse response,
			@RequestBody SignUpDTO signUpDTO) {
		logger.info("***************************报名提交********************************");
		logger.info("报名提交请求参数:{}", JSON.toJSONString(signUpDTO));
		Response result = Response.getDefaulTrueInstance();
		try {
			result = signUpService.insertSignUp(signUpDTO);
		} catch (Exception e) {
			logger.error("报名提交异常，具体异常信息为:{}", e.getMessage(), e);
			result.setCode(Constants.RESPONSE_CODE_FAIL);
			result.setMessage("报名提交失败");
		}
		logger.info("报名提交，返回数据={}", JSON.toJSON(result));
		return result;
	}
}
