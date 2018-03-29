package com.bluedon.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

@Controller
public class GenHTMLTestController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genHtml")
	@ResponseBody
	public String genHtml() throws Exception{
		//1.从freemarkerConfiguratioin中获取configuration对象
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//2.创建模板文件
		//3.加载模板文件 获取template对象
		Template template = configuration.getTemplate("hello.ftl");
		//4.设置数据集
		Map model = new HashMap<>();
		model.put("hello", "world");
		//5.创建输出流
		Writer writer = new FileWriter(new File("E:\\新建文件夹 (2)\\other.html"));
		//6.通过template的process方法 输出静态文件
		template.process(model, writer);
		//7.关闭流
		writer.close();
		return "ok";
	}
	
}
