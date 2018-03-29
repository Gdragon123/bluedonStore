package com.bluedon.activemq;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.bluedon.pojo.TbItem;
import com.bluedon.pojo.TbItemDesc;
import com.bluedon.service.TbItemService;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

public class GenHTMLItemChangeListener implements MessageListener{

	@Autowired
	private TbItemService itemService;
	
	@Autowired
	private FreeMarkerConfig freemarkerConfig;
	
	@Value("${FREEMARKER_ITEM_PATH}")
	private String FREEMARKER_ITEM_PATH;
	
	@Override
	public void onMessage(Message message) {
		//接受消息 商品id
		if(message instanceof TextMessage){
			TextMessage textMessage = (TextMessage) message;
			try {
				//商品id
				Long itemId = Long.parseLong(textMessage.getText().toString());
				//调用商品服务，查询商品的基本信息和描述信息
				TbItem item = itemService.getTbItemById(itemId);
				TbItemDesc itemDesc = itemService.getTbItemDescById(itemId);
				//调用生成freemarker模板的方法
				genHTMLFreemarker("item.ftl",item,itemDesc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void genHTMLFreemarker(String teplateName, TbItem item, TbItemDesc itemDesc) throws Exception{
		//1.注入FreemarkerConfigurer
		//2.获取configuration对象
		Configuration configuration = freemarkerConfig.getConfiguration();
		//3.根据传递过来的模板的名称获取模板的对象
		Template template = configuration.getTemplate(teplateName);
		//4.设置数据集
		Map model = new HashMap<>();
		model.put("item", item);
		model.put("itemDesc", itemDesc);
		//5.创建writer
		Writer writer = new FileWriter(new File(FREEMARKER_ITEM_PATH+item.getId()+".html"));
		//6.调用模板对象的process方法输出到静态页面
		template.process(model, writer);
		//7.关闭流
		writer.close();
	}

}
