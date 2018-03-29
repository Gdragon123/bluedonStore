package com.bluedon.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.OrderInfo;
import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbUser;
import com.bluedon.service.OrderService;
import com.bluedon.service.UserLoginService;
import com.bluedon.utils.CookieUtils;

@Controller
public class OrderController {

	@Autowired
	private UserLoginService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	@RequestMapping("order/order-cart")
	public String showOrder(HttpServletRequest request,Model model){
		//获取cookie中的token
//		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//根据token获取用户信息
//		TaotaoResult result = userService.getUserByToken(token);
		//判断用户是否登录
//		if(result.getStatus() == 200){//已登录
			//获取用户信息
//			TbUser user = (TbUser) result.getData();
			//获取request中的用户数据  这时候肯定是登录了的
			TbUser user = (TbUser) request.getAttribute("USER_INFO");
			//根据用户id获取购物车数据
			List<TbItem> list = orderService.getOrderByUserId(user.getId());
			//将数据传递到jsp页面
			model.addAttribute("cartList", list);
//		}else{//未登录
//			//TODO 
//		}
		
		return "order-cart";
	}
	
	@RequestMapping(value="/order/create", method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request,Model model){
		//接收表单提交的pojo数据
		//补全用户信息
		TbUser user = (TbUser) request.getAttribute("USER_INFO");
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//创建订单
		TaotaoResult result = orderService.createOrder(orderInfo);
		//获取订单号
		String orderId = (String) result.getData();
		//传递信息到jsp页面
		model.addAttribute("orderId", orderId);
		model.addAttribute("payment", orderInfo.getPayment());
		//当前日期加三天
		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusDays(3);
		model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));
		
		return "success";
	}
	
	
}
