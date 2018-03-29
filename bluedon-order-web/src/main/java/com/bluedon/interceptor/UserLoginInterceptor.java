package com.bluedon.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.service.UserLoginService;
import com.bluedon.utils.CookieUtils;

public class UserLoginInterceptor implements HandlerInterceptor {

	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Autowired
	private UserLoginService userService;
	
	//在进入controller的方法之前调用，可以用来拦截认证
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//获取cookie中的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//根据token获取用户信息
		TaotaoResult result = userService.getUserByToken(token);
		//判断用户是否登录
		if(result.getStatus() != 200){
			//如果未登录，就跳转到登录页面
			response.sendRedirect(SSO_LOGIN_URL+"/page/login?redirect="+request.getRequestURL());//request.getRequestURL() 获取当前请求的url 
			return false;//返回false，表示不执行controller的方法
		}else{
			//如果已登录，就放行
			request.setAttribute("USER_INFO", result.getData());//解决请求两次sso服务的问题，以为拦截器和controller中的request是同一个，所以可以将用户数据设置到request中传递过去
			return true;
		}
		
	}

	//在进入controller的方法之后，返回modelandview之前调用
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	//在controller的方法执行完后调用，用于处理资源，异常等等
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
