package com.bluedon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.common.pojo.EasyUIDataGridResult;
import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.pojo.TbContent;
import com.bluedon.service.ContentService;


@Controller
public class ContentCtroller {
	
	//引入服务
	//注入服务
	@Autowired
	private ContentService contentService;

	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult saveContent(TbContent content){
		
		return contentService.saveContent(content);
		
	}
	
	@RequestMapping(value="/content/query/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId ,Integer page ,Integer rows){
		
		return contentService.getContentList(categoryId, page, rows);
		
	}
	
	/*@RequestMapping(value="/rest/content/edit",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult editContent(TbContent content){
		
		System.out.println("content的内容 ： "+content.getContent());
		return contentService.editContent(content);
		
	}
	
	@RequestMapping(value="/content/delete",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult deleteContent(@RequestParam(value="ids") Long id){
		
		return contentService.deleteContent(id);
		
	}*/
	
}
