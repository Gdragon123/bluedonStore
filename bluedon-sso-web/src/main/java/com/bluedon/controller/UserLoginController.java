package com.bluedon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.service.UserLoginService;
import com.bluedon.utils.CookieUtils;
import com.bluedon.utils.JsonUtils;

@Controller
public class UserLoginController {
	
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	@Autowired
	private UserLoginService userLoginService;
	
	/**用户登录
	 * @param username
	 * @param password
	 * @param request 
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		//注入服务
		//调用服务
		//执行方法，获取返回值
		TaotaoResult result = userLoginService.login(username, password);
		//判断登录是否成功
		if(result.getStatus() == 200){
			//如果成功，将用户信息存入cookie中
			String token = result.getData().toString();
			//用工具类实现cookie跨越
			CookieUtils.setCookie(request, response, TT_TOKEN_KEY, token);
		}
		//返回提示 TaotaoResult，其中包含Token
		return result;
	}
	
	/**根据token获取用户数据
	 * @param token
	 * @param callback 回调函数
	 * @return
	 */
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		//注入服务
		//调用服务
		//执行方法
		TaotaoResult result = userLoginService.getUserByToken(token);
		//判断callback是否为空
		if(StringUtils.isNotBlank(callback)){
			//如果不为空，说明需要跨域请求，需要拼接成类似：fun({"id":1})的json字符串
			String jsonstr = callback + "("+JsonUtils.objectToJson(result)+");";
			return jsonstr;
		}
		
		return JsonUtils.objectToJson(result);
	}
	
}
