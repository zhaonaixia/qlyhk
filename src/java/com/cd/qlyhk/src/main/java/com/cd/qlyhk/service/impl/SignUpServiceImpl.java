package com.cd.qlyhk.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.rest.Response;
import com.cd.qlyhk.service.ISignUpService;
import com.cd.qlyhk.vo.SignUpDTO;
import com.cd.rdf.base.dao.IBaseDAO;

@Service(ISignUpService.BEAN_ID)
public class SignUpServiceImpl implements ISignUpService {

	private static final Logger logger = LoggerFactory.getLogger(SignUpServiceImpl.class);

	private final String mapperNamespace = SignUpServiceImpl.class.getName();

	@Autowired
	private IBaseDAO baseDAO;

	
	@Override
	public Response insertSignUp(SignUpDTO signUpDTO) {
		Response result = Response.getDefaulTrueInstance();
		
		String companyName = signUpDTO.getCompanyName();
		if(companyName.indexOf(";") > -1) {
			String[] companyNames = companyName.split(";");
			for(String custName : companyNames) {
				signUpDTO.setCompanyName(custName);
				if(getSignUpDTO(signUpDTO)) {
					result.setCode(Constants.RESPONSE_CODE_FAIL);
					result.setMessage("报名成功，无需重复提交报名");
				} else {
					baseDAO.add(mapperNamespace + ".insertSignUp", signUpDTO);
				}
				
			}
		} else {
			
			if(getSignUpDTO(signUpDTO)) {
				result.setCode(Constants.RESPONSE_CODE_FAIL);
				result.setMessage("报名成功，无需重复提交报名");
			} else {
				baseDAO.add(mapperNamespace + ".insertSignUp", signUpDTO);
			}
			
		}
		
		return result;
	}

	private boolean getSignUpDTO(SignUpDTO signUpDTO) {
		boolean result = false;
		Map<String, Object> dataMap = baseDAO.findOne(mapperNamespace + ".getSignUpDTO", signUpDTO);
		if(dataMap != null && !dataMap.isEmpty()) {
			result = true;
		}
		return result;
	}
}
