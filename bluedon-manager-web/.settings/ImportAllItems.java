package com.bluedon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.search.service.SearchItemsService;


@Controller
public class ImportAllItems {

	@Autowired
	private SearchItemsService service;

	/**
	 * 请求的url：/search/importAll
		参数：无
		返回值：json数据。TaotaoResult。
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/search/importAll")
	@ResponseBody
	public TaotaoResult importAllItems() throws Exception{
		
		return service.importAllItems();
		
	}
	
}
