package com.bluedon.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbUser;
import com.bluedon.service.CartService;
import com.bluedon.service.TbItemService;
import com.bluedon.service.UserLoginService;
import com.bluedon.utils.CookieUtils;
import com.bluedon.utils.JsonUtils;

@Controller
public class CartController {

	@Autowired
	private TbItemService itemService;
	
	@Autowired
	private UserLoginService userService;
	
	@Autowired
	private CartService cartService;

	@Value("${TT_TOKEN_KEY}")
	private String TT_TOKEN_KEY;
	
	@Value("${TT_CART_KEY}")
	private String TT_CART_KEY;
	
	@Value("${TT_CART_EXPIRE_TIME}")
	private int TT_CART_EXPIRE_TIME;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addItemToCart(@PathVariable Long itemId,Integer num,HttpServletRequest request,HttpServletResponse response){
		//注入服务
		//调用服务
		//执行方法
		//根据商品id获取商品信息
		TbItem item = itemService.getTbItemById(itemId);
		//获取cookie中的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//根据token获取用户信息
		TaotaoResult result = userService.getUserByToken(token);
		//判断是否登录成功
		if(result.getStatus() == 200){
			//如果登录成功，获取用户信息
			TbUser user = (TbUser) result.getData();
			//添加到购物车
			cartService.addCatItem(item, user.getId()+"", num);
		}else{
			//如果未登录，添加商品到cookie中的购物车
			addCookieCart(itemId, num, request, response);
		}
		return "cartSuccess";
	}
	
	@RequestMapping("/cart/cart")
	public String getCartList(HttpServletRequest request,Model model){
		//获取cookie中的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//根据token获取用户信息
		TaotaoResult result = userService.getUserByToken(token);
		//判断用户是否登录
		if(result.getStatus() == 200){
			//如果已登录，获取用户信息
			TbUser user = (TbUser) result.getData();
			//查询用户对应的购物车列表
			List<TbItem> list = cartService.getCartList(user.getId());
			//传递数据到jsp页面
			model.addAttribute("cartList", list);
		}else{
			//如果未登录，获取cookie中的购物车
			List<TbItem> list2 = getCartListInCookie(request);
			//传递数据到jsp页面
			model.addAttribute("cartList", list2);
		}
		return "cart";
	}
	
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response){
		//获取cookie中的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
			//如果token不为空，根据token查询用户信息
			TaotaoResult result = userService.getUserByToken(token);
			//判断用户是否登录
			if(result.getStatus() == 200){
				//如果已登录，获取用户信息
				TbUser user = (TbUser) result.getData();
				//更新购物车中的商品数量
				return cartService.updateCatByItemId(user.getId(), itemId, num);
			}else{
				TaotaoResult result2 = updateCookieCart(itemId, num, request, response);
				return result2;
			}
			
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCat(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
		//获取cookie中的token
		String token = CookieUtils.getCookieValue(request, TT_TOKEN_KEY);
		//根据token，获取用户信息
		TaotaoResult result = userService.getUserByToken(token);
		//判断用户是否登录
		if(result.getStatus() == 200){
			//如果已登录，获取用户信息
			TbUser user = (TbUser) result.getData();
			//删除购物车中的商品
			cartService.deleteCatByUserIdItemId(user.getId(), itemId);
		}else{
			//如果未登录，删除cookie中的购物车中的商品
			delItemInCookieCart(itemId, request, response);
		}
		//重定向到购物车页面
		return "redirect:/cart/cart.html";
	}
	
	private void addCookieCart(Long itemId,int num,HttpServletRequest request,HttpServletResponse response){
		//如果未登录，去cookie里面查询购物车列表
		List<TbItem> list = getCartListInCookie(request);
		boolean flag=false;
		//判断要添加的商品是否存在于购物车中
		if(list != null && list.size() > 0){
			for (TbItem tbItem : list) {
				if(tbItem.getId() == itemId.longValue()){//包装类型不能直接判断是否相等(比较的是存储地址)，要转成基本数据类型再比较(比较的是值)
					//如果存在，数量相加
					tbItem.setNum(tbItem.getNum()+num);
					flag=true;
					break;
				}
			}
		}
		if(flag){
			//重新设置购物车到cookie
			CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(list), TT_CART_EXPIRE_TIME,
					true);
		}else{
			//如果不存在cookie中，根据id获取商品信息
			TbItem item = itemService.getTbItemById(itemId);
			//设置数量和图片
			item.setNum(num);
			if(StringUtils.isNotBlank(item.getImage())){
				item.setImage(item.getImage().split(",")[0]);
			}
			//添加商品到购物车列表中
			list.add(item);
			//重新设置购物车到cookie
			CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(list), TT_CART_EXPIRE_TIME,
					true);
			
		}
	}
	
	public TaotaoResult updateCookieCart(Long itemId,int num,HttpServletRequest request,HttpServletResponse response){
		//如果未登录，去cookie里面查询购物车列表
		List<TbItem> list = getCartListInCookie(request);
		boolean flag=false;
		//判断要添加的商品是否存在于购物车中
		if(list != null && list.size() > 0){
			for (TbItem tbItem : list) {
				if(tbItem.getId() == itemId.longValue()){//包装类型不能直接判断是否相等(比较的是存储地址)，要转成基本数据类型再比较(比较的是值)
					//如果存在，数量相加
					tbItem.setNum(tbItem.getNum()+num);
					flag=true;
					break;
				}
			}
		}
		if(flag){
			//重新设置购物车到cookie
			CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(list), TT_CART_EXPIRE_TIME,
					true);
		}
		
		return TaotaoResult.ok();
	}
	
	//获取cookie中的购物车
	private List<TbItem> getCartListInCookie(HttpServletRequest request) {
		//获取cookie中的json数据
		String jsonstr = CookieUtils.getCookieValue(request, TT_CART_KEY,true);
		//判断是否为空
		if(StringUtils.isNotBlank(jsonstr)){
			//不为空，转成list集合
			List<TbItem> list = JsonUtils.jsonToList(jsonstr, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
	
	private TaotaoResult delItemInCookieCart(Long itemId,HttpServletRequest request,HttpServletResponse response){
		//如果未登录，去cookie里面查询购物车列表
		List<TbItem> list = getCartListInCookie(request);
		boolean flag=false;
		//判断要添加的商品是否存在于购物车中
		if(list != null && list.size() > 0){
			for (TbItem tbItem : list) {
				if(tbItem.getId() == itemId.longValue()){//包装类型不能直接判断是否相等(比较的是存储地址)，要转成基本数据类型再比较(比较的是值)
					//如果存在，删除该商品
					list.remove(tbItem);
					flag=true;
					break;
				}
			}
		}
		if(flag){
			//重新设置购物车到cookie
			CookieUtils.setCookie(request, response, TT_CART_KEY, JsonUtils.objectToJson(list), TT_CART_EXPIRE_TIME,
					true);
		}
		
		return TaotaoResult.ok();
	}
}
