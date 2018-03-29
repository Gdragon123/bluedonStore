package com.bluedon.exception;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

public class GlobalExceptionReslover implements org.springframework.web.servlet.HandlerExceptionResolver {

	Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception ex) {
		//1.写日志文件
		logger.error("系统发生异常",ex);
		//2.发邮件，发短信给管理员  调用第三方服务
		
		//3.展示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("message", "系统发生异常，请稍后重试");
		modelAndView.setViewName("error/exception");
		
		return modelAndView;
	}

}
