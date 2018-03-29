package com.bluedon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluedon.dao.Item;
import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbItemDesc;
import com.bluedon.service.TbItemService;

/**获取商品详情
 * @author Administrator
 *
 */
@Controller
public class ItemController {

	@Autowired
	private TbItemService tbItemService;
	
	@RequestMapping("/item/{itemId}")
	public String getItemById(@PathVariable Long itemId,Model model){
		
		//注入服务
		//调用服务
		//执行方法
		TbItem tbItem = tbItemService.getTbItemById(itemId);
		//转成Item对象
		Item item = new Item(tbItem);
		
		TbItemDesc itemDesc = tbItemService.getTbItemDescById(itemId);
		System.out.println(itemDesc.getItemDesc());
		//设置数据到页面 
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);
		//返回逻辑视图
		return "item";
	}
	
}
