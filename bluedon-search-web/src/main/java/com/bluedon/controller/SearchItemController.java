package com.bluedon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bluedon.common.pojo.SearchResult;
import com.bluedon.search.service.SearchItemsService;

@Controller
public class SearchItemController {
	
	private static final Integer ITEM_ROWS = 60;

	@Autowired
	private SearchItemsService searchItemsService;
	
	/**
	 * 获取搜索结果  url:/search 参数：page q 返回值：逻辑视图  method:get
	 * @param queryString
	 * @param page
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String searchItem(@RequestParam(value="q")String queryString,@RequestParam(defaultValue="1")Integer page,Model model) throws Exception{
		
		//1.引入服务
		//2.调用服务
		//3.执行方法
		queryString = new String(queryString.getBytes("iso-8859-1"), "utf-8");//配置get请求乱码   还可以在tomcat中配置URIEncoding
		SearchResult result = searchItemsService.searchItem(queryString, page, ITEM_ROWS); 
		//4.封装数据到页面;
		model.addAttribute("query", queryString);
		model.addAttribute("totaoPages",result.getPageCount());
		model.addAttribute("itemList", result.getItemList());
		model.addAttribute("page", page);
		return "search";
	}
	
}
