package com.bluedon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.common.pojo.EasyUITreeNode;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbContentCategory;
import com.bluedon.service.ContentCategoryService;


@Controller
public class ContentCategoryController {
	
	//引入服务		
	//注入服务
	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping(value="/content/category/list",method=RequestMethod.GET)
	@ResponseBody
	public List<EasyUITreeNode> getContentCategory(@RequestParam(value="id",defaultValue="0") Long parentId){
		
		//调用方法		
		return contentCategoryService.getContentCategory(parentId);
		
	}
	
	
	@RequestMapping("/content/category/create")
	@ResponseBody
	public TaotaoResult saveContentCategory(TbContentCategory category){
		
		return contentCategoryService.saveContentCategory(category);
		
	}
	
}
