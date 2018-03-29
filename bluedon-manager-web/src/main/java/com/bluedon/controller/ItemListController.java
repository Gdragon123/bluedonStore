package com.bluedon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.common.pojo.EasyUIDataGridResult;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbItem;
import com.bluedon.service.TbItemService;


@Controller
public class ItemListController {

	@Autowired
	private TbItemService itemListService;
	
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getList(Integer page , Integer rows){
		
		//注入服务
		//引用服务
		//调用方法
		
		return itemListService.getItemList(page, rows);
		
	}
	
	
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult saveItem(TbItem item , String desc){
		
		
		return itemListService.saveItem(item, desc);
		
	}
	
}
