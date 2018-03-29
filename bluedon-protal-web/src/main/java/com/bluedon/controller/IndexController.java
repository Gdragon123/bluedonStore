package com.bluedon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluedon.pojo.Ad1Node;
import com.bluedon.pojo.TbContent;
import com.bluedon.service.ContentService;
import com.bluedon.utils.JsonUtils;

@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@Value("${AD1_CATEGORY_ID}")
	private long AD1_CATEGORY_ID;
	@Value("${AD1_HEIGHT}")
	private String AD1_HEIGHT;
	@Value("${AD1_HEIGHT_B}")
	private String AD1_HEIGHT_B;
	@Value("${AD1_WIDTH}")
	private String AD1_WIDTH;
	@Value("${AD1_WIDTH_B}")
	private String AD1_WIDTH_B;
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/index")
	public String showIndex(Model model){
		//1.引入服务
		//2.调用服务
		//3.执行方法
		List<TbContent> list = contentService.getContentCatListById(AD1_CATEGORY_ID);
		//4.封装数据
		List<Ad1Node> nodes = new ArrayList<>();
		
		for (TbContent tbContent : list) {
			Ad1Node ad1Node = new Ad1Node();
			ad1Node.setAlt(tbContent.getSubTitle());
			ad1Node.setSrc(tbContent.getPic());
			ad1Node.setHeight(AD1_HEIGHT);
			ad1Node.setHeightB(AD1_HEIGHT_B);
			ad1Node.setSrcB(tbContent.getPic2());
			ad1Node.setWidth(AD1_WIDTH);
			ad1Node.setWidthB(AD1_WIDTH_B);
			nodes.add(ad1Node);
		}
		//5.返回json数据到jsp页面
		model.addAttribute("ad1",JsonUtils.objectToJson(nodes));
		return "index";
	}
	
}
