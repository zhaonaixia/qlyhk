package com.cd.qlyhk.service;

import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.vo.SignUpDTO;

/**
 *报名服务接口
 * @author sailor
 *
 */
public interface ISignUpService {

	final String BEAN_ID = "signUpService";
	
	/**
	 * 新增报名
	 * @param signUpDTO
	 */
	Response insertSignUp(SignUpDTO signUpDTO);
	
}
