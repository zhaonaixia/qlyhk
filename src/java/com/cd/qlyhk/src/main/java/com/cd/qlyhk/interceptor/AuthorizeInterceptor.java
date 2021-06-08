package com.cd.qlyhk.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cd.qlyhk.constants.Constants;
import com.cd.qlyhk.service.IUserService;
import com.cd.qlyhk.vo.QlyRhUserVO;

@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorizeInterceptor.class);

	/**
     * 不进行用户信息校验URL列表
     */
    private String[] excludeURLs = {"/login.do", "/logout.do"};
    
	@Value("${frontend.url}")
	private String frontendUrl;
	
	@Value("${admin.frontend.url}")
	private String adminFrontendUrl;
	
	@Autowired
	private IUserService userService;
	
	// 这个方法是在访问接口之前执行的
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		String path = request.getServletPath();
//		logger.info("*********************拦截URL*******************" + path);
		String reqURL = request.getServletPath();
        logger.info("请求的SessionID={}", request.getSession().getId());
        logger.info("请求的URL={}", reqURL);
		
		boolean isExclude = false;
        for (String excludeURL : excludeURLs) {
            if (reqURL.endsWith(excludeURL)) {
                isExclude = true;
                logger.info("请求的URL在excludeURLs列表中，将直接放行请求......................");
                break;
            }
        }
		
        if(!isExclude) {
        	// 客户端发起“预请求”的时候不校验token凭证是否有效
        	if(reqURL.startsWith("/admin") && !"OPTIONS".equals(request.getMethod())) {
        		String token = request.getHeader("token");
    			logger.info("用户token={}", token);
//    			QlyRhUserVO userVO = (QlyRhUserVO) request.getSession().getAttribute(Constants.LOGIN_SESSION_ADMIN_NAME);
            	QlyRhUserVO userVO = userService.getQlyRhUserByToken(token);
    			if(userVO == null) {
    				logger.info("*********************SESSION失效，请重新登录*******************");
//    				String localUrl = adminFrontendUrl + "#/login";
    				response.sendError(401);
//    				response.sendRedirect(localUrl);
    				return false;
    			}
    		} else if(reqURL.startsWith("/openapi") && (reqURL.contains("queryArticleReadCond") || reqURL.contains("queryArticleReadCondDetails")
    				|| reqURL.contains("queryRecordCondition") || reqURL.contains("queryRecordConditionDetails"))) {
    			String openId = request.getParameter("openId");
    			//是否会员
    			String isMember = userService.getIsMember(openId);
    			logger.info(openId + "*********************是否会员*******************" + isMember);
    			if("0".equals(isMember))  {
    				String localUrl = frontendUrl + "#/connections?userId=" + openId;
    				response.sendRedirect(localUrl);
    				return false;
    			}
    		}
        }
        
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
	}
}
