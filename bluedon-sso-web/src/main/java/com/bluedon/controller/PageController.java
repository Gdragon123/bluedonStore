package com.bluedon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

	/**跳转到登录或者注册页面
	 * @param page
	 * @return
	 */
	@RequestMapping("/page/{page}")
	public String page(@PathVariable String page,String redirect,Model model){
		//将重定向过来的参数传递到jsp页面
		model.addAttribute("redirect", redirect);
		return page;
	}
	
}
