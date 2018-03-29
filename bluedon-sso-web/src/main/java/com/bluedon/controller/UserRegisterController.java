package com.bluedon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbUser;
import com.bluedon.service.UserRegisterService;

@Controller
public class UserRegisterController {

	@Autowired
	private UserRegisterService userRegisterService;
	
	/**
	 * @param param
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/user/check/{param}/{type}",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult checkData(@PathVariable String param,@PathVariable Integer type){
		//注入服务
		//调用服务
		//执行方法
		return userRegisterService.checkData(param, type);
	}
	
	/**
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user){
		//注入服务
		//调用服务
		//执行方法
		return userRegisterService.register(user);
	}
	
}
