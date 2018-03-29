package com.bluedon.search.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.bluedon.common.pojo.TaotaoResult;
import com.bluedon.search.service.SearchItemsService;

public class SearchItemMessageListener implements MessageListener {

	@Autowired
	private SearchItemsService searchItemsService;
	
	@Override
	public void onMessage(Message message) {

		//获取消息
		if(message instanceof TextMessage){
			TextMessage textMessage = (TextMessage) message;
			try {
				Long itemId = Long.parseLong(textMessage.getText());
				//获取商品信息
				//使用solrj更新到索引库
				TaotaoResult result = searchItemsService.updateSearchItemById(itemId);
				System.out.println(result.getStatus());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
